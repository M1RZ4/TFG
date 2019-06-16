package main.java.logic.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import dominio.Gestor;
import dominio.Instancia;
import dominio.Planificacion;
import main.java.logic.Analysis;
import main.java.logic.LanguageManager;
import main.java.logic.enums.Rule;

/**
 * Clase AnalysysWriter encargada de generar ficheros excel con los resultados
 * del análisis experimental generado en función de los parámetros dados.
 * Implementa la interfaz {@link Writer}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class AnalysisWriter implements Writer {

	private Analysis analysis;
	private List<List<Instancia>> instances;

	public AnalysisWriter(Analysis analysis, List<List<Instancia>> instances) {
		this.analysis = analysis;
		this.instances = instances;
	}

	/**
	 * Método implementado de la interfaz {@link Writer} que crea un archivo excel
	 * de nombre fileName con los resultados del análisis experimental
	 * 
	 * @param fileName
	 *            nombre del fichero
	 */
	@Override
	public void write(String fileName) {
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
			HSSFSheet sheet = workbook
					.createSheet(LanguageManager.getInstance().getTexts().getString("menu_experimental_study"));
			HSSFRow rowhead = sheet.createRow(0);
			rowhead.createCell(0).setCellValue("MC");
			rowhead.createCell(1).setCellValue("n");
			rowhead.createCell(2).setCellValue("EDD");
			rowhead.createCell(3).setCellValue("SPT");
			rowhead.createCell(4).setCellValue("ATC-0.25");
			rowhead.createCell(5).setCellValue("ATC-0.50");
			rowhead.createCell(6).setCellValue("ATC-0.75");
			rowhead.createCell(7).setCellValue("ATC-1.00");
			fillRows(sheet);
			try (FileOutputStream f = new FileOutputStream(fileName + ".xls")) {
				workbook.write(f);
			} catch (IOException e) {
				e.printStackTrace(); // TODO error al crear excel
			}
		} catch (IOException e) {
			e.printStackTrace(); // TODO error al crear excel
		}
	}

	/**
	 * Método auxiliar que cumplimenta el excel con los datos del tardiness para
	 * cada una de los parámetros
	 */
	private void fillRows(HSSFSheet sheet) {
		double[] averages;
		for (int i = 0; i < analysis.getNumberOfTasks().length; i++) {
			HSSFRow row = sheet.createRow(i + 1);
			// MC
			row.createCell(0).setCellValue(analysis.getMaxCapacity()[i]);

			// n
			row.createCell(1).setCellValue(analysis.getNumberOfTasks()[i]);

			// EDD
			averages = calculateAverage(Rule.EDD, 0);
			row.createCell(2).setCellValue(averages[i]);

			// SPT
			averages = calculateAverage(Rule.SPT, 0);
			row.createCell(3).setCellValue(averages[i]);

			// ATC - 0.25
			averages = calculateAverage(Rule.ATC, 0.25);
			row.createCell(4).setCellValue(averages[i]);

			// ATC - 0.50
			averages = calculateAverage(Rule.ATC, 0.5);
			row.createCell(5).setCellValue(averages[i]);

			// ATC - 0.75
			averages = calculateAverage(Rule.ATC, 0.75);
			row.createCell(6).setCellValue(averages[i]);

			// ATC - 1.00
			averages = calculateAverage(Rule.ATC, 1);
			row.createCell(7).setCellValue(averages[i]);
		}
	}

	/**
	 * Método auxiliar que calcula las medias de tardiness de las planificaciones de
	 * un conjunto de instancias generados con un número de tareas y capacidad
	 * máxima fijos en función de la regla de prioridad escogida
	 * 
	 * @param r
	 *            regla de prioridad
	 * @param g
	 *            parámetro g para ATC
	 * @return lista de medias de tardiness para los parámetros dados
	 */
	private double[] calculateAverage(Rule r, double g) {
		double[] averages = new double[analysis.getNumberOfTasks().length];
		for (int i = 0; i < analysis.getNumberOfTasks().length; i++) { // len(t) = len(c)
			List<Double> tardinessList = new ArrayList<Double>();
			for (int j = 0; j < instances.get(i).size(); j++) { // n
				Instancia x = instances.get(i).get(j);
				Planificacion p = null;
				switch (r) {
				case EDD:
					p = Gestor.planificaEDD(x);
					break;
				case SPT:
					p = Gestor.planificaSPT(x);
					break;
				case ATC:
					p = Gestor.planificaATC(g, x);
					break;
				default:
					break;
				}
				tardinessList.add(calculateTardiness(x, p));
			}
			double average = 0;
			for (double d : tardinessList)
				average += d;
			average = average / tardinessList.size(); // Tardiness medio con r para n instancias con t y c dados
			averages[i] = average;
		}
		return averages;
	}

	/**
	 * Método auxiliar que calcula el tardiness en base a una instancia y su
	 * planificacion
	 * 
	 * @param i
	 *            instancia
	 * @param p
	 *            planificacion
	 * @return tardiness
	 */
	private double calculateTardiness(Instancia i, Planificacion p) {
		double tardiness = 0;
		int[] startTimes = p.getSti();
		double[] dueDates = i.getD();
		double[] durations = i.getP();

		for (int j = 0; j < startTimes.length; j++)
			tardiness += Math.max(0, startTimes[j] + durations[j] - dueDates[j]);

		return tardiness;
	}

}
