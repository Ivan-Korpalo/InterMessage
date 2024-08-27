import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextPanel {
	
	String username = "PLACEHOLDERUSER";
	
	JFrame frame;
	JTextPane messagelog;
	SimpleAttributeSet boldSet;
	SimpleAttributeSet basicSet;
	
	Document messages;
	JScrollPane scrollPane;
	
	JPanel messenger;
	JButton send;
	JTextField message;
	
	public TextPanel() throws BadLocationException {

		//Frame info
		frame = new JFrame("Chat info");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		frame.setSize(500,220);
		
		//Message display
		messagelog = new JTextPane();
				
		boldSet = new SimpleAttributeSet();
		StyleConstants.setBold(boldSet, true);
		basicSet = new SimpleAttributeSet();
		
		//initialization
		messagelog.setCharacterAttributes(boldSet, true);
		messagelog.setText("Username here: ");
		
		//modification
		messages = messagelog.getStyledDocument();
		messages.insertString(messages.getLength(), "Line 1", basicSet);
		messages.insertString(messages.getLength(), "\nLine 2", basicSet);
		
		
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
					write(username, message.getText());
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
	
	public void display() throws BadLocationException{
		frame.setVisible(true);
	}
	
	/*
	public void write(String s) throws BadLocationException {
		messages.insertString(messages.getLength(), "\n"+s, basicSet);		
	}*/
	
	public void write(String username, String content) throws BadLocationException {
		messages.insertString(messages.getLength(), "\n"+username+": ", boldSet);
		messages.insertString(messages.getLength(), content, basicSet);	
	}

}
