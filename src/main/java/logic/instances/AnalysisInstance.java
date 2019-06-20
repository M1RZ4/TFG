package logic.instances;

import java.util.ArrayList;

import dominio.Instancia;
import dominio.Intervalo;

/**
 * Clase AnalysisInstance que representa una instancia generada para su uso en
 * el análisis experimental
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class AnalysisInstance extends Instancia {

	/**
	 * Constructor de la clase {@link AnalysisInstance}
	 * 
	 * @param p
	 *            vector de duraciones
	 * @param d
	 *            vector de due dates
	 */
	public AnalysisInstance(double[] p, double[] d) {
		super("");
		this.p = p;
		this.d = d;
	}

	public void add(Intervalo interval) {
		if (perfilMaquina == null)
			perfilMaquina = new ArrayList<Intervalo>();
		perfilMaquina.add(interval);
	}

	public void setName(String name) {
		nombre = name;
	}

}
