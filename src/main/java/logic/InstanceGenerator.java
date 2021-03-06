package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dominio.Instancia;
import dominio.Intervalo;
import logic.instances.AnalysisInstance;

/**
 * Clase InstanceGenerator encargada de generar los vectores y listas con los
 * distintos parámtros de tareas e intervalos necesarios para generar los
 * ficheros de instancias por parte de la clase
 * {@link logic.io.InstanceWriter InstanceWriter}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceGenerator {

	private int numberOfTasks;
	private int maxCapacity;

	private double[] durations;
	private double[] dueDates;
	private int maxInterval;
	private List<Double> intervalDurations;
	private List<Integer> intervalCapacities;

	/**
	 * Constructor de la clase InstanceGenerator
	 * 
	 * @param numberOfTasks
	 *            número de tareas a generar
	 * @param maxCapacity
	 *            máxima capacidad de la máquina
	 */
	public InstanceGenerator(int numberOfTasks, int maxCapacity) {
		this.numberOfTasks = numberOfTasks;
		this.maxCapacity = maxCapacity;
	}
	
	public double[] getDurations() {
		return durations;
	}

	public double[] getDueDates() {
		return dueDates;
	}

	public List<Double> getIntervalDurations() {
		return intervalDurations;
	}

	public List<Integer> getIntervalCapacities() {
		return intervalCapacities;
	}

	public int getMaxInterval() {
		return maxInterval;
	}

	/**
	 * Método que genera los vectores de duraciones y due dates en función del
	 * número de tareas
	 */
	public void createTasks() {
		// Duraciones de las tareas
		durations = new double[numberOfTasks];
		for (int i = 0; i < durations.length; i++)
			durations[i] = new Random().nextInt(100) + 1;

		// Due dates de las tareas
		dueDates = new double[durations.length];
		for (int i = 0; i < dueDates.length; i++) {
			double sum = 0;
			for (int j = 0; j < i; j++)
				sum += durations[j];
			sum /= 2;
			double low = durations[i];
			double high = Math.max(durations[i] + 2, sum);
			dueDates[i] = new Random().nextInt((int) (high - low)) + low;
		}
	}

	/**
	 * Método que genera las listas de duraciones y capacidades de los intervalos en
	 * función del número de tareas y la capacidad máxima de la máquina
	 */
	public void createIntervals() {
		// Inicializar variables de la clase
		intervalDurations = new ArrayList<Double>();
		intervalCapacities = new ArrayList<Integer>();
		// Capacidad inicial
		int initialCapacity = new Random().nextInt(2) + 1;
		intervalCapacities.add(initialCapacity);
		// Capacidad final
		int finalCapacity = new Random().nextInt(2) + 1;
		// Calcular número de intervalos
		maxInterval = 2 * maxCapacity;
		if (initialCapacity == finalCapacity && initialCapacity == 1)
			maxInterval -= 1;
		else if (initialCapacity == finalCapacity && initialCapacity == 2)
			maxInterval -= 3;
		else
			maxInterval -= 2;
		// Calcular duraciones
		createDurations(maxInterval);
		// Calcular capacidades
		createCapacities(initialCapacity, finalCapacity);
	}

	/**
	 * Método auxiliar que calcula las duraciones de los intervalos
	 * 
	 * @param maxInterval
	 *            número máximo de intervalos a generar
	 */
	private void createDurations(int maxInterval) {
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

	}

	/**
	 * Método auxiliar que calcula las capacidades de los intervalos
	 * 
	 * @param initialCapacity
	 *            capacidad inicial
	 * @param finalCapacity
	 *            capacidad final
	 */
	private void createCapacities(int initialCapacity, int finalCapacity) {
		for (int i = initialCapacity + 1; i < maxCapacity; i++)
			intervalCapacities.add(i);
		for (int i = maxCapacity; i > finalCapacity; i--)
			intervalCapacities.add(i);
		intervalCapacities.add(finalCapacity);
	}

	/**
	 * Método que genera una lista de listas de {@link AnalysisInstance} para
	 * después planificarlas y realizar el análisis experimental
	 * 
	 * @param analysis
	 *            objeto análisis
	 * @param step
	 *            número de combinación
	 * @return lista de listas de instancias, una por cada combinación de t y c
	 */
	public List<List<Instancia>> generateInstances(Analysis analysis) {
		List<List<Instancia>> result = new ArrayList<List<Instancia>>();
		for (int t = 0; t < analysis.getNumberOfTasks().length; t++) {
			List<Instancia> instances = new ArrayList<Instancia>();
			for (int i = 0; i < analysis.getNumberOfInstances(); i++) {
				numberOfTasks = analysis.getNumberOfTasks()[t];
				maxCapacity = analysis.getMaxCapacity()[t];
				createTasks();
				createIntervals();
				AnalysisInstance instance = new AnalysisInstance(durations, dueDates);
				int start = 0;
				for (int j = 0; j < intervalCapacities.size(); j++) {
					int end = (int) (start + intervalDurations.get(j));
					Intervalo interval = new Intervalo(intervalCapacities.get(j), start, end);
					instance.add(interval);
					start = end;
				}
				instances.add(instance);
			}
			result.add(instances);
		}
		return result;
	}

}
