import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Connection {
	//may need to change name of project in comments
    
    //parameters
	final int defaultTimeOut = 0;
    Socket socket;
    ServerSocket server;
    PrintStream ps;
    BufferedReader br;
    
    
    /**
     * Factory methods hook into this constructor to finish their job. This method reads a socket and creates the read and write streams
     * needed to send and receive data along the connection.
     *
     * @param socket The socket whose streams need to be accessed
     * @throws RTSPException if an IOException occurs among the streams
     */
    private Connection(Socket socket) throws RTSPException {//constructor should not be directly used, use factory methods
    	try {
    		this.socket = socket;//the connection itself
    		ps = new PrintStream(socket.getOutputStream());//use this to write and send commands
    		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));//use this to read messages from the server	
    		socket.setSoTimeout(defaultTimeOut);
    		
    	} catch(IOException e) {
    		throw new RTSPException("IO connection error");
    	}
    }
    
    /**
     * Factory method that establishes a new connection with an InterMessage in the receiver state using an explicit host and port number.
     * This constructor does not send any request for additional data.
     *
     * @param host Name of the host where the InterMessage receiver is running
     * @param port Port number used by the receiver
     * @throws RTSPException If the host does not exist or the connection can't be established.
     */
    
    public static Connection senderConnection(String host, int port) throws RTSPException { //factory method
    	try {
    		System.out.println("System initialized. Attempting connection to "+host+":"+port);
    		Connection result = new Connection(new Socket(host, port));//the connection itself
    		return result;
    		
    		//generic error handling
    	} catch(UnknownHostException e) {
    		 throw new RTSPException("Unknown Host");
    	} catch(IOException e) {
    		throw new RTSPException("IO connection error");
    	}
    }
    
    /**
     * Factory method that configures an InterMessage instance into the receiver state, where it awaits a connection on the designated port.
     * Once a connection is established, the ServerSocket is discarded and no new connections can be established on this port.
     *
     * @param port Port number used by the receiver
     * @throws RTSPException If an error happens while a connection is being established.
     */
    
    public static Connection receiverConnection(int port) throws RTSPException { //factory method
    	try {
    		ServerSocket served = new ServerSocket(port);
    		System.out.println("System initialized. Awaiting connection to port "+port);
    		Connection result = new Connection(served.accept());//the connection itself
    		served.close(); 		
    		return result;  		
    		
    		//generic error handling
    	} catch(IOException e) {
    		throw new RTSPException("IO connection error");
    	}
    }

    /**
     * Sends the final QUIT message, and closes the connection with the other client. This function
     * ignores any exception that may happen while sending the message, receiving its reply, or closing the connection.
     */
    public synchronized void close() {
    	
    	//write and send QUIT command
    	try {
    		ps.println("QUIT");    		
    		ps.close();
			socket.close();
    	} catch (IOException e) {
    		//ignoring exceptions
    	}
    }
    
    
    /**
     * Sends a String to the receiver.
     * 
     * @param message The String to be sent
     */
    public void sendString(String message) {
    	ps.println(message);
    	ps.println(".");
    	ps.flush();
    }
    
    
    /**
     * Reads a string that was received along a connection, and processes any special commands contained within it
     */
    public String[] readString() throws RTSPException {//change this to something proper
    	ArrayList<String>result = new ArrayList<>();
    	
    	String command;
    	try {
    		command = br.readLine();
    	} catch (IOException e) {
    		throw new RTSPException("Connection lost");
    	}
		
		if (command.equals("QUIT")){
			//send a close command
			close();
			throw new RTSPException("Other party has terminated session");
		}
		
		
		//Big WIP area
		if (command.length() < 4 || !command.substring(0, 4).equals("SAY ")) {
			System.out.println("Invalid message by sender, waiting for them to try again");
			bak(); //test this to make sure it doesn't break anything
			clear();
			return readString();
		}
		
		while (!command.equals(".")) {
			
			result.add(command);
			try {
	    		command = br.readLine();
	    	} catch (IOException e) {
	    		throw new RTSPException("IOException while reading");
	    	}
		}
		
		ack();//signals sender that message has been succesfully received
    	return result.toArray(new String[result.size()]);
    }
    
    public void clear() throws RTSPException {
    	try {
    		socket.setSoTimeout(200);
    		String query = br.readLine();
    		
    		for (int max = 20; max > 0; max--) {
    			if (query.equals("SRY")) {
    				socket.setSoTimeout(defaultTimeOut);
    				return;
    			}
    			query = br.readLine();
    		}
    		
    		throw new IOException();
    		
    		
    	} catch (IOException e) {
    		//if you ever want to handle this more cleanly, you may need to reset the Timeout
    		//socket.setSoTimeout(defaultTimeOut);
    		close();
    		throw new RTSPException("Failed to recover from malformed packet. Terminating connection.");
    	}
    }
    
    /**
     * Sends packet to confirm the receipt of a valid message
     */
    public void ack() {//send this upon successfully receiving valid message
    	ps.println("ACK");
    	ps.flush();
    }
    /**
     * Sends packet to confirm that client is ready to receive messages
     */    
    public void rak() {//send this upon successfully transitioning into the Receiver state
    	ps.println("RAK");
    	ps.flush();
    }
    /**
     * Sends packet to inform the sender that they violated protocol
     */        
    public void bak() {
    	ps.println("BAK");
    	ps.flush();
    }
    /**
     * Sends packet to acknowledge the BAK
     */  
    public void sry() {
    	ps.println("SRY");
    	ps.flush();
    }
    
    /**
     *  Pauses the program until it receives a RAK packet. Anything else implies a desync or connection problem, so the program will throw an exception
     */  
    public void waitR() throws RTSPException {
    	String query;
    	try {
    		query = br.readLine();
    		if (!query.equals("RAK")) throw new RTSPException("Receiver improperly acknowledged");
    	} catch (IOException i) {
    		throw new RTSPException("IO exception on Stream");
    	}
    }
    
    /**
     *  Pauses the program until it receives an ACK packet. Outputs true if there are no issues. False for non-fatal errors, and throws an exception for fatal ones
     */      
    public boolean waitA() throws RTSPException {
    	String query;
    	try {
    		query = br.readLine();
    		if (query.equals("BAK")) {
    			sry();
    			return false;
    		}
    		if (!query.equals("ACK")) throw new RTSPException("Improper acknowledgement");
    		return true;
    	} catch (IOException i) {
    		throw new RTSPException("IO exception on Stream");
    	}
    }

}
