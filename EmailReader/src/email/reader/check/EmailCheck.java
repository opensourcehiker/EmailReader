package email.reader.check;

import java.io.File;
import java.io.IOException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import email.reader.grab.EmailGrab;
import email.reader.gui.EmailGUI;
import email.reader.run.Run;

public class EmailCheck {
	private EmailGrab grab = null;
	private int previous = 0;
	
	public EmailCheck(String user,String pass) {
			try {
				grab = new EmailGrab(user,pass);
			Folder first = grab.getInbox();
			previous = first.getMessageCount();
			} catch (MessagingException e1) {
				EmailGUI.out.error("Failed to check email.");
				e1.printStackTrace();
			}
			new Run() {
				
				public void run() {
				try {
					Folder inbox = grab.getInbox();
				if (previous < inbox.getMessageCount()) {
					Message m = inbox.getMessage(inbox.getMessageCount());
					if (m.getFrom()[0].toString().contains(EmailGUI.gui.getEmail())) {
					String sub = EmailGUI.gui.decrypt(m.getSubject());
					if (sub.startsWith("SEND")) {
						String uuid = sub.split("AbC")[1];
						if (!EmailGUI.gui.getUUID().toString().replaceAll("-", "").equals(uuid)) {
							File f = EmailGUI.gui.saveFile(m);
							EmailGUI.out.alert("A file [" + f.getName() + "] has been received!");
					}
					}else if (sub.startsWith("GRAB")) {
						String uuid = sub.split("AbC")[1];
						if (!EmailGUI.gui.getUUID().toString().replaceAll("-", "").equals(uuid)) {
							String text = EmailGUI.gui.decrypt(EmailGUI.gui.getText((MimeMultipart) m.getContent()));
							EmailGUI.out.alert("Received request to grab [" + text + "] file..");
								File f = new File(text);
								if (!f.exists()) {
								EmailGUI.out.error("File [" + text + "] nonexistent.");
								EmailGUI.gui.sendMessage("ERROR", "NOSUCHFILE" + text);
								}else{
							EmailGUI.out.info("Grabbed [" + f.getName() + "] file.");
							EmailGUI.gui.sendFile(f);
					}
						}
					}else if (sub.startsWith("ERROR")) {
						String uuid = sub.split("AbC")[1];
						if (!EmailGUI.gui.getUUID().toString().replaceAll("-", "").equals(uuid)) {
							String text = EmailGUI.gui.decrypt(EmailGUI.gui.getText((MimeMultipart) m.getContent()));
							if (text.startsWith("NOSUCHFILE")) {
								String file = text.substring(10, text.length());
								EmailGUI.out.error("File [" + file + "] is nonexistent.");
							}else if (text.startsWith("COMMANDFAIL")) {
								String cmd = text.substring(11,text.length());
								EmailGUI.out.error("Command [" + cmd + "] is nonexistent.");
							}
						}
					}else if (sub.startsWith("COMMAND")) {
						String uuid = sub.split("AbC")[1];
						if (!EmailGUI.gui.getUUID().toString().replaceAll("-", "").equals(uuid)) {
							String text = EmailGUI.gui.decrypt(EmailGUI.gui.getText((MimeMultipart) m.getContent()));
							EmailGUI.out.info("Attemping to run [" + text + "] command..");
							EmailGUI.gui.sendMessage("WORKCOMMAND", text);
							Runtime.getRuntime().exec("cmd /c " + text);
						}
					}else if (sub.startsWith("WORKCOMMAND" )) {
						String uuid = sub.split("AbC")[1];
						if (!EmailGUI.gui.getUUID().toString().replaceAll("-", "").equals(uuid)) {
							String text = EmailGUI.gui.decrypt(EmailGUI.gui.getText((MimeMultipart) m.getContent()));
							EmailGUI.out.info("Command [" + text + "] request acknowledged.");
						}
					}
				}
				}
				previous = inbox.getMessageCount();
				} catch (MessagingException | IOException e) {
					EmailGUI.out.error("Disconnected from email.");
					this.cancel();
				}
			}
			}.runRepeated(1000, 1000);
	}

	public EmailGrab getEmailGrab() {
		return grab;
	}

	public void setEmailGrab(EmailGrab grab) {
		this.grab = grab;
	}

}
