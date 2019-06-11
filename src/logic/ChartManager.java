package logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

import dominio.Gestor;
import dominio.Instancia;
import dominio.Intervalo;
import dominio.Planificacion;
import logic.enums.Rule;

/**
 * Clase ChartManager encargada de generar los gráficos que se mostrarán en la
 * aplicación y que podrán ser exportados por {@link logic.io.ChartWriter
 * ChartWriter}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class ChartManager {

	private JFreeChart chart;

	public JFreeChart getChart() {
		return chart;
	}

	/**
	 * Método que genera un gráfico con el perfil de máquina de una instancia para
	 * mostarlo en {@link gui.ApplicationWindow ApplicationWindow}
	 * 
	 * @param tickUnit
	 *            distancia entre marcas de graduación
	 */
	public void loadMainChart(Instancia i, int tickUnit) {
		// Crear el gráfico
		chart = ChartFactory.createXYLineChart("", "t", "", loadMainChartDataset(i), PlotOrientation.VERTICAL, true,
				true, false);

		// Modificar el fondo
		XYPlot plot = setPlot(chart);

		// Modificar el eje X
		List<Intervalo> intervals = i.getPerfilMaquina();

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		Intervalo interval = intervals.get(intervals.size() - 1);
		int maxDomain = interval.getInicio(); // TODO menor en instancias grandes
		if (interval.getFin() < 300000)
			maxDomain = interval.getFin();

		domain = setDomain(domain, maxDomain, tickUnit);

		// Modificar el eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		int maxRange = 0;
		for (Intervalo aux : intervals)
			if (aux.getCap() > maxRange)
				maxRange = aux.getCap();

		range = setRange(range, maxRange, 1);

		// Modificar el gráfico
		plot.setRenderer(setRenderer(Color.BLACK));
	}

	/**
	 * Método que retorna un gráfico con la distribución de duraciones de las tareas
	 * de una instancia para mostarlo en {@link gui.DurationsDialog DurationsDialog}
	 * 
	 * @param durations
	 *            vector de duraciones de las tareas
	 * @return gráfico de duraciones
	 */
	public JFreeChart createDurationsChart(double[] durations) {
		// Crear el gráfico
		JFreeChart durationsChart = ChartFactory.createXYLineChart("",
				LanguageManager.getInstance().getTexts().getString("chart_tasks"), "t",
				createDataset(durations, "chart_durations"), PlotOrientation.VERTICAL, true, true, false);

		// Modificar el fondo
		XYPlot plot = setPlot(durationsChart);

		// Modificar el eje X
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		int maxDomain = durations.length; // Nº Tareas

		domain = setDomain(domain, maxDomain, 1);

		// Modificar el eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		double maxRange = 0;
		for (Double d : durations)
			if (d > maxRange)
				maxRange = d; // Max duración

		range = setRange(range, maxRange, 10);// TODO personalizar (el 10)

		// Modificar el gráfico
		XYLineAndShapeRenderer renderer = setRenderer(Color.RED);
		plot.setRenderer(renderer);

		// Retornar el resultado
		return durationsChart;
	}

	/**
	 * Método que retorna un gráfico con la distribución de due dates de las tareas
	 * de una instancia para mostarlo en {@link gui.DueDatesDialog DueDatesDialog}
	 * 
	 * @param dueDates
	 *            vector de due dates de las tareas
	 * @return gráfico de duraciones
	 */
	public JFreeChart createDueDatesChart(double[] dueDates) {
		// Crear el gráfico
		JFreeChart dueDatesChart = ChartFactory.createXYLineChart("",
				LanguageManager.getInstance().getTexts().getString("chart_tasks"), "t",
				createDataset(dueDates, "chart_due_dates"), PlotOrientation.VERTICAL, true, true, false);

		// Modificar el fondo
		XYPlot plot = setPlot(dueDatesChart);

		// Modificar el eje X
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		int maxDomain = dueDates.length; // Nº Tareas

		domain = setDomain(domain, maxDomain, 1);

		// Modificar el eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		double maxRange = 0;
		for (Double d : dueDates)
			if (d > maxRange)
				maxRange = d; // Max due date

		range = setRange(range, maxRange, 10); // TODO personalizar (el 10)

		// Modificar el gráfico
		XYLineAndShapeRenderer renderer = setRenderer(Color.RED);
		plot.setRenderer(renderer);

		// Retornar el resultado
		return dueDatesChart;
	}

	/**
	 * Método auxiliar que modifica el fondo de un gráfico
	 * 
	 * @param chart
	 *            gráfico con el fondo a modificar
	 * @return fondo modificado
	 */
	private XYPlot setPlot(JFreeChart chart) {
		XYPlot plot = chart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);
		return plot;
	}

	/**
	 * Método auxiliar que modifica el eje X de un gráfico
	 * 
	 * @param domain
	 *            eje X
	 * @param maxDomain
	 *            máximo valor para el eje X
	 * @param tickUnit
	 *            separación entre marcas de graduación
	 * @return eje X modificado
	 */
	private NumberAxis setDomain(NumberAxis domain, double maxDomain, int tickUnit) {
		domain.setRange(0.00, maxDomain - 1);
		domain.setTickUnit(new NumberTickUnit(tickUnit));
		domain.setVerticalTickLabels(true);
		domain.setLabelPaint(Color.BLACK);
		domain.setTickMarkPaint(Color.BLACK);
		domain.setAxisLinePaint(Color.BLACK);
		return domain;
	}

	/**
	 * Método auxiliar que modifica el eje Y de un gráfico
	 * 
	 * @param range
	 *            eje Y
	 * @param maxRange
	 *            máximo valor para el eje Y
	 * @param tickUnit
	 *            separación entre marcas de graduación
	 * @return eje Y modificado
	 */
	private NumberAxis setRange(NumberAxis range, double maxRange, int tickUnit) {
		range.setRange(0.0, maxRange + 1); //TODO en setDomain se tuvo que cambiar
		range.setTickUnit(new NumberTickUnit(tickUnit));
		range.setTickMarkPaint(Color.BLACK);
		range.setAxisLinePaint(Color.BLACK);
		return range;
	}

	/**
	 * Método auxiliar que modifica el renderer de un gráfico
	 * 
	 * @param color
	 *            Color del gráfico
	 * @return renderer modificado
	 */
	private XYLineAndShapeRenderer setRenderer(Color color) {
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, color);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		return renderer;
	}

	/**
	 * Método auxiliar que genera el dataset para el método
	 * {@link ChartManager#loadMainChart(int) loadMainChart}
	 * 
	 * @return dataset con el perfil de máquina
	 */
	private XYDataset loadMainChartDataset(Instancia i) {
		final XYSeries capacity = new XYSeries(
				LanguageManager.getInstance().getTexts().getString("chart_avaliable_capacity"));
		for (int j = 0; j < i.getPerfilMaquina().size(); j++) {
			if (j == i.getPerfilMaquina().size() - 1) {
				// capacity.add(i.getPerfilMaquina().get(j).getInicio(),
				// i.getPerfilMaquina().get(j).getCap());
				// capacity.add(i.getPerfilMaquina().get(j).getInicio() + 100,
				// i.getPerfilMaquina().get(j).getCap()); //TODO el 100
			} else {
				capacity.add(i.getPerfilMaquina().get(j).getInicio(), i.getPerfilMaquina().get(j).getCap());
				capacity.add(i.getPerfilMaquina().get(j).getFin(), i.getPerfilMaquina().get(j).getCap());
			}
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(capacity);
		return dataset;
	}

	/**
	 * Método auxiliar que genera el dataset para los métodos
	 * {@link ChartManager#createDuedatesChart(double[]) createDueDatesChart} y
	 * {@link ChartManager#createDurationsChart(double[]) createDueDatesChart}
	 * 
	 * @param array
	 *            vector de duraciones o due dates
	 * @param series
	 *            nombre del parámetro
	 * @return dataset con el perfil de máquina
	 */
	private XYDataset createDataset(double[] array, String series) {
		XYSeries dueDate = new XYSeries(LanguageManager.getInstance().getTexts().getString(series));
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (int i = 0; i < array.length; i++)
			dueDate.add(i, array[i]);
		dataset.addSeries(dueDate);
		return dataset;
	}

	/**
	 * Método que modifica el gráfico con el perfil de máquina de la instancia que
	 * se muestra en {@link gui.ApplicationWindow ApplicationWindow}, añadiendo
	 * tareas y capacidad ocupada en función de la regla de prioridad escogida
	 * 
	 * @param i
	 *            instancia
	 * @param rule
	 *            regla de prioridad
	 * @param g
	 *            parámetro g para la regla ATC
	 * @param tickUnit
	 *            separación entre marcas de graduación
	 */
	public void setMainChart(Instancia i, Rule rule, double g, int tickUnit) {
		// Escoger la planificación
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

		// Modificar el gráfico
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesShapesVisible(1, false);
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesPaint(1, Color.BLACK);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));

		// Crear las tareas
		Map<Integer, Integer> capacity = createTasks(renderer, i, p);

		// Crear el dataset
		XYDataset ds = setMainChartDataset(i, p, capacity);

		// Crear el gráfico
		chart = ChartFactory.createXYLineChart("", "t", "", ds, PlotOrientation.VERTICAL, true, true, false);

		// Modificar el fondo
		XYPlot plot = setPlot(chart);

		// Modificar eje X
		List<Intervalo> intervals = i.getPerfilMaquina();
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		Intervalo interval = intervals.get(intervals.size() - 1);
		double maxDomain = interval.getInicio(); // TODO menor en instancias grandes
		if (interval.getFin() < 300000)
			maxDomain = interval.getFin();

		domain = setDomain(domain, maxDomain, tickUnit);

		// Modificar eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		int maxRange = 0;
		for (Intervalo aux : intervals)
			if (aux.getCap() > maxRange)
				maxRange = aux.getCap();

		range = setRange(range, maxRange, 1);

		// Asignar renderer
		plot.setRenderer(renderer);

	}

	/**
	 * Método auxiliar que genera un diccionario que representa la capacidad ocupada
	 * en cada instante en el gráfico en base a la planificación dada. También crea
	 * los elementos gráficos asociados a las tareas generadas
	 * 
	 * @param renderer
	 *            renderer del gráfico
	 * @param i
	 *            instancia
	 * @param p
	 *            planificación
	 * @return diccionario de capacidades en el tiempo
	 */
	private Map<Integer, Integer> createTasks(XYLineAndShapeRenderer renderer, Instancia i, Planificacion p) {
		// Inicializar parámetros de visualización de las tareas
		BasicStroke stroke = new BasicStroke(1.0f);
		Color border = Color.BLACK;
		Color fill = Color.LIGHT_GRAY;
		double height = 1;

		// Inicializar otros parámetros
		double y = 0;
		double[] durations = i.getP();
		double[] dueDates = i.getD();
		int[] startTimes = p.getSti();
		double[] endTimes = durations.clone(); // Vector de tiempos de fin
		HashMap<Double, Double> tasks = new HashMap<Double, Double>(); // Diccionario con clave [y] y valor [tiempo de
																		// fin]
		HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>(); // Diccionario con clave [tiempo de inicio] y
																			// valor [ID de la tarea]
		HashMap<Integer, Integer> capacity = new HashMap<Integer, Integer>(); // Diccionario con clave [tiempo] y valor
																				// [capacidad ocupada]

		// Asignar IDs
		for (int j = 0; j < startTimes.length; j++)
			ids.put(startTimes[j], j);

		// Asignar tiempos de fin
		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

		// Ordenar los vectores de tiempos de inicio, fin, duraciones y due dates en
		// función de los tiempos de inicio (la ordenación por defecto es en base al ID)
		for (int s = 0; s <= startTimes.length - 1; s++) {
			for (int k = 0; k <= startTimes.length - 2; k++) {
				if (startTimes[k] > startTimes[k + 1]) {
					double temp = 0d;

					temp = startTimes[k];
					startTimes[k] = startTimes[k + 1];
					startTimes[k + 1] = (int) temp;

					temp = endTimes[k];
					endTimes[k] = endTimes[k + 1];
					endTimes[k + 1] = temp;

					temp = durations[k];
					durations[k] = durations[k + 1];
					durations[k + 1] = temp;

					temp = dueDates[k];
					dueDates[k] = dueDates[k + 1];
					dueDates[k + 1] = temp;
				}
			}
		}

		// Asignar tareas
		for (int t = 0; t < startTimes.length; t++) {
			if (t == 0) {
				tasks.put(0d, endTimes[t]);
			} else {
				Object[] sortedKeys = tasks.keySet().toArray();
				Arrays.sort(sortedKeys);
				boolean found = false;
				for (int j = 0; j < sortedKeys.length; j++)
					if (tasks.get(sortedKeys[j]) <= startTimes[t]) {
						y = (double) sortedKeys[j];
						found = true;
						break;
					}
				if (!found) {
					y = (double) sortedKeys[sortedKeys.length - 1] + 1;
				}
				tasks.put(y, endTimes[t]);
			}

			// Crear las tareas gráficamente
			Shape rectangle = new Rectangle2D.Double(startTimes[t], y, durations[t], height);
			XYShapeAnnotation note = new XYShapeAnnotation(rectangle, stroke, border, fill);
			XYTextAnnotation text = new XYTextAnnotation("" + ids.get(startTimes[t]),
					rectangle.getBounds().getCenterX(), rectangle.getBounds().getCenterY());
			renderer.addAnnotation(note, Layer.BACKGROUND);
			renderer.addAnnotation(text); // TODO omitir en instancias grandes
		}

		// Asignar capacidades
		for (int j = 0; j < endTimes[endTimes.length - 1]; j++) {
			for (int k = 0; k < startTimes.length; k++) {

				if (j >= startTimes[k] && j < endTimes[k]) {
					if (capacity.get(j) == null) {
						capacity.put(j, 1);
					} else {
						capacity.put(j, capacity.get(j) + 1);
					}
				}
			}

		}
		capacity.put((int) endTimes[endTimes.length - 1], 1); // endTime !unico falla
		return capacity;
	}

	/**
	 * Método auxiliar que modifica el dataset para el método
	 * {@link ChartManager#setMainChart(Instancia, Rule, double, int) setMainChart}
	 * 
	 * @param i
	 *            instancia
	 * @param p
	 *            planificación
	 * @param occupied
	 *            diccionario que representa la capacidad ocupada en el tiempo
	 * @return dataset con las capacidades ocupada y disponible
	 */
	private XYDataset setMainChartDataset(Instancia i, Planificacion p, Map<Integer, Integer> occupied) {
		// Inicializar parámetros
		double[] durations = i.getP();
		int[] startTimes = p.getSti();
		double[] endTimes = durations.clone(); // Vector de tiempos de fin

		// Asignar tiempos de fin
		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

		// Ordenar los vectores de tiempos de inicio, fin, duraciones y due dates en
		// función de los tiempos de inicio (la ordenación por defecto es en base al ID)
		for (int s = 0; s <= startTimes.length - 1; s++) {
			for (int k = 0; k <= startTimes.length - 2; k++) {
				if (startTimes[k] > startTimes[k + 1]) {
					double temp = 0d;

					temp = startTimes[k];
					startTimes[k] = startTimes[k + 1];
					startTimes[k + 1] = (int) temp;

					temp = endTimes[k];
					endTimes[k] = endTimes[k + 1];
					endTimes[k + 1] = temp;

					temp = durations[k];
					durations[k] = durations[k + 1];
					durations[k + 1] = temp;

				}
			}
		}

		// Crear serie para la capacidad ocupada
		final XYSeries tasks = new XYSeries(
				LanguageManager.getInstance().getTexts().getString("chart_occupied_capacity"));
		Object[] sortedKeys = occupied.keySet().toArray();
		Arrays.sort(sortedKeys);
		for (int j = 0; j < sortedKeys.length; j++) {
			for (int t = 0; t < startTimes.length; t++) {
				int key = (int) sortedKeys[j];
				if (key == startTimes[t] || key == endTimes[t]) {
					if (key > 0 && occupied.get(key) > occupied.get(sortedKeys[j - 1]))
						tasks.add(key, occupied.get(key) - 1);
					else if (key > 0 && occupied.get(key) < occupied.get(sortedKeys[j - 1]))
						tasks.add(key, occupied.get(key - 1));
					tasks.add(key, occupied.get(key));
				}
			}
		}
		tasks.add(endTimes[endTimes.length - 1], 0);

		// Crear serie para la capacidad disponible
		final XYSeries capacity = new XYSeries(
				LanguageManager.getInstance().getTexts().getString("chart_avaliable_capacity"));
		for (int j = 0; j < i.getPerfilMaquina().size(); j++) {
			if (j == i.getPerfilMaquina().size() - 1)
				break;
			capacity.add(i.getPerfilMaquina().get(j).getInicio(), i.getPerfilMaquina().get(j).getCap());
			capacity.add(i.getPerfilMaquina().get(j).getFin(), i.getPerfilMaquina().get(j).getCap());
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(tasks);
		dataset.addSeries(capacity);
		return dataset;
	}

}
