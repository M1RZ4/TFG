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

	public ScheduledInstance(Instancia i) {
		super(i.getNombre());
		perfilMaquina = i.getPerfilMaquina();
	}

	public ScheduledInstance(int step, Instancia i, Rule rule, double g) {
		super(i.getNombre());
		this.perfilMaquina = i.getPerfilMaquina();

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
		case MyRule:
			return;
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

	public void setID(int id) {
		if (ids == null)
			ids = new int[0];
		int[] temp = Arrays.copyOf(ids, ids.length + 1);
		temp[temp.length - 1] = id;
		this.ids = temp;
	}

	public void setD(double d) {
		if (this.d == null)
			this.d = new double[0];
		double[] temp = Arrays.copyOf(this.d, this.d.length + 1);
		temp[temp.length - 1] = d;
		this.d = temp;
	}

	public void setP(double p) {
		if (this.p == null)
			this.p = new double[0];
		double[] temp = Arrays.copyOf(this.p, this.p.length + 1);
		temp[temp.length - 1] = p;
		this.p = temp;
	}

	public void setSti(int sti) {
		if (startTimes == null)
			startTimes = new int[0];
		int[] temp = Arrays.copyOf(startTimes, startTimes.length + 1);
		temp[temp.length - 1] = sti;
		startTimes = temp;
	}
}
