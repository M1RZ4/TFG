package logic;

import java.util.Arrays;

import dominio.Instancia;

/**
 * Clase PartialInstance que representa una instancia parcial. Hereda de
 * {@link dominio.Instancia Instancia}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class PartialInstance extends Instancia {

	public PartialInstance(int step, Instancia i) {
		super(i.getNombre());
		this.perfilMaquina = i.getPerfilMaquina();
		this.d = Arrays.copyOfRange(i.getD(), 0, step);
		this.p = Arrays.copyOfRange(i.getP(), 0, step);
	}

}
