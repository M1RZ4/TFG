package logic;

import dominio.Instancia;
import logic.io.Reader;
import logic.io.Writer;

/**
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceManager {

	private Reader reader;
	private Writer writer;
	private InstanceGenerator instanceGenerator;
	private ChartManager chartManager;
	private Instancia instance;
}
