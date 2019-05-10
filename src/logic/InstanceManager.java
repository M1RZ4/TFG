package logic;

import java.util.List;
import java.util.ResourceBundle;

import org.jfree.chart.JFreeChart;

import dominio.Instancia;
import gui.enums.Rule;
import logic.io.ChartWriter;
import logic.io.InstanceReader;
import logic.io.InstanceWriter;
import logic.io.Writer;

/**
 * Clase InstanceManager
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceManager {

	private InstanceReader reader = new InstanceReader();
	private Writer writer;
	private InstanceGenerator instanceGenerator;
	private ChartManager chartManager; // TODO inicializar
	private Instancia instance;

	// InstanceManager
	public Instancia getInstance() {
		return instance;
	}

	public double[] getP() {
		return instance.getP();
	}

	public double[] getD() {
		return instance.getD();
	}

	// Reader
	public void readInstance(String fileName) {
		instance = reader.readInstance(fileName);
	}

	// ChartManager
	public void initializeChartManager(ResourceBundle texts) {
		chartManager = new ChartManager(texts);
	}

	public JFreeChart getChart() {
		return chartManager.getChart();
	}

	public void loadMainChart(int tickUnit) {
		chartManager.loadMainChart(instance, tickUnit);
	}

	public JFreeChart createDurationsChart(double[] durations) {
		return chartManager.createDurationsChart(durations);
	}

	public JFreeChart createDueDatesChart(double[] dueDates) {
		return chartManager.createDueDatesChart(dueDates);
	}

	public void createMainChart(Rule rule, double g, int tickUnit) {
		chartManager.createMainChart(instance, rule, g, tickUnit);
	}

	// InstanceGenerator
	public void initializeInstanceGenerator(int numberOfTasks, int maxCapacity) {
		instanceGenerator = new InstanceGenerator(numberOfTasks, maxCapacity);
	}

	public void createTasks() {
		instanceGenerator.createTasks();
	}

	public void createCapacity() {
		instanceGenerator.createIntervals();
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

	// Writer
	public void writeInstance(String fileName) {
		writer = new InstanceWriter(getDurations(), getDueDates(), getIntervalDurations(), getIntervalCapacities());
		writer.write(fileName);
	}

	public void writeChart(String fileName) {
		writer = new ChartWriter(getChart());
		writer.write(fileName);
	}
}
