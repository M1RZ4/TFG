package logic;

import java.util.List;

import org.jfree.chart.JFreeChart;

import dominio.Instancia;
import logic.io.ChartWriter;
import logic.io.InstanceReader;
import logic.io.InstanceWriter;
import logic.io.Writer;

/**
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceManager {

	private InstanceReader reader = new InstanceReader();
	private Writer writer;
	private InstanceGenerator instanceGenerator = new InstanceGenerator();
	private ChartManager chartManager = new ChartManager();
	private Instancia instance;

	public void readInstance(String fileName) {
		instance = reader.readInstance(fileName);
	}

	public JFreeChart getChart() {
		return chartManager.getChart();
	}

	public double[] getDurations() {
		return instanceGenerator.getDurations();
	}

	public double[] getDueDates() {
		return instanceGenerator.getDueDates();
	}

	public List<Double> getIntervalDurations() {
		return instanceGenerator.getIntervalDurations();
	}

	public List<Integer> getIntervalCapacities() {
		return instanceGenerator.getIntervalCapacities();
	}

	public void writeInstance(String fileName) {
		writer = new InstanceWriter();
		((InstanceWriter) writer).initializeParameters(getDurations(), getDueDates(), getIntervalDurations(),
				getIntervalCapacities());
		writer.write(fileName);
	}

	public void writeChart(String fileName) {
		writer = new ChartWriter();
		((ChartWriter) writer).setChart(getChart());
		writer.write(fileName);
	}
}
