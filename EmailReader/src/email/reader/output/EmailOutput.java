package email.reader.output;


import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public class EmailOutput extends JTextArea{
	private static final long serialVersionUID = 1L;
	
	public EmailOutput() {
		this.setEditable(false);
		this.setBackground(Color.WHITE);
		this.setForeground(Color.BLACK);
		this.setVisible(true);
	}
	
	public void write(String msg) {
		this.append(msg + "\n");
	}

	public void error(String msg) {
		SimpleDateFormat format = new SimpleDateFormat("[hh:mm:ss]");
		this.write(format.format(new Date()) + " ERROR: " + msg);
	}
	
	public void info(String msg) {
		SimpleDateFormat format = new SimpleDateFormat("[hh:mm:ss]");
		this.write(format.format(new Date()) + " INFO: " + msg);
	}
	
	public void alert(String msg) {
		SimpleDateFormat format = new SimpleDateFormat("[hh:mm:ss]");
		this.write(format.format(new Date()) + " ALERT: " + msg);
	}
	

	
	

}
