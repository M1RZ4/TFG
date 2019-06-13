package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;

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
	private JPanel spinnerPanel;
	private JSpinner markSpinner;
	private JFreeChart chart;

	public DueDatesDialog(JFreeChart chart) {
		this.chart = chart;
		getContentPane().setBackground(Color.WHITE);
		setTitle(LanguageManager.getInstance().getTexts().getString("chart_due_dates"));
		setBounds(100, 100, 400, 300);
		setResizable(true);
		getContentPane().setLayout(new BorderLayout(0, 5));
		getContentPane().add(getSpinnerPanel(), BorderLayout.SOUTH);
	}

	private JPanel getSpinnerPanel() {
		if (spinnerPanel == null) {
			spinnerPanel = new JPanel();
			spinnerPanel.add(getMarkSpinner());
		}
		return spinnerPanel;
	}

	private JSpinner getMarkSpinner() {
		if (markSpinner == null) {
			markSpinner = new JSpinner();
			markSpinner.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
			Component editor = markSpinner.getEditor();
			JFormattedTextField text = ((DefaultEditor) editor).getTextField();
			text.setColumns(4);
			markSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					XYPlot plot = chart.getXYPlot();
					NumberAxis range = (NumberAxis) plot.getRangeAxis();
					range.setTickUnit(new NumberTickUnit((int) markSpinner.getValue()));
					revalidate();
				}
			});
		}
		return markSpinner;
	}

}
