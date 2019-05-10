package logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import gui.enums.Rule;

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
	ResourceBundle texts;

	public ChartManager(ResourceBundle texts) {
		this.texts = texts;
	}

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
		XYPlot plot = chart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);

		// Modificar el eje X
		List<Intervalo> intervals = i.getPerfilMaquina();

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		Intervalo interval = intervals.get(intervals.size() - 1);
		int maxDomain = interval.getInicio(); // TODO menor en instancias grandes
		if (interval.getFin() < 300000)
			maxDomain = interval.getFin();

		domain.setRange(0.00, maxDomain + 1);
		domain.setTickUnit(new NumberTickUnit(tickUnit));
		domain.setVerticalTickLabels(true);
		domain.setLabelPaint(Color.BLACK);
		domain.setTickMarkPaint(Color.BLACK);
		domain.setAxisLinePaint(Color.BLACK);

		// Modificar el eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		int maxRange = 0;
		for (Intervalo aux : intervals)
			if (aux.getCap() > maxRange)
				maxRange = aux.getCap();

		range.setRange(0.0, maxRange + 1);
		range.setTickUnit(new NumberTickUnit(1));
		range.setTickMarkPaint(Color.BLACK);
		range.setAxisLinePaint(Color.BLACK);

		// Modificar el gráfico
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, Color.BLACK);

		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);
	}

	/**
	 * Método auxiliar que genera el dataset para el método
	 * {@link ChartManager#loadMainChart(int) loadMainChart}
	 * 
	 * @return dataset con el perfil de máquina
	 */
	private XYDataset loadMainChartDataset(Instancia i) {
		final XYSeries capacity = new XYSeries(texts.getString("chart_avaliable_capacity"));
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
	 * Método que retorna un gráfico con la distribución de duraciones de las tareas
	 * de una instancia para mostarlo en {@link gui.DurationsDialog DurationsDialog}
	 * 
	 * @param durations
	 *            vector de duraciones de las tareas
	 * @return gráfico de duraciones
	 */
	public JFreeChart createDurationsChart(double[] durations) {
		// Crear el gráfico
		JFreeChart durationsChart = ChartFactory.createXYLineChart("", texts.getString("chart_tasks"), "t",
				createDurationsDataset(durations), PlotOrientation.VERTICAL, true, true, false);

		// Modificar el fondo
		XYPlot plot = durationsChart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);

		// Modificar el eje X
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		int maxDomain = durations.length; // Nº Tareas

		domain.setRange(0.00, maxDomain - 1);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setVerticalTickLabels(true);
		domain.setLabelPaint(Color.BLACK);
		domain.setTickMarkPaint(Color.BLACK);
		domain.setAxisLinePaint(Color.BLACK);

		// Modificar el eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		double maxRange = 0;
		for (Double d : durations)
			if (d > maxRange)
				maxRange = d; // Max duración

		range.setRange(0.0, maxRange + 10);
		range.setTickUnit(new NumberTickUnit(10)); // TODO personalizable mejor? SI
		range.setTickMarkPaint(Color.BLACK);
		range.setAxisLinePaint(Color.BLACK);

		// Modificar el gráfico
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, Color.RED);

		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);

		// Retornar el resultado
		return durationsChart;
	}

	/**
	 * Método auxiliar que genera el dataset para el método
	 * {@link ChartManager#createDurationsChart(double[]) createDurationsChart}
	 * 
	 * @return dataset con el perfil de máquina
	 */
	private XYDataset createDurationsDataset(double[] durations) {
		XYSeries duration = new XYSeries(texts.getString("chart_duration"));
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (int i = 0; i < durations.length; i++)
			duration.add(i, durations[i]);
		dataset.addSeries(duration);
		return dataset;
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
		JFreeChart dueDatesChart = ChartFactory.createXYLineChart("", texts.getString("chart_tasks"), "t",
				createDueDatesDataset(dueDates), PlotOrientation.VERTICAL, true, true, false);

		// Modificar el fondo
		XYPlot plot = dueDatesChart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);

		// Modificar el eje X
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		int maxDomain = dueDates.length; // Nº Tareas

		domain.setRange(0.00, maxDomain - 1);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setVerticalTickLabels(true);
		domain.setLabelPaint(Color.BLACK);
		domain.setTickMarkPaint(Color.BLACK);
		domain.setAxisLinePaint(Color.BLACK);

		// Modificar el eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		double maxRange = 0;
		for (Double d : dueDates)
			if (d > maxRange)
				maxRange = d; // Max due date

		range.setRange(0.0, maxRange + 10);
		range.setTickUnit(new NumberTickUnit(10)); // TODO personalizable mejor? SI
		range.setTickMarkPaint(Color.BLACK);
		range.setAxisLinePaint(Color.BLACK);

		// Modificar el gráfico
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, Color.RED);

		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);

		// Retornar el resultado
		return dueDatesChart;
	}

	/**
	 * Método auxiliar que genera el dataset para el método
	 * {@link ChartManager#createDuedatesChart(double[]) createDueDatesChart}
	 * 
	 * @return dataset con el perfil de máquina
	 */
	private XYDataset createDueDatesDataset(double[] dueDates) {
		XYSeries dueDate = new XYSeries(texts.getString("chart_due_date"));
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (int i = 0; i < dueDates.length; i++)
			dueDate.add(i, dueDates[i]);
		dataset.addSeries(dueDate);
		return dataset;
	}

	public void createMainChart(Instancia i, Rule rule, double g, int tickUnit) {
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
		XYDataset ds = createMainChartDataset(i, p, capacity);

		// Crear el gráfico
		chart = ChartFactory.createXYLineChart("", "t", "", ds, PlotOrientation.VERTICAL, true, true, false);

		// Modificar el fondo //TODO como es variable de la clase igual te puedes saltar
		// esto [PROBAR]
		XYPlot plot = chart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);

		// Modificar eje X
		List<Intervalo> intervals = i.getPerfilMaquina();
		NumberAxis domain = (NumberAxis) plot.getDomainAxis();

		Intervalo interval = intervals.get(intervals.size() - 1);
		double maxDomain = interval.getInicio(); // TODO menor en instancias grandes
		if (interval.getFin() < 300000)
			maxDomain = interval.getFin();

		domain.setRange(0.00, maxDomain + 1);
		domain.setTickUnit(new NumberTickUnit(tickUnit));
		domain.setVerticalTickLabels(true);
		domain.setLabelPaint(Color.BLACK);
		domain.setTickMarkPaint(Color.BLACK);
		domain.setAxisLinePaint(Color.BLACK);

		// Modificar eje Y
		NumberAxis range = (NumberAxis) plot.getRangeAxis();

		int maxRange = 0;
		for (Intervalo aux : intervals)
			if (aux.getCap() > maxRange)
				maxRange = aux.getCap();

		range.setRange(0.0, maxRange + 1);
		range.setTickUnit(new NumberTickUnit(1));
		range.setTickMarkPaint(Color.BLACK);
		range.setAxisLinePaint(Color.BLACK);

		// Asignar renderer
		plot.setRenderer(renderer);

	}

	private XYDataset createMainChartDataset(Instancia i, Planificacion p, Map<Integer, Integer> occupied) {
		double[] durations = i.getP();
		int[] startTimes = p.getSti();
		double[] endTimes = durations.clone();

		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

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

		final XYSeries tasks = new XYSeries("Capacidad Ocupada");
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

		final XYSeries capacity = new XYSeries("Capacidad disponible");
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

	private Map<Integer, Integer> createTasks(XYLineAndShapeRenderer renderer, Instancia i, Planificacion p) {
		// Parámetros de visualización de las tareas
		BasicStroke stroke = new BasicStroke(1.0f);
		Color border = Color.BLACK;
		Color fill = Color.LIGHT_GRAY;
		double height = 1;
		double y = 0;
		double[] durations = i.getP();
		double[] dueDates = i.getD();
		int[] startTimes = p.getSti();
		double[] endTimes = durations.clone(); // startTime[t-1]
		HashMap<Double, Double> tasks = new HashMap<Double, Double>(); // [y][endTime]
		HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>(); // [startTime][taskID] startTime !único falla
		HashMap<Integer, Integer> capacity = new HashMap<Integer, Integer>(); // [time][occupied]

		for (int j = 0; j < startTimes.length; j++)
			ids.put(startTimes[j], j);

		for (int j = 0; j < endTimes.length; j++)
			endTimes[j] += startTimes[j];

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

		for (int t = 0; t < startTimes.length; t++) {
			if (t == 0) {
				tasks.put(0d, endTimes[t]);
				// capacity.put(0, 1);
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

			Shape rectangle = new Rectangle2D.Double(startTimes[t], y, durations[t], height);
			XYShapeAnnotation note = new XYShapeAnnotation(rectangle, stroke, border, fill);
			XYTextAnnotation text = new XYTextAnnotation("" + ids.get(startTimes[t]),
					rectangle.getBounds().getCenterX(), rectangle.getBounds().getCenterY());
			renderer.addAnnotation(note, Layer.BACKGROUND);
			renderer.addAnnotation(text); // TODO omitir en instancias grandes
		}

		for (int j = 0; j < endTimes[endTimes.length - 1]; j++) {
			for (int k = 0; k < startTimes.length; k++) { // recorrer tareas

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
}
