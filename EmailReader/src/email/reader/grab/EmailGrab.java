package email.reader.grab;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailGrab {
	private Folder f = null;
	private Session sesh = null;
	private Store store = null;
	
	public EmailGrab(String email, String pass) throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		sesh = Session.getInstance(props);
		store = sesh.getStore();
		store.connect("imap.gmail.com", email, pass);
		f = store.getFolder("INBOX");
		f.open(Folder.READ_ONLY);
		}
	
	public Folder getInbox() throws MessagingException {
		if (!f.isOpen()) {
			f.open(Folder.READ_ONLY);
		}
		return f;
	}

	public Session getSesh() {
		return sesh;
	}

	public void setSesh(Session sesh) {
		this.sesh = sesh;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
}
