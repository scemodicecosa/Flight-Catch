package flightcatch;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Log {
	private JTextPane console;
	
	public Log(JTextPane console){
		this.console = console;
	}
	public void logConsole(String text) throws BadLocationException{
		appendString(text);
		
	}
	public void clearConsole(){
		console.setText("");
	}
	public void appendString(String str) throws BadLocationException
	{
		Style style = console.addStyle("New_Style", null);
		StyleConstants.setForeground(style, Color.WHITE);
//		StyleConstants.setBackground(style, Color.BLACK);
		StyledDocument document = (StyledDocument) console.getDocument();
		document.insertString(0, str+'\n', style);
		console.update(console.getGraphics());
		// ^ or your style attribute 
		
	}
	public JTextPane getConsole(){
		return console;
	}
}
