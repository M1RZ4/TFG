package main.java.logic;

import java.util.Arrays;
import java.util.stream.IntStream;

import dominio.Gestor;
import dominio.Instancia;
import dominio.Planificacion;
import main.java.logic.enums.Rule;

/**
 * Clase SheduledInstancia que representa una instancia planificada
 * parcialmente. Hereda de {@link dominio.Instancia Instancia}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class ScheduledInstance extends Instancia {

	private int[] ids;
	private int[] startTimes;

	public ScheduledInstance(int step, Instancia i, Rule rule, double g) {
		super(i.getNombre());

		Planificacion p = null;
		switch (rule) {
		case ATC:
			p = Gestor.planificaATC(g, i);
			break;
		case EDD:
			p = Gestor.planificaEDD(i);
			break;
		case SPT:
			p = Gestor.planificaSPT(i);
			break;
		default:
			break;
		}

		double[] durations = i.getP().clone();
		double[] dueDates = i.getD().clone();
		int[] startTimes = p.getSti().clone();
		ids = IntStream.range(0, durations.length).toArray();

		for (int s = 0; s <= startTimes.length - 1; s++) {
			for (int k = 0; k <= startTimes.length - 2; k++) {
				if (startTimes[k] > startTimes[k + 1]) {
					double temp = 0d;

					temp = startTimes[k];
					startTimes[k] = startTimes[k + 1];
					startTimes[k + 1] = (int) temp;

					temp = durations[k];
					durations[k] = durations[k + 1];
					durations[k + 1] = temp;

					temp = dueDates[k];
					dueDates[k] = dueDates[k + 1];
					dueDates[k + 1] = temp;

					temp = ids[k];
					ids[k] = ids[k + 1];
					ids[k + 1] = (int) temp;
				}
			}
		}

		this.perfilMaquina = i.getPerfilMaquina();
		this.d = Arrays.copyOfRange(dueDates, 0, step);
		this.p = Arrays.copyOfRange(durations, 0, step);
		this.startTimes = Arrays.copyOfRange(startTimes, 0, step);
	}

	public int[] getIds() {
		return ids;
	}

	public int[] getStartTimes() {
		return startTimes;
	}
}
