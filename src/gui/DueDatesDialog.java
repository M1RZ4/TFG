package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;

public class DueDatesDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public DueDatesDialog() {
		getContentPane().setBackground(Color.WHITE);
		setTitle("Fecha de vencimiento");
		setBounds(100, 100, 400, 300);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}
}
