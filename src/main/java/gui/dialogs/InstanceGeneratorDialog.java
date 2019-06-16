package main.java.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.java.gui.ApplicationWindow;
import main.java.logic.LanguageManager;

/**
 * Clase InstanceGeneratorDialog que representa una diálogo para interactuar con
 * la herramienta de generación de instancias
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceGeneratorDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel configPanel;
	private JPanel buttonPanel;
	private JButton btnGuardar;
	private JButton btnCancelar;
	private JLabel lblNmeroDeTareas;
	private JSpinner tasksSpinner;
	private JLabel lblCapacidadMxima;
	private JSpinner capacitySpinner;
	private JTextField textField;
	private JLabel lblIntervalos;

	private ApplicationWindow app;

	public InstanceGeneratorDialog(ApplicationWindow app) {
		this.app = app;
		setTitle(LanguageManager.getInstance().getTexts().getString("menu_instance_generator"));
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 315, 175);
		getContentPane().add(getConfigPanel(), BorderLayout.CENTER);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
	}

	private JPanel getConfigPanel() {
		if (configPanel == null) {
			configPanel = new JPanel();
			configPanel.setBackground(Color.WHITE);
			configPanel.setLayout(null);
			configPanel.add(getLblNmeroDeTareas());
			configPanel.add(getTasksSpinner());
			configPanel.add(getLblCapacidadMxima());
			configPanel.add(getCapacitySpinner());
			configPanel.add(getTextField());
			configPanel.add(getLblIntervalos());
		}
		return configPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.DARK_GRAY);
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			buttonPanel.add(getBtnGuardar());
			buttonPanel.add(getBtnCancelar());
		}
		return buttonPanel;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton(LanguageManager.getInstance().getTexts().getString("button_save"));
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateIntervals();
					save();
				}
			});
		}
		return btnGuardar;
	}

	/**
	 * Método auxiliar que actualiza los valores de los parámetros del generador de
	 * instancias en función de lo introducido por el usuario
	 */
	private void updateIntervals() {
		app.getManager().initializeInstanceGenerator((int) tasksSpinner.getValue(), (int) capacitySpinner.getValue());
		app.getManager().createTasks();
		app.getManager().createIntervals();
		textField.setText(String.valueOf(app.getManager().getMaxInterval()));
	}

	/**
	 * Método auxiliar que guarda la instancia generada en un fichero de texto
	 */
	private void save() {
		JFileChooser jfc = new JFileChooser();
		int returnVal = jfc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			app.getManager().writeInstance(file.getAbsolutePath() + ".txt");
			JOptionPane.showMessageDialog(null,
					LanguageManager.getInstance().getTexts().getString("message_instance_generator"),
					LanguageManager.getInstance().getTexts().getString("message_title"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton(LanguageManager.getInstance().getTexts().getString("button_cancel"));
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private JLabel getLblNmeroDeTareas() {
		if (lblNmeroDeTareas == null) {
			lblNmeroDeTareas = new JLabel(LanguageManager.getInstance().getTexts().getString("label_number_of_tasks"));
			lblNmeroDeTareas.setBounds(10, 10, 165, 14);
			lblNmeroDeTareas.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblNmeroDeTareas;
	}

	private JSpinner getTasksSpinner() {
		if (tasksSpinner == null) {
			tasksSpinner = new JSpinner();
			tasksSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			tasksSpinner.setBounds(185, 10, 114, 20);
			tasksSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateIntervals();
				}
			});
		}
		return tasksSpinner;
	}

	private JLabel getLblCapacidadMxima() {
		if (lblCapacidadMxima == null) {
			lblCapacidadMxima = new JLabel(LanguageManager.getInstance().getTexts().getString("label_max_capacity"));
			lblCapacidadMxima.setBounds(10, 45, 165, 14);
			lblCapacidadMxima.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblCapacidadMxima;
	}

	private JSpinner getCapacitySpinner() {
		if (capacitySpinner == null) {
			capacitySpinner = new JSpinner();
			capacitySpinner.setModel(new SpinnerNumberModel(new Integer(3), new Integer(3), null, new Integer(1)));
			capacitySpinner.setBounds(185, 45, 114, 20);
			capacitySpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateIntervals();
				}
			});
		}
		return capacitySpinner;
	}

	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setText("0");
			textField.setHorizontalAlignment(SwingConstants.CENTER);
			textField.setEditable(false);
			textField.setBounds(185, 80, 114, 20);
			textField.setColumns(10);
		}
		return textField;
	}

	private JLabel getLblIntervalos() {
		if (lblIntervalos == null) {
			lblIntervalos = new JLabel(LanguageManager.getInstance().getTexts().getString("label_intervals"));
			lblIntervalos.setHorizontalAlignment(SwingConstants.CENTER);
			lblIntervalos.setBounds(10, 80, 165, 14);
		}
		return lblIntervalos;
	}
}
