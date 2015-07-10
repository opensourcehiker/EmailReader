package email.reader.thread;

import java.io.File;

import email.reader.gui.EmailGUI;

public class EmailThread implements Runnable{
	private File file = null;
	
	public EmailThread(File file) {
		this.file = file;
	}

	public void run() {
		new EmailGUI(file);
	}

}
