package email.reader.splash;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import email.reader.button.Click;
import email.reader.thread.EmailThread;

public class EmailSplash extends JFrame{
	private static final long serialVersionUID = 1L;
	private BorderLayout main = null;
	private File f = null;

	public EmailSplash() {
		BufferedImage splash = null;
		try {
			splash = ImageIO.read(EmailSplash.class.getResource("Splash.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setUndecorated(true);
		this.setSize(410,290);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		main = new BorderLayout();
		this.setLayout(main);
		JPanel p = new JPanel();
		p.setBorder(new LineBorder(Color.BLACK,3,false));
		p.setPreferredSize(new Dimension(410,290));
		p.setBackground(Color.WHITE);
		SpringLayout sL = new SpringLayout();
		p.setLayout(sL);
		JLabel bc = new JLabel(new ImageIcon(splash));
		p.add(bc);
		sL.putConstraint(SpringLayout.HORIZONTAL_CENTER, bc, 0, SpringLayout.HORIZONTAL_CENTER, p);
		sL.putConstraint(SpringLayout.NORTH, bc, 3, SpringLayout.NORTH, p);
		JTextField field = new JTextField(25);
		field.setBackground(Color.WHITE);
		field.setForeground(Color.BLACK);
		field.setBorder(new LineBorder(Color.BLACK,1,false));
		field.setEditable(false);
		p.add(field);
		sL.putConstraint(SpringLayout.HORIZONTAL_CENTER, field, 40, SpringLayout.HORIZONTAL_CENTER, p);
		sL.putConstraint(SpringLayout.SOUTH, field, -50, SpringLayout.SOUTH, p);
		Click click = new Click("Browse") {

			private static final long serialVersionUID = 1L;

			public void onLeftClick() {
				this.browse();
			}

			public void onRightClick() {
				this.browse();
			}

			public void onWheelClick() {
				this.browse();
			}
			
			private void browse() {
				JFileChooser c = new JFileChooser();
				c.setApproveButtonText("Done");
				c.setDialogTitle("Select Attachment Directory");
				c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int var = c.showDialog(p, null);
				if (var == JFileChooser.APPROVE_OPTION) {
					f = c.getSelectedFile();
					field.setText(" " + f.getPath());
				}else if (var == JFileChooser.CANCEL_OPTION) {
					return;
				}
			}
			
		};
		p.add(click);
		sL.putConstraint(SpringLayout.WEST, click, 13, SpringLayout.WEST, p);
		sL.putConstraint(SpringLayout.SOUTH, click, -45, SpringLayout.SOUTH, p);
		JLabel text = new JLabel("Select a file to save attachments to");
		text.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				if (f != null) {
				switch (arg0.getButton()) {
				case 1:
					setVisible(false);
					new Thread(new EmailThread(getFile())).start();
				}
				}
			}

			public void mouseEntered(MouseEvent arg0) {
				if (f != null) {
				text.setFont(new Font("Plain",Font.BOLD,12));
				}
			}

			public void mouseExited(MouseEvent arg0) {
				if (f != null) {
				text.setFont(new Font("Plain",Font.ITALIC,12));
				}
			}

			public void mousePressed(MouseEvent arg0) {
				
			}

			public void mouseReleased(MouseEvent arg0) {
				
			}
			
		});
		text.setForeground(Color.BLACK);
		text.setFont(new Font("Plain",Font.ITALIC,12));
		p.add(text);
		sL.putConstraint(SpringLayout.HORIZONTAL_CENTER, text, 0, SpringLayout.HORIZONTAL_CENTER, p);
		sL.putConstraint(SpringLayout.SOUTH, text, -20, SpringLayout.SOUTH, p);
		this.add(p,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public File getFile() {
		return f;
	}
	
	public EmailSplash getInstance() {
		return this;
	}
	
	
}
