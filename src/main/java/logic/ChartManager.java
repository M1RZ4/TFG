package main.java.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

import dominio.Instancia;
import dominio.Intervalo;
import main.java.logic.enums.Rule;
import main.java.logic.instances.ScheduledInstance;

/**
 * Clase ChartManager encargada de generar los gráficos que se mostrarán en la
 * aplicación y que podrán ser exportados por
 * {@link main.java.logic.io.ChartWriter ChartWriter}
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
		int maxDomain = interval.getInicio();
		maxDomain += maxDomain / 10;
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
	 * de una instancia para mostarlo en {@link gui.dialogs.DurationsDialog
	 * DurationsDialog}
	 * 
	 * @param durations
	 *            vector de duraciones de las tareas
	 * @return gráfico de duraciones
	 */
	public JFreeChart createDurationsChart(double[] durations, int[] ids) {
		JFreeChart chart = ChartFactory.createPieChart(
				LanguageManager.getInstance().getTexts().getString("chart_durations"), createDataset(durations, ids),
				true, true, false);
		Plot plot = chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		return chart;
	}

	/**
	 * Método que retorna un gráfico con la distribución de due dates de las tareas
	 * de una instancia para mostarlo en {@link gui.dialogs.DueDatesDialog
	 * DueDatesDialog}
	 * 
	 * @param dueDates
	 *            vector de due dates de las tareas
	 * @return gráfico de duraciones
	 */
	public JFreeChart createDueDatesChart(double[] dueDates, int[] ids) {
		JFreeChart chart = ChartFactory.createPieChart(
				LanguageManager.getInstance().getTexts().getString("chart_due_dates"), createDataset(dueDates, ids),
				true, true, false);
		Plot plot = chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		return chart;
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
		range.setRange(0.0, maxRange + 1);
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
		final XYSeries capacity = createCapacity(i);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(capacity);
		return dataset;
	}

	/**
	 * Método auxiliar que genera la serie para la capacidad disponible de la
	 * máquina
	 * 
	 * @param i
	 *            instancia
	 * @return serie con la capacidad
	 */
	private XYSeries createCapacity(Instancia i) {
		final XYSeries capacity = new XYSeries(
				LanguageManager.getInstance().getTexts().getString("chart_avaliable_capacity"));
		for (int j = 0; j < i.getPerfilMaquina().size(); j++) {
			if (j == i.getPerfilMaquina().size() - 1) {
				capacity.add(i.getPerfilMaquina().get(j).getInicio(), i.getPerfilMaquina().get(j).getCap());
				capacity.add(
						i.getPerfilMaquina().get(j).getInicio()
								+ i.getPerfilMaquina().get(i.getPerfilMaquina().size() - 1).getInicio() / 10,
						i.getPerfilMaquina().get(j).getCap());
			} else {
				capacity.add(i.getPerfilMaquina().get(j).getInicio(), i.getPerfilMaquina().get(j).getCap());
				capacity.add(i.getPerfilMaquina().get(j).getFin(), i.getPerfilMaquina().get(j).getCap());
			}
		}
		return capacity;
	}

	/**
	 * Método auxiliar que genera el dataset para los métodos
	 * {@link ChartManager#createDuedatesChart(double[]) createDueDatesChart} y
	 * {@link ChartManager#createDurationsChart(double[]) createDueDatesChart}
	 * 
	 * @param array
	 *            vector de duraciones o due dates
	 * @return dataset
	 */
	private PieDataset createDataset(double[] array, int[] ids) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int i = 0; i < array.length; i++)
			dataset.setValue(
					String.valueOf(LanguageManager.getInstance().getTexts().getString("chart_task") + " " + ids[i]),
					array[i]);
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
	public void setMainChart(int step, Instancia instance, Rule rule, double g, int tickUnit, boolean displayNumbers) {
		ScheduledInstance i;
		switch (rule) {
		case MyRule:
			i = (ScheduledInstance) instance;
			break;
		default:
			i = new ScheduledInstance(step, instance, rule, g);
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
		double sum = 0;
		for (int j = 0; j < instance.getP().length; j++)
			sum += instance.getP()[j];
		Map<Integer, Integer> capacity = createTasks(renderer, i, displayNumbers, (int) sum);

		// Crear el dataset
		XYDataset ds = setMainChartDataset(i, capacity);

		// Crear el gráfico
		chart = ChartFactory.createXYLineChart("", "t", "", ds, PlotOrientation.VERTICAL, true, true, false);

		// Modificar el fondo
		XYPlot plot = setPlot(chart);

		// Modificar eje X
		List<Intervalo> intervals = i.getPerfilMaquina();
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		Intervalo interval = intervals.get(intervals.size() - 1);
		double maxDomain = interval.getInicio();
		maxDomain += maxDomain / 10;
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
	private Map<Integer, Integer> createTasks(XYLineAndShapeRenderer renderer, ScheduledInstance i,
			boolean displayNumbers, int maxDuration) {
		// Inicializar parámetros de visualización de las tareas
		BasicStroke stroke = new BasicStroke(1.0f);
		Color border = Color.BLACK;
		Color fill = Color.LIGHT_GRAY;
		double height = 1;

		// Inicializar otros parámetros
		double y = 0;
		double[] durations = i.getP();
		int[] startTimes = i.getStartTimes();
		double[] endTimes = durations.clone(); // Vector de tiempos de fin
		HashMap<Double, Double> tasks = new HashMap<Double, Double>(); // Diccionario con clave [y] y valor [tiempo de
																		// fin]
		HashMap<Integer, Integer> capacity = new HashMap<Integer, Integer>(); // Diccionario con clave [tiempo] y valor
																				// [capacidad ocupada]

		// Asignar tiempos de fin
		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

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
			note.setToolTipText(LanguageManager.getInstance().getTexts().getString("chart_task") + " " + i.getIds()[t]);
			renderer.addAnnotation(note, Layer.BACKGROUND);
			// Crear las anotaciones (solo en instancias con n < 20)
			if (displayNumbers) {
				XYTextAnnotation text = new XYTextAnnotation("" + i.getIds()[t], rectangle.getBounds().getCenterX(),
						rectangle.getBounds().getCenterY());
				text.setFont(new Font("", Font.BOLD, 12));
				renderer.addAnnotation(text);
			}
		}

		// Asignar capacidades
		for (int j = 0; j < maxDuration; j++) {
			boolean free = true;
			for (int k = 0; k < startTimes.length; k++) {
				if (j >= startTimes[k] && j < endTimes[k]) {
					free = false;
					if (capacity.get(j) == null) {
						capacity.put(j, 1);
					} else {
						capacity.put(j, capacity.get(j) + 1);
					}
				}
			}
			if (free)
				capacity.put(j, 0);

		}
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
	private XYDataset setMainChartDataset(ScheduledInstance i, Map<Integer, Integer> occupied) {
		// Inicializar parámetros
		double[] durations = i.getP();
		int[] startTimes = i.getStartTimes();
		double[] endTimes = durations.clone();

		// Asignar tiempos de fin
		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

		// Crear serie para la capacidad ocupada
		final XYSeries tasks = new XYSeries(
				LanguageManager.getInstance().getTexts().getString("chart_occupied_capacity"));
		Object[] sortedKeys = occupied.keySet().toArray();
		Arrays.sort(sortedKeys);
		int last = 0;
		for (int j = 0; j < sortedKeys.length; j++) {
			boolean isTask = false;
			for (int t = 0; t < i.getD().length; t++) {
				if (j == startTimes[t] || j == endTimes[t])
					isTask = true;
			}
			if (isTask) {
				System.out.println("Coincidencia con inicio/fin tarea - j = " + j + " - Valor actual = "
						+ occupied.get(j) + " - Anterior valor = " + last);
				if (j > 0) {
					if (occupied.get(j) > last) {
						tasks.add(j, last);
						tasks.add(j, occupied.get(j));
						last = occupied.get(j);
						System.out.println("Añadidos puntos en j = " + j);
					} else if (occupied.get(j) < last) {
						tasks.add(j, last);
						tasks.add(j, occupied.get(j));
						last = occupied.get(j);
						System.out.println("Añadidos puntos en j = " + j);
					} else
						System.out.println("No se han encontrado puntos que añadir en j = " + j);
				} else {
					tasks.add(j, occupied.get(j));
					last = occupied.get(j);
				}
			}
		}
		// tasks.add(Arrays.stream(endTimes).max().getAsDouble(), 0);

		// Crear serie para la capacidad disponible
		final XYSeries capacity = createCapacity(i);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(tasks);
		dataset.addSeries(capacity);
		return dataset;
	}

}
