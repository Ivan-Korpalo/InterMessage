import java.awt.BorderLayout;

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
	
	static public void display() throws BadLocationException{
		
		//Frame info
		JFrame frame = new JFrame("Chat info");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		frame.setSize(500,220);
		
		//Message display
		JTextPane messagelog = new JTextPane();
				
		SimpleAttributeSet boldSet = new SimpleAttributeSet();
		StyleConstants.setBold(boldSet, true);
		SimpleAttributeSet basicSet = new SimpleAttributeSet();
		
		//initialization
		messagelog.setCharacterAttributes(boldSet, true);
		messagelog.setText("Username here: ");
		
		//modification
		Document messages = messagelog.getStyledDocument();
		messages.insertString(messages.getLength(), "Line 1", basicSet);
		messages.insertString(messages.getLength(), "\nLine 2", basicSet);
		
		
		//Wrap it in a Scroll pane
		JScrollPane scrollPane = new JScrollPane(messagelog);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		frame.add(scrollPane, BorderLayout.CENTER);
	
		//Make dialogue panel
		JPanel messenger = new JPanel(new BorderLayout());
		JButton send = new JButton("Say");
		JTextField message = new JTextField();
		
		messenger.add(send, BorderLayout.WEST);
		messenger.add(message, BorderLayout.CENTER);
		frame.add(messenger, BorderLayout.SOUTH);

		
		frame.setVisible(true);
	}

}
