package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.JDialog;

public class DurationsDialog extends JDialog {

	private ResourceBundle texts;
	private static final long serialVersionUID = 1L;

	public DurationsDialog(ResourceBundle texts) {
		this.texts = texts;
		getContentPane().setBackground(Color.WHITE);
		setTitle(this.texts.getString("chart_durations"));
		setBounds(100, 100, 400, 300);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}
}
