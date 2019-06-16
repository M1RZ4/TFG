package main.java.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import main.java.gui.ApplicationWindow;
import main.java.logic.LanguageManager;
import main.java.logic.ScheduledInstance;

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
	private int sti;
	private int step = 0;

	public TasksDialog(ApplicationWindow app, int tickUnit) {
		this.app = app;
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

	private void scheduleTask() {
		int row = tasksTable.getSelectedRow();
		if (row == -1)
			JOptionPane.showMessageDialog(null, LanguageManager.getInstance().getTexts().getString("error_task_table"),
					LanguageManager.getInstance().getTexts().getString("error_title"), JOptionPane.ERROR_MESSAGE);
		else {
			i = new ScheduledInstance(app.getManager().getInstance());
			i.setID((int) tasksTable.getValueAt(row, 0));
			i.setD((double) tasksTable.getValueAt(row, 1));
			i.setP((double) tasksTable.getValueAt(row, 2));
			i.setSti(sti); // TODO añadir el valor de sti a la tabla igual que en application window
			sti += (double) tasksTable.getValueAt(row, 1);
			
			app.getFrame().remove(app.getCp());
			app.getManager().setMainChart(step, i, tickUnit);
			app.setCp(new ChartPanel(app.getManager().getChart()));
			app.getFrame().getContentPane().add(app.getCp());
			app.getFrame().revalidate();
			
			step++;
			
		}
	}

}
