package logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import java.util.ResourceBundle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import dominio.Instancia;
import dominio.Intervalo;

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
	Instancia instance;

	public ChartManager(ResourceBundle texts, Instancia instance) {
		this.texts = texts;
		this.instance = instance;
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
	public void loadMainChart(int tickUnit) {
		// Crear el gráfico
		chart = ChartFactory.createXYLineChart("", "t", "", createMainChartDataset(), PlotOrientation.VERTICAL, true,
				true, false);

		// Modificar el fondo
		XYPlot plot = chart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);

		// Modificar el eje X
		List<Intervalo> intervals = instance.getPerfilMaquina();

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
	private XYDataset createMainChartDataset() {
		final XYSeries capacity = new XYSeries(texts.getString("chart_avaliable_capacity"));
		for (int j = 0; j < instance.getPerfilMaquina().size(); j++) {
			if (j == instance.getPerfilMaquina().size() - 1) {
				// capacity.add(i.getPerfilMaquina().get(j).getInicio(),
				// i.getPerfilMaquina().get(j).getCap());
				// capacity.add(i.getPerfilMaquina().get(j).getInicio() + 100,
				// i.getPerfilMaquina().get(j).getCap()); //TODO el 100
			} else {
				capacity.add(instance.getPerfilMaquina().get(j).getInicio(),
						instance.getPerfilMaquina().get(j).getCap());
				capacity.add(instance.getPerfilMaquina().get(j).getFin(), instance.getPerfilMaquina().get(j).getCap());
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
	 * @return gráfico de duraciones
	 */
	public JFreeChart createDurationsChart(double[] durations) {
		// Crear el gráfico
		JFreeChart durationsChart = ChartFactory.createXYLineChart("", texts.getString("chart_tasks"), "t", createDurationsDataset(durations),
				PlotOrientation.VERTICAL, true, true, false);

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
	 * {@link ChartManager#createDuedatesChart() createDueDatesChart}
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

	public void createDuedatesChart() {

	}
}
