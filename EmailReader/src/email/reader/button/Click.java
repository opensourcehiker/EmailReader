package email.reader.button;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public abstract class Click extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public abstract void onLeftClick();
	public abstract void onRightClick();
	public abstract void onWheelClick();
	
	public Click(String text) {
		JLabel label = new JLabel(text,JLabel.CENTER);
		this.setBorder(new LineBorder(Color.BLACK,1,true));
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(80,30));
		this.add(label);
		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				switch (arg0.getButton()) {
				case 1:
					onLeftClick();
					break;
				case 2:
					onWheelClick();
					break;
				case 3:
					onRightClick();
				}
			}

			public void mouseEntered(MouseEvent arg0) {
				setBorder(new LineBorder(Color.BLACK,2,true));
			}

			public void mouseExited(MouseEvent arg0) {
				setBorder(new LineBorder(Color.BLACK,1,true));
			}

			public void mousePressed(MouseEvent arg0) {
				
			}

			public void mouseReleased(MouseEvent arg0) {
				
			}
			
		});
	}

}
