package logic;

import java.io.FileNotFoundException;
import java.util.List;

import org.jfree.chart.JFreeChart;

import dominio.Instancia;
import logic.enums.Rule;
import logic.io.ChartWriter;
import logic.io.InstanceReader;
import logic.io.InstanceWriter;
import logic.io.Writer;

/**
 * Clase principal InstanceManager que gestiona las diversas funcionalidades del
 * proyecto a través de sus distintos componentes
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceManager {

	private InstanceReader reader = new InstanceReader();
	private Writer writer;
	private InstanceGenerator instanceGenerator;
	private ChartManager chartManager = new ChartManager();
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
	public void readInstance(String fileName) throws FileNotFoundException {
		instance = reader.readInstance(fileName);
	}

	// ChartManager

	public JFreeChart getChart() {
		return chartManager.getChart();
	}

	public void loadMainChart(int tickUnit) {
		chartManager.loadMainChart(instance, tickUnit);
	}

	public JFreeChart createDurationsChart(double[] durations, int[] ids) {
		return chartManager.createDurationsChart(durations, ids);
	}

	public JFreeChart createDueDatesChart(double[] dueDates, int ids[]) {
		return chartManager.createDueDatesChart(dueDates, ids);
	}

	public void setMainChart(int step, Rule rule, double g, int tickUnit) {
		chartManager.setMainChart(step, instance, rule, g, tickUnit);
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

	// LanguageManager
	public String getText(String key) {
		return LanguageManager.getInstance().getTexts().getString(key);
	}

	public char getMnemonic(String key) {
		return LanguageManager.getInstance().getTexts().getString(key).charAt(0);
	}
}
