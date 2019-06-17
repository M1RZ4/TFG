package main.java.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import main.java.gui.ApplicationWindow;
import main.java.logic.Analysis;
import main.java.logic.LanguageManager;

/**
 * Clase ExperimentalAnalysisDialog que representa una diálogo para interactuar
 * con la herramienta de análisis experimental
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class ExperimentalAnalysisDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel configPanel;
	private JPanel buttonPanel;
	private JButton btnGuardar;
	private JButton btnCancelar;
	private JLabel lblNmeroDeTareas;
	private JSpinner tasksSpinner;
	private JLabel lblCapacidadMxima;
	private JSpinner capacitySpinner;
	private JSpinner instanceSpinner;
	private JLabel lblIntervalos;
	private JButton btnCargar;

	private ApplicationWindow app;

	public ExperimentalAnalysisDialog(ApplicationWindow app) {
		this.app = app;
		setTitle(LanguageManager.getInstance().getTexts().getString("menu_experimental_study"));
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
			configPanel.add(getLblIntervalos());
			configPanel.add(getInstancesSpinner());
		}
		return configPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.DARK_GRAY);
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			buttonPanel.add(getBtnCargar());
			buttonPanel.add(getBtnGuardar());
			buttonPanel.add(getBtnCancelar());
		}
		return buttonPanel;
	}

	private JButton getBtnCargar() {
		if (btnCargar == null) {
			btnCargar = new JButton(LanguageManager.getInstance().getTexts().getString("button_load"));
			btnCargar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int error = load();
					if (error == 0)
						save();
				}
			});
		}
		return btnCargar;
	}

	/**
	 * Método auxiliar que carga un análisis desde un fichero de texto para realizar
	 * el estudio experimental
	 * 
	 * @return -1 si hay un error, 0 en caso contrario
	 */
	private int load() {
		JFileChooser jfc = new JFileChooser();
		int returnVal = jfc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			try {
				app.getManager().readAnalysis(file.getAbsolutePath());
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), app.getManager().getText("error_title"),
						JOptionPane.ERROR_MESSAGE);
				return -1;
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, app.getManager().getText("error_not_found"),
						app.getManager().getText("error_title"), JOptionPane.ERROR_MESSAGE);
				return -1;
			} catch (IllegalStateException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), app.getManager().getText("error_title"),
						JOptionPane.ERROR_MESSAGE);
				return -1;
			}
			JOptionPane.showMessageDialog(this, app.getManager().getText("message_load"),
					app.getManager().getText("message_title"), JOptionPane.INFORMATION_MESSAGE);
			return 0;
		}
		return -1;
	}

	/**
	 * Método auxiliar que guarda los resultados del análisis experimental en un
	 * fichero excel
	 */
	private void save() {
		JFileChooser jfc = new JFileChooser();
		int returnVal = jfc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			app.getManager().initializeInstanceGenerator(0, 0);
			try {
				app.getManager().writeAnalysis((file.getAbsolutePath()));
			} catch (IllegalStateException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), app.getManager().getText("error_title"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this, app.getManager().getText("message_save"),
					app.getManager().getText("message_title"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton(LanguageManager.getInstance().getTexts().getString("button_save"));
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					app.getManager()
							.setAnalysis(new Analysis((int) instanceSpinner.getValue(),
									new int[] { (int) tasksSpinner.getValue() },
									new int[] { (int) capacitySpinner.getValue() }));
					save();
				}
			});
		}
		return btnGuardar;
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
		}
		return capacitySpinner;
	}

	private JSpinner getInstancesSpinner() {
		if (instanceSpinner == null) {
			instanceSpinner = new JSpinner();
			instanceSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			instanceSpinner.setBounds(185, 80, 114, 20);
		}
		return instanceSpinner;
	}

	private JLabel getLblIntervalos() {
		if (lblIntervalos == null) {
			lblIntervalos = new JLabel(LanguageManager.getInstance().getTexts().getString("label_number_of_instances"));
			lblIntervalos.setHorizontalAlignment(SwingConstants.CENTER);
			lblIntervalos.setBounds(10, 80, 165, 14);
		}
		return lblIntervalos;
	}

}
