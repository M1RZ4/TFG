package main.java.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;

import main.java.logic.LanguageManager;

/**
 * Clase DurationsDialog que representa una diálogo para mostrar la distribución
 * de duraciones de las tareas
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class DurationsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public DurationsDialog() {
		getContentPane().setBackground(Color.WHITE);
		setTitle(LanguageManager.getInstance().getTexts().getString("chart_durations"));
		setBounds(100, 100, 400, 300);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}
}
