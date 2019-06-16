package main.java.logic;

/**
 * Clase Analysis que almacena la información necesaria para realizar un
 * análisis experimental
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class Analysis {

	private int numberOfInstances;
	private int[] numberOfTasks;
	private int[] maxCapacity;

	public Analysis(int numberOfInstances, int[] numberOfTasks, int[] maxCapacity) {
		this.numberOfInstances = numberOfInstances;
		this.numberOfTasks = numberOfTasks;
		this.maxCapacity = maxCapacity;
	}

	public int getNumberOfInstances() {
		return numberOfInstances;
	}

	public int[] getNumberOfTasks() {
		return numberOfTasks;
	}

	public int[] getMaxCapacity() {
		return maxCapacity;
	}

}
