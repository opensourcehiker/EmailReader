package email.reader.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.mail.MessagingException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import email.reader.grab.EmailGrab;

public abstract class UP extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel uL = new JLabel("Email");
	private JLabel pL = new JLabel("Password");
	private JTextField uT = new JTextField(15);
	private JTextField pT = new JTextField(15);
	private JLabel title = new JLabel(" Login ");
	
	public UP() {
		this.setPreferredSize(new Dimension(250,200));
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.BLACK,1,true));
		TextFieldListener listen = new TextFieldListener(uT) {

			public void actionPerformed(ActionEvent arg0) {
				if (uT.getText() != null && pT.getText() != null) {
					try {
						EmailGrab grab = new EmailGrab(uT.getText(), pT.getText());
						onLogin(grab);
						EmailGUI.out.info("Successfully connected.");
						uT.setText("");
						pT.setText("");
					}catch (MessagingException e) {
						uT.setText("");
						pT.setText("");
						EmailGUI.out.error("Connection failed.");
						e.printStackTrace();
					}
				}
			}
			
		};
		uT.addActionListener(listen);
		pT.addActionListener(listen);
		title.setBorder(new LineBorder(Color.LIGHT_GRAY,1,false));
		title.setFont(new Font("Plain",Font.PLAIN,18));
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		this.add(uT);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, uT,-30,SpringLayout.VERTICAL_CENTER,this);
		layout.putConstraint(SpringLayout.WEST, uT,65,SpringLayout.WEST,this);
		this.add(pT);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, pT,30,SpringLayout.VERTICAL_CENTER,this);
		layout.putConstraint(SpringLayout.WEST, pT,65,SpringLayout.WEST,this);
		this.add(uL);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER,uL,-30,SpringLayout.VERTICAL_CENTER,this);
		layout.putConstraint(SpringLayout.WEST, uL,32,SpringLayout.WEST,this);
		this.add(pL);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER,pL,30,SpringLayout.VERTICAL_CENTER,this);
		layout.putConstraint(SpringLayout.WEST, pL,5,SpringLayout.WEST,this);
		this.add(title);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER,title,-75,SpringLayout.VERTICAL_CENTER,this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,title,0,SpringLayout.HORIZONTAL_CENTER,this);
	}

	public JLabel getEmailLabel() {
		return uL;
	}

	public void setEmailLabel(JLabel uL) {
		this.uL = uL;
	}

	public JLabel getPasswordLabel() {
		return pL;
	}

	public void setPasswordLabel(JLabel pL) {
		this.pL = pL;
	}
	
	public JTextField getPasswordField() {
		return pT;
	}

	public void setPasswordField(JTextField pT) {
		this.pT = pT;
	}
	
	public JTextField getEmailField() {
		return uT;
	}
	
	public void setEmailField(JTextField uT) {
		this.uT = uT;
	}
	
	public abstract void onLogin(EmailGrab grab);

}
