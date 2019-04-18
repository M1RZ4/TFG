package logic;

import java.util.List;

/**
 * Clase InstanceGenerator
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceGenerator {

	private double[] durations;
	private double[] dueDates;
	private List<Double> intervalDurations;
	private List<Integer> intervalCapacities;

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

}
