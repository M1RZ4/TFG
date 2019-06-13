package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logic.LanguageManager;

public class TasksDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTable tasksTable;

	public TasksDialog() {
		getContentPane().setBackground(Color.WHITE);
		setTitle(LanguageManager.getInstance().getTexts().getString("table_tasks"));
		setBounds(100, 100, 400, 300);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getScrollPane(), BorderLayout.CENTER);
		pack();
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBackground(Color.WHITE);
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

}
