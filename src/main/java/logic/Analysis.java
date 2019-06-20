package logic;

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

	/**
	 * Constructor de la clase {@link Analysis}
	 * 
	 * @param numberOfInstances
	 *            número de instancias a analizar por combinación
	 * @param numberOfTasks
	 *            vector de número de tareas a analizar
	 * @param maxCapacity
	 *            vector de capacidades máxima a analizar
	 */
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
