import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Netpanel {
	
	static public void display() {
		int ynetworkoffset = 0;
		
		
		JFrame panel = new JFrame("Network info");
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		
		panel.setSize(500,220);
		
		//closes the network connection and the program
		JButton closure = new JButton("QUIT");
		
		closure.setBounds(380,20+ynetworkoffset,70, 40);
		panel.add(closure);
		panel.setLayout(null);
		
		
		//dismisses error messages outputted by the log
		JButton dismiss = new JButton("Dismiss");
		dismiss.setBounds(380, 145+ynetworkoffset, 95, 30);
		panel.add(dismiss);
		
		
		//Connects to a receiver located at specified address and port
		JButton sender = new JButton("Connect");
		
		sender.setBounds(20, 80+ynetworkoffset, 100, 30);
		panel.add(sender);
		
		
		//Opens the device to receiving connections from senders at the specified port
		JButton receiver = new JButton("Host");
		receiver.setBounds(140, 80+ynetworkoffset, 100, 30);
		panel.add(receiver);
		
		
		//Text giving context to textbox that asks for user to input address to connect to
		JLabel address = new JLabel("Address:");
		address.setBounds(20,115+ynetworkoffset, 55,20);
		panel.add(address);
				
		//the aforementioned textbox
		JTextField addaddress = new JTextField("Type here");
		addaddress.setBounds(90, 115+ynetworkoffset, 151, 20);
		panel.add(addaddress);
		
		
		
		
		//Text giving context to textbox that asks for user to input port to either connect to or host on
		JLabel port = new JLabel("Port:");
		port.setBounds(280,115+ynetworkoffset, 100,20);
		panel.add(port);
		//Textbox not yet implemented
		
		//the aforementioned textbox
		JTextField addport = new JTextField("Type here");
		addport.setBounds(320, 115+ynetworkoffset, 151, 20);//tweak dimensions later
		panel.add(addport);
		
		
		
		//Text giving context to dialog that displays certain system specific messages (such as errors)
		JLabel netlog = new JLabel("log:");
		netlog.setBounds(20,145+ynetworkoffset, 55,20);
		panel.add(netlog);
		//Dialogue box not yet implemented
		
		
		
		panel.setVisible(true);
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	

}
