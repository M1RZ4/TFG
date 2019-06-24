package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.jfree.chart.JFreeChart;

import dominio.Instancia;
import logic.enums.Rule;
import logic.instances.AnalysisInstance;
import logic.instances.ScheduledInstance;
import logic.io.AnalysisReader;
import logic.io.AnalysisWriter;
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

	private Writer writer;
	private InstanceGenerator instanceGenerator;
	private ChartManager chartManager = new ChartManager();
	private Instancia instance;
	private Analysis analysis;

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
		instance = new InstanceReader().read(fileName);
	}

	public void readAnalysis(String fileName) throws FileNotFoundException {
		analysis = new AnalysisReader().read(fileName);
	}

	public Analysis getAnalysis() {
		return analysis;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
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
		boolean temp = true;
		if (getD().length > 20)
			temp = false;
		chartManager.setMainChart(step, instance, rule, g, tickUnit, temp, getP());
	}

	public void setMainChart(int step, ScheduledInstance i, int tickUnit) {
		boolean temp = true;
		if (getD().length > 20)
			temp = false;
		chartManager.setMainChart(step, i, Rule.MyRule, 0, tickUnit, temp, getP());
	}

	// InstanceGenerator
	public void initializeInstanceGenerator(int numberOfTasks, int maxCapacity) {
		instanceGenerator = new InstanceGenerator(numberOfTasks, maxCapacity);
	}

	public void createTasks() {
		instanceGenerator.createTasks();
	}

	public void createIntervals() {
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

	public int getMaxInterval() {
		return instanceGenerator.getMaxInterval();
	}

	public List<List<Instancia>> generateInstances() {
		return instanceGenerator.generateInstances(analysis);
	}

	// Writer
	public void writeInstance(String fileName) {
		writer = new InstanceWriter(getDurations(), getDueDates(), getIntervalDurations(), getIntervalCapacities());
		writer.write(fileName);
	}

	public void saveInstances(List<List<Instancia>> list) {
		File dir = new File("temp");
		dir.mkdir();

		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				Instancia x = list.get(i).get(j);
				String name = dir.getAbsolutePath() + "/" + i + "_" + j + ".txt";
				((AnalysisInstance) x).setName(name);
				writer = new InstanceWriter(x);
				writer.write(name);
			}
		}
	}

	public void writeChart(String fileName) {
		writer = new ChartWriter(getChart());
		writer.write(fileName);
	}

	public void writeAnalysis(String fileName) {
		List<List<Instancia>> list = generateInstances();
		saveInstances(list);
		writer = new AnalysisWriter(analysis, list);
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
