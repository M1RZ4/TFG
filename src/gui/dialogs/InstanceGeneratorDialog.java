package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import logic.LanguageManager;

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

	private double[] durations;
	private double[] dueDates;
	List<Double> intervalDurations;
	List<Integer> intervalCapacities;

	public InstanceGeneratorDialog() {
		setTitle(LanguageManager.getInstance().getTexts().getString("menu_instance_generator"));
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 250, 150);
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
					createTasks();
					createCapacity();
					save();
				}
			});
		}
		return btnGuardar;
	}

	private void createTasks() {
		durations = new double[(int) getTasksSpinner().getValue()];
		for (int i = 0; i < durations.length; i++)
			durations[i] = new Random().nextInt(100) + 1;
		dueDates = new double[durations.length];
		for (int i = 0; i < dueDates.length; i++) {
			double sum = Arrays.stream(durations).sum() / 2;
			dueDates[i] = new Random().nextInt((int) (Math.max(durations[i] + 2, sum) - durations[i] + 1))
					+ durations[i];
		}
	}

	private void createCapacity() {
		intervalDurations = new ArrayList<Double>();
		intervalCapacities = new ArrayList<Integer>();
		
		int maxCapacity = (int) getCapacitySpinner().getValue();
		int initialCapacity = new Random().nextInt(2) + 1;
		intervalCapacities.add(initialCapacity);
		int finalCapacity = new Random().nextInt(2) + 1;
		int maxInterval = 2 * maxCapacity;
		if (initialCapacity == finalCapacity && initialCapacity == 1)
			maxInterval -= 1;
		else if (initialCapacity == finalCapacity && initialCapacity == 2)
			maxInterval -= 3;
		else
			maxInterval -= 2;

		double sum = Arrays.stream(durations).sum();
		double initialDuration = new Random().nextInt((int) (sum / maxCapacity)) + 1;
		intervalDurations.add(initialDuration);
		double intervalSum = initialDuration;
		for (int i = 0; i < maxInterval - 2; i++) {
			double duration = new Random().nextInt((int) (sum / maxCapacity)) + 1;
			intervalDurations.add(duration);
			intervalSum += duration;
		}
		double finalDuration = 300000 - intervalSum;
		intervalDurations.add(finalDuration);

		for (int i = initialCapacity + 1; i < maxCapacity; i++)
			intervalCapacities.add(i);
		for (int i = maxCapacity; i > finalCapacity; i--)
			intervalCapacities.add(i);
		intervalCapacities.add(finalCapacity);
	}

	private void save() {
		JFileChooser jfc = new JFileChooser();
		int returnVal = jfc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			writeInstance(file.getAbsolutePath() + ".txt");
		}
	}

	private void writeInstance(String file) {
		try (FileWriter fw = new FileWriter(file)) {
			fw.write("NOP: " + durations.length + "\n");
			fw.write("NINT: " + intervalDurations.size() + "\n");
			int previous = 0;
			for (int i = 0; i < intervalDurations.size(); i++) {
				int sum = (int) (previous + intervalDurations.get(i));
				fw.write(previous + " " + sum + " " + intervalCapacities.get(i) + "\n");
				previous = sum;
			}
			for (int i = 0; i < durations.length; i++) {
				fw.write((i + 1) + " " + (int) durations[i] + " " + (int) dueDates[i]);
				if (i < durations.length - 1)
					fw.write("\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
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
			lblNmeroDeTareas.setBounds(10, 11, 90, 14);
			lblNmeroDeTareas.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblNmeroDeTareas;
	}

	private JSpinner getTasksSpinner() {
		if (tasksSpinner == null) {
			tasksSpinner = new JSpinner();
			tasksSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			tasksSpinner.setBounds(110, 8, 114, 20);
		}
		return tasksSpinner;
	}

	private JLabel getLblCapacidadMxima() {
		if (lblCapacidadMxima == null) {
			lblCapacidadMxima = new JLabel(LanguageManager.getInstance().getTexts().getString("label_max_capacity"));
			lblCapacidadMxima.setBounds(10, 46, 93, 14);
			lblCapacidadMxima.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblCapacidadMxima;
	}

	private JSpinner getCapacitySpinner() {
		if (capacitySpinner == null) {
			capacitySpinner = new JSpinner();
			capacitySpinner.setModel(new SpinnerNumberModel(new Integer(3), new Integer(3), null, new Integer(1)));
			capacitySpinner.setBounds(110, 43, 114, 20);
		}
		return capacitySpinner;
	}
}
