package main.java.logic.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

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
			sheet.setDefaultRowHeight((short) 600);
			HSSFRow rowhead = sheet.createRow(0);
			HSSFCellStyle style = workbook.createCellStyle();
			style = setHeaderStyle(style, workbook);

			// MC
			HSSFCell cell = rowhead.createCell(0);
			cell.setCellValue("MC");
			cell.setCellStyle(style);
			sheet.setColumnWidth(0, 4000);

			// n
			cell = rowhead.createCell(1);
			cell.setCellValue("n");
			cell.setCellStyle(style);
			sheet.setColumnWidth(1, 4000);

			// EDD
			cell = rowhead.createCell(2);
			cell.setCellValue("EDD");
			cell.setCellStyle(style);
			sheet.setColumnWidth(2, 4000);

			// SPT
			cell = rowhead.createCell(3);
			cell.setCellValue("SPT");
			cell.setCellStyle(style);
			sheet.setColumnWidth(3, 4000);

			// ATC - 0.25
			cell = rowhead.createCell(4);
			cell.setCellValue("ATC-0.25");
			cell.setCellStyle(style);
			sheet.setColumnWidth(4, 4000);

			// ATC - 0.50
			cell = rowhead.createCell(5);
			cell.setCellValue("ATC-0.50");
			cell.setCellStyle(style);
			sheet.setColumnWidth(5, 4000);

			// ATC - 0.75
			cell = rowhead.createCell(6);
			cell.setCellValue("ATC-0.75");
			cell.setCellStyle(style);
			sheet.setColumnWidth(6, 4000);

			// ATC - 1.00
			cell = rowhead.createCell(7);
			cell.setCellValue("ATC-1.00");
			cell.setCellStyle(style);
			sheet.setColumnWidth(7, 4000);

			fillRows(sheet, workbook);

			try (FileOutputStream f = new FileOutputStream(fileName + ".xls")) {
				workbook.write(f);
			} catch (IOException e) {
				throw new IllegalStateException(LanguageManager.getInstance().getTexts().getString("error_excel"));
			}
		} catch (IOException e) {
			throw new IllegalStateException(LanguageManager.getInstance().getTexts().getString("error_excel"));
		}
	}

	/**
	 * Método privado que modifica el estilo de las celdas de cabecera del excel:
	 * letra blanca en negrita sobre fondo verde con texto centrado en celda con
	 * bordes dobles
	 * 
	 * @param style
	 *            estilo a modificar
	 * @param wb
	 *            workbook del xls
	 * @return style modificado
	 */
	private HSSFCellStyle setHeaderStyle(HSSFCellStyle style, HSSFWorkbook wb) {
		// Situación del texto
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		// Color de fondo
		style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Bordes
		style.setBorderBottom(BorderStyle.DOUBLE);
		style.setBorderTop(BorderStyle.DOUBLE);
		style.setBorderLeft(BorderStyle.DOUBLE);
		style.setBorderRight(BorderStyle.DOUBLE);

		// Fuente del texto
		HSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);

		return style;
	}

	/**
	 * Método privado que modifica el estilo de las celdas a rellenar del excel:
	 * texto centrado en las celdas con bordes dobles
	 * 
	 * @param style
	 *            estilo a modificar
	 * @param wb
	 *            workbook del xls
	 * @return style modificado
	 */
	private HSSFCellStyle setCellStyle(HSSFCellStyle style, HSSFWorkbook wb) {
		// Situación del texto
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		// Bordes
		style.setBorderBottom(BorderStyle.DOUBLE);
		style.setBorderTop(BorderStyle.DOUBLE);
		style.setBorderLeft(BorderStyle.DOUBLE);
		style.setBorderRight(BorderStyle.DOUBLE);

		// Fuente del texto
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);

		return style;
	}

	/**
	 * Método auxiliar que cumplimenta el excel con los datos del tardiness para
	 * cada una de los parámetros
	 */
	private void fillRows(HSSFSheet sheet, HSSFWorkbook wb) {
		double[] averages;

		HSSFCellStyle style = wb.createCellStyle();
		style = setCellStyle(style, wb);

		for (int i = 0; i < analysis.getNumberOfTasks().length; i++) {
			HSSFRow row = sheet.createRow(i + 1);

			// MC
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(analysis.getMaxCapacity()[i]);
			cell.setCellStyle(style);

			// n
			cell = row.createCell(1);
			cell.setCellValue(analysis.getNumberOfTasks()[i]);
			cell.setCellStyle(style);

			// EDD
			averages = calculateAverage(Rule.EDD, 0);
			cell = row.createCell(2);
			cell.setCellValue(averages[i]);
			cell.setCellStyle(style);

			// SPT
			averages = calculateAverage(Rule.SPT, 0);
			cell = row.createCell(3);
			cell.setCellValue(averages[i]);
			cell.setCellStyle(style);

			// ATC - 0.25
			averages = calculateAverage(Rule.ATC, 0.25);
			cell = row.createCell(4);
			cell.setCellValue(averages[i]);
			cell.setCellStyle(style);

			// ATC - 0.50
			averages = calculateAverage(Rule.ATC, 0.5);
			cell = row.createCell(5);
			cell.setCellValue(averages[i]);
			cell.setCellStyle(style);

			// ATC - 0.75
			averages = calculateAverage(Rule.ATC, 0.75);
			cell = row.createCell(6);
			cell.setCellValue(averages[i]);
			cell.setCellStyle(style);

			// ATC - 1.00
			averages = calculateAverage(Rule.ATC, 1);
			cell = row.createCell(7);
			cell.setCellValue(averages[i]);
			cell.setCellStyle(style);
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
