import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextPanel {
	
	private Connection session;
	
	String username = "MYSELF";
	String othername = "THEM";
	String sysname = "CONSOLE";
	
	JFrame frame;
	JTextPane messagelog;
	SimpleAttributeSet boldSet;
	SimpleAttributeSet basicSet;
	
	Document messages;
	JScrollPane scrollPane;
	
	JPanel messenger;
	JButton send;
	JTextField message;
	
	private TextPanel() throws BadLocationException {

		//Frame info
		frame = new JFrame("Chat info");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		frame.setSize(500,220);
		
		//Message display
		messagelog = new JTextPane();
				
		boldSet = new SimpleAttributeSet();
		StyleConstants.setBold(boldSet, true);
		basicSet = new SimpleAttributeSet();
		
		//modification
		messages = messagelog.getStyledDocument();
		
		
		//Wrap it in a Scroll pane
		scrollPane = new JScrollPane(messagelog);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		frame.add(scrollPane, BorderLayout.CENTER);
	
		//Make dialogue panel
		messenger = new JPanel(new BorderLayout());
		send = new JButton("Say");
		
		
		message = new JTextField();
		
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					send(username, message.getText());
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }  
	    });
		
		messenger.add(send, BorderLayout.WEST);
		messenger.add(message, BorderLayout.CENTER);
		frame.add(messenger, BorderLayout.SOUTH);
		
		
		//frame.setVisible(true);
	}	
	
	private TextPanel(String username) throws BadLocationException {
		this();
		this.username = username;
	}
	
	
	public TextPanel(String name, int port) throws BadLocationException {//use this when hosting a session
		this(name);
		send.setText("Wait");
		send.setEnabled(false);
		
		
		frame.setVisible(true);
		try {
			write(sysname, "Hosting on port: "+port+". Awaiting connection");
		} catch (BadLocationException e) {
	    	//To-do proper error handling
	    } 
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						session = Connection.receiverConnection(port);
						session.rak();
						
						String introduction = session.readString()[0]; //any other junk is simply ignored
						//System.out.println("received :"+introduction);
						
						if (introduction.substring(0,4).equals("ELO ")){
							othername = introduction.substring(4);
						} else {
							throw new RTSPException("Introduction failed. Aborting Connection. Please close window");
						}
						
						session.sendString("ELO "+username);
						session.waitA();
						write(sysname, "Connection established. Awaiting message from sender");
						
						//newly added
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								try {
									try {
										String[] response = session.readString(); //returns a validated message
										write(othername, strip(response[0]));
										for (int i = 1; i < response.length ; i++) {//prints validated message
											write(othername, response[i]);
										}
									
										session.waitR();
										send.setText("Say");
										send.setEnabled(true);
									} catch (RTSPException e) {
									write(sysname, e.getMessage());
									}
								} catch (BadLocationException e) {
									//error handling
								}
							}
						});
						
					} catch (RTSPException e) {
						write(sysname, e.getMessage());// maybe do more here?
					}
					
					
				} catch (BadLocationException e) {
						//To-do proper error handling
					}
				}
			});
	    		
	   
	    
	};
	
	public TextPanel(String name, String host, int port) throws BadLocationException {//use this when connecting to a host
		this(name);
		send.setText("Connecting...");
		send.setEnabled(false);
		
		
		frame.setVisible(true);
		try {
			write(sysname, "Connecting to "+host+" on port:"+port+". Awaiting connection");
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						try {
							session = Connection.senderConnection(host, port);
			    			session.waitR();
			    			session.sendString("ELO "+username);
			    			session.waitA();
			    			
			    			String introduction = session.readString()[0]; //any other junk is simply ignored
							
							if (introduction.substring(0,4).equals("ELO ")){
								othername = introduction.substring(4);
							} else {
								throw new RTSPException("Introduction failed. Aborting Connection. Please close window");
							}
			    			
			    			write(sysname, "Connection established. Please send a message");
							send.setText("Say");
							send.setEnabled(true);
						} catch (RTSPException e) {
							write(sysname, e.getMessage());
						}
						
						
						//enable buttons.
					} catch (BadLocationException e) {
						//To-do proper error handling
					}
					
			    	//receiving(session, new Scanner(System.in));
				}
			});
	    		
	    } catch (BadLocationException e) {
	    	//To-do proper error handling
	    } //finally {
	    
		
	    //}
	};
	
	public void display() throws BadLocationException{
		frame.setVisible(true);
	}
	
	/*
	public void write(String s) throws BadLocationException {
		messages.insertString(messages.getLength(), "\n"+s, basicSet);		
	}*/
	
	public void write(String username, String content) throws BadLocationException {
		messages.insertString(messages.getLength(), username+": ", boldSet);
		messages.insertString(messages.getLength(), content+"\n", basicSet);	
	}
	
	
	public void send(String username, String content) throws BadLocationException {
		//String message = username +"\n"+ content;
		send.setText("Wait");
		send.setEnabled(false);
		
		write(username, content);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					session.sendString("SAY "+content);
					session.waitA();
					session.rak();
					
					String[] response = session.readString(); //returns a validated message
					
					try {
						write(othername, strip(response[0]));
						for (int i = 1; i < response.length ; i++) {//prints validated message
							write(othername, response[i]);
						}
					} catch (BadLocationException e) {
						//error handling
					}
					
					session.waitR();
					send.setText("Say");
					send.setEnabled(true);
					
					//System.out.println("Message Received. You may now respond.");
					
				} catch (RTSPException e) {
				//handle the exception properly
				}
			}
		});
	}
	
	public String strip(String query) {
		return query.substring(4);
	}

}
