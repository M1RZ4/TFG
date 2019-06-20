package logic.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dominio.Instancia;
import dominio.Intervalo;

/**
 * Clase InstanceWriter encargada de generar ficheros de texto con instancias en
 * función de los parámetros generados con {@link logic.InstanceGenerator
 * InstanceGenerator}. Implementa la interfaz {@link Writer}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceWriter implements Writer {

	private double[] durations;
	private double[] dueDates;
	private List<Double> intervalDurations;
	private List<Integer> intervalCapacities;

	/**
	 * Constructor para la clase {@link InstanceWriter}
	 * 
	 * @param durations
	 *            vector de duraciones
	 * @param dueDates
	 *            vector de due dates
	 * @param intervalDurations
	 *            lista de duraciones de los intervalos
	 * @param intervalCapacities
	 *            lista de capacidades máximas de los intervalos
	 */
	public InstanceWriter(double[] durations, double[] dueDates, List<Double> intervalDurations,
			List<Integer> intervalCapacities) {
		this.durations = durations;
		this.dueDates = dueDates;
		this.intervalDurations = intervalDurations;
		this.intervalCapacities = intervalCapacities;
	}

	/**
	 * Constructor para la clase {@link InstanceWriter}
	 * 
	 * @param i
	 *            instancia a guardar
	 */
	public InstanceWriter(Instancia i) {
		this.durations = i.getP();
		this.dueDates = i.getD();
		List<Intervalo> list = i.getPerfilMaquina();
		intervalCapacities = new ArrayList<Integer>();
		intervalDurations = new ArrayList<Double>();
		for (Intervalo x : list) {
			intervalCapacities.add(x.getCap());
			intervalDurations.add((double) (x.getFin() - x.getInicio()));
		}
	}

	/**
	 * Método implementado de la interfaz {@link Writer} que crea un archivo de
	 * texto de nombre fileName con los parámetros de una instancia
	 * 
	 * @param fileName
	 *            nombre del fichero
	 */
	@Override
	public void write(String fileName) {
		try (FileWriter fw = new FileWriter(fileName)) {
			fw.write("NOP: " + durations.length + "\n");
			fw.write("NINT: " + intervalDurations.size() + "\n");
			int previous = 0;
			for (int i = 0; i < intervalDurations.size(); i++) {
				int sum = (int) (previous + intervalDurations.get(i));
				fw.write(previous + " " + sum + " " + intervalCapacities.get(i) + "\n");
				previous = sum;
			}
			for (int i = 0; i < durations.length; i++) {
				fw.write((i + 1) + " " + (int) durations[i] + " " + (int) dueDates[i]);
				if (i < durations.length - 1)
					fw.write("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
