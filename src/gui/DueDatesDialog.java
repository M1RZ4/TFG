package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;

import logic.LanguageManager;

/**
 * Clase DueDatesDialog que representa una diálogo para mostrar la distribución
 * de duraciones de las tareas
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class DueDatesDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public DueDatesDialog() {
		getContentPane().setBackground(Color.WHITE);
		setTitle(LanguageManager.getInstance().getTexts().getString("chart_due_dates"));
		setBounds(100, 100, 400, 300);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}
}
