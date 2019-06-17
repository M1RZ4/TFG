package main.java.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.jfree.chart.ChartPanel;

import dominio.Intervalo;
import main.java.gui.ApplicationWindow;
import main.java.logic.LanguageManager;
import main.java.logic.instances.ScheduledInstance;

/**
 * Clase TasksDialog que representa una diálogo para mostrar la información de
 * las tareas
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class TasksDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTable tasksTable;
	private JPanel buttonPanel;
	private JButton btnPlanificar;

	private ApplicationWindow app;
	private int tickUnit;
	private ScheduledInstance i;
	private int startTime;
	private int step = 0;
	private int[] avaliable;
	private List<Integer> tasks = new ArrayList<Integer>();

	public TasksDialog(ApplicationWindow app, int tickUnit) {
		this.app = app;
		i = new ScheduledInstance(app.getManager().getInstance());
		fillAvaliableCapacity();
		this.tickUnit = tickUnit;
		getContentPane().setBackground(Color.WHITE);
		setTitle(LanguageManager.getInstance().getTexts().getString("table_tasks"));
		setBounds(100, 100, 400, 300);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getScrollPane(), BorderLayout.CENTER);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		pack();
	}

	/**
	 * Método auxiliar que genera un vector con las capacidades disponibles en cada
	 * momento
	 */
	private void fillAvaliableCapacity() {
		avaliable = new int[(int) DoubleStream.of(app.getManager().getP()).sum()];
		List<Intervalo> list = i.getPerfilMaquina();
		int sum = (int) DoubleStream.of(app.getManager().getP()).sum();
		for (int j = 0; j < sum; j++) {
			for (Intervalo in : list)
				if (j >= in.getInicio() && j < in.getFin())
					avaliable[j] = in.getCap();
		}
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBackground(Color.DARK_GRAY);
			scrollPane.setViewportView(getTasksTable());
		}
		return scrollPane;
	}

	public JTable getTasksTable() {
		if (tasksTable == null) {
			tasksTable = new JTable(
					new DefaultTableModel(new Object[] { LanguageManager.getInstance().getTexts().getString("table_id"),
							LanguageManager.getInstance().getTexts().getString("table_duration"),
							LanguageManager.getInstance().getTexts().getString("table_due_date"),
							LanguageManager.getInstance().getTexts().getString("table_start_time") }, 0) {

						private static final long serialVersionUID = 1L;

						@Override
						public boolean isCellEditable(int row, int column) {
							return false;
						}

					});

			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			renderer.setBackground(Color.DARK_GRAY);
			renderer.setForeground(Color.WHITE);
			for (int i = 0; i < tasksTable.getColumnModel().getColumnCount(); i++) {
				tasksTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
			}

			JTableHeader header = tasksTable.getTableHeader();
			header.setDefaultRenderer(renderer);
			tasksTable.setFillsViewportHeight(true);
			tasksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		}
		return tasksTable;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.WHITE);
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			buttonPanel.add(getBtnPlanificar());
		}
		return buttonPanel;
	}

	public JButton getBtnPlanificar() {
		if (btnPlanificar == null) {
			btnPlanificar = new JButton(LanguageManager.getInstance().getTexts().getString("button_schedule"));
			btnPlanificar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scheduleTask();
				}
			});
		}
		return btnPlanificar;
	}

	/**
	 * Método auxiliar
	 */
	private void scheduleTask() {
		int row = tasksTable.getSelectedRow();
		if (row == -1)
			JOptionPane.showMessageDialog(null, LanguageManager.getInstance().getTexts().getString("error_task_table"),
					LanguageManager.getInstance().getTexts().getString("error_title"), JOptionPane.ERROR_MESSAGE);
		else if (tasks.contains((int) tasksTable.getValueAt(row, 0))) {
			JOptionPane.showMessageDialog(null, LanguageManager.getInstance().getTexts().getString("error_repeated"),
					LanguageManager.getInstance().getTexts().getString("error_title"), JOptionPane.ERROR_MESSAGE);
		} else {
			double duration = (double) tasksTable.getValueAt(row, 1);
			boolean less = false;
			// Calcular menor tiempo de inicio
			loop: for (int j = 0; j < avaliable.length; j++) {
				avaliable: if (avaliable[j] > 0 && j < startTime) {
					for (int k = j; k < j + duration; k++) {
						if (avaliable[k] == 0)
							break avaliable;
					}
					less = true;
				}
				if (less) {
					startTime = j;
					break loop;
				}
			}

			// Restar capacidad en instantes ocupados
			int endTime = (int) (startTime + duration);
			for (int k = startTime; k < endTime; k++)
				avaliable[k] -= 1;

			int id = (int) tasksTable.getValueAt(row, 0);
			tasks.add(id);
			double dueDate = (double) tasksTable.getValueAt(row, 2);

			// Añadir tiempo de inicio a la tabla
			DefaultTableModel model = (DefaultTableModel) tasksTable.getModel();
			model.setValueAt(startTime, row, 3);

			// Actualizar parámetros de la instancia
			i.setID(id);
			i.setP(duration);
			i.setD(dueDate);
			i.setSti(startTime);
			startTime += duration;

			// Actualizar gráfico principal
			app.getFrame().remove(app.getCp());
			app.getManager().setMainChart(step, i, tickUnit);
			app.setCp(new ChartPanel(app.getManager().getChart()));
			app.getFrame().getContentPane().add(app.getCp());
			app.getFrame().revalidate();

			// Incrementar step
			step++;

			// Actualiza el tardiness
			updateTardiness();

		}
	}

	/**
	 * Método auxiliar que actualiza el tardiness tras planificar acda tarea
	 */
	private void updateTardiness() {
		double tardiness = 0;
		int[] startTimes = i.getStartTimes();
		double[] dueDates = i.getD();
		double[] durations = i.getP();

		for (int j = 0; j < startTimes.length; j++)
			tardiness += Math.max(0, startTimes[j] + durations[j] - dueDates[j]);
		app.getTextFieldTardiness().setText(String.valueOf(tardiness));
	}

}
