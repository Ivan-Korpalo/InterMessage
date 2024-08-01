import java.util.Scanner;

import javax.swing.text.BadLocationException;

public class Main {
	static boolean running = true;
	static final String stop = "QUIT";
	static final String talk = "SAY ";
	
	
	
	public static void main(String[] args) {
		Netpanel.display();
		try {
			TextPanel.display();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	//Old method from console days. let's run the UI
	/**
	public static void main(String[] args) {
    	boolean sending = args[0].equalsIgnoreCase("true");
    	Connection link;
    	
    	//initialize connection
    	try {
    		if (sending) {//If we will send the first message
    			link = Connection.senderConnection( args[2], Integer.parseInt(args[1]));
    			link.waitR();
    			System.out.println("Connection established. Type a command and press enter.");
    			sending(link, new Scanner(System.in));
    			
    		} else { //if we will receive the first message
    			link = Connection.receiverConnection( Integer.parseInt(args[1]));
    			System.out.println("Connection established. Awaiting message from sender");
    			receiving(link, new Scanner(System.in));
    		}
    	} catch (RTSPException e) {
    			System.out.println(e);
    			System.out.println("Please restart the program");
    	}
	}
	**/
	
	public static void sending(Connection link, Scanner input) throws RTSPException {
		
		
		String command = input.nextLine();
			
			if (command.equals(stop)){
				//send a close command
				link.close();
				
				//then
				System.out.println("terminating");
				return;
			}
				
			if (command.length() < 4 || !command.substring(0, 4).equals(talk)) {
				System.out.println("Invalid message: must begin with either 'QUIT' or 'SAY '");
				System.out.println("Try Again");
				sending(link, input);
				return;
			}
			
		link.sendString(command);
		//await confirmation from receiver that command was obtained before changing state
		if (!link.waitA()) {
			System.out.println("Receiver claims message violated protocol. Try again");
			sending(link, input);
		} else {
		
		System.out.println("Message succesfully sent. Awaiting response");
		
		receiving(link, input);
		}
	}
	
	public static void receiving(Connection link, Scanner input) throws RTSPException {
		link.rak();
		
		String[] response = link.readString(); //returns a validated message
		
		for (int i = 0; i < response.length ; i++) {//prints validated message
			System.out.println(response[i]);
		}
		
		link.waitR();
		System.out.println("Message Received. You may now respond.");
		
		
		sending(link, input);
	}
}
