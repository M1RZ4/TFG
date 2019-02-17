package logic;

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

	public Reader getReader() {
		return reader;
	}

	public Writer getWriter() {
		return writer;
	}

	public InstanceGenerator getInstanceGenerator() {
		return instanceGenerator;
	}

	public ChartManager getChartManager() {
		return chartManager;
	}

}
