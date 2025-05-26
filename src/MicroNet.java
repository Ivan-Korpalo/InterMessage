import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class MicroNet {
	
	JFrame panel;
	
	//panels
	JPanel inputpanel;
	JPanel namepanel;
	JPanel addresspanel;
	JPanel portpanel;
	JPanel buttonpanel;
	
	//buttons
	JButton closure;
	JButton dismiss;
	JButton sender;
	JButton receiver;
	
	//labels
	JLabel namelabel;
	JLabel addresslabel;
	JLabel portlabel;
	
	//Fields
	JTextField nameinput;
	JTextField addressinput;
	JTextField portinput;
	
	//JLabel netlog;
	
	//I am missing the textfield that displays the log
	//JLabel logtext;	
	
	
	//constructor
	public MicroNet() {
		
		panel = new JFrame("Network info");
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		
		//panel.setSize(500,220);
		panel.setLayout(new BorderLayout());
		//panel.setLayout(new BoxLayout(panel.getContentPane(), BoxLayout.PAGE_AXIS));
		
		//panel.setLayout(new GridLayout(0,2,0,0));
		
		inputpanel = new JPanel(new GridLayout(0,2,0,0));
		
		//Text giving context to textbox that asks for user to input screen name
		namelabel = new JLabel("Nickname:");
		inputpanel.add(namelabel);
						
		//the aforementioned textbox
		nameinput = new JTextField();		
		inputpanel.add(nameinput);
		
		
		
		
		//Text giving context to textbox that asks for user to input address to connect to
		addresslabel = new JLabel("Address:");
		inputpanel.add(addresslabel);
				
								
		//the aforementioned textbox
		addressinput = new JTextField();	
		inputpanel.add(addressinput);
		
		
		
		
		//Text giving context to textbox that asks for user to input port to either connect to or host on
		portlabel = new JLabel("Port:");
		inputpanel.add(portlabel);
		
		
		//the aforementioned textbox
		portinput = new JTextField();	
		inputpanel.add(portinput);
		
		
		
		
		buttonpanel = new JPanel(new GridLayout(1,0,0,0));
		//Connects to a receiver located at specified address and port
		sender = new JButton("Connect");
						
		buttonpanel.add(sender);
		//buttonpanel.add(sender);			
						
		//Opens the device to receiving connections from senders at the specified port
		receiver = new JButton("Host");
		buttonpanel.add(receiver);
		
		
		//button functionality
		receiver.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			host(nameinput.getText(), Integer.parseInt(portinput.getText()));}
		});
		
		
		sender.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			connect(nameinput.getText(), addressinput.getText(), Integer.parseInt(portinput.getText()));}
		});
		
		
		
		
		panel.add(inputpanel, BorderLayout.CENTER);
		panel.add(buttonpanel, BorderLayout.SOUTH);
		
		
	}
	
	public void display() {
		panel.pack();
		panel.setVisible(true);
		
		
	}
	
	public void host(String name, int port) {
		TextPanel t;
		try {
			t = new TextPanel(name, port);
			t.display();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connect(String name, String host, int port) {
		TextPanel t;
		try {
			t = new TextPanel(name, host, port);
			t.display();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
