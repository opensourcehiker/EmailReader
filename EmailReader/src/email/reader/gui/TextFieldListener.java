package email.reader.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public abstract class TextFieldListener implements ActionListener{
	private JTextField field = new JTextField();
	
	public TextFieldListener(JTextField field) {
		this.setField(field);
	}

	public abstract void actionPerformed(ActionEvent arg0);

	public JTextField getField() {
		return field;
	}

	public void setField(JTextField field) {
		this.field = field;
	}

}
