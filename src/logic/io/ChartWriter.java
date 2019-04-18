package logic.io;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;

import org.jfree.chart.JFreeChart;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Clase ChartWriter encargada de generar los ficheros PDF con los gráficos
 * generados con {@link logic.ChartManager ChartManager}. Implementa la interfaz
 * {@link Writer}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class ChartWriter implements Writer {

	private JFreeChart chart;

	/**
	 * Método auxiliar que modifica el valor de la variable chart de la clase
	 * 
	 * @param chart
	 *            Gráfico a asignar a la variable chart de la clase
	 */
	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	/**
	 * Método implementado de la interfaz {@link Writer} que crea un archivo PDF de
	 * nombre fileName con el gráfico chart
	 * 
	 * @param fileName
	 *            nombre del fichero PDF a crear
	 */
	@Override
	public void write(String fileName) {
		PdfWriter writer = null;
		Document document = new Document();
		int width = 600;
		int height = 400;
		document.setPageSize(new Rectangle(0, 0, width, height));
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();
			PdfContentByte contentByte = writer.getDirectContent();
			PdfTemplate template = contentByte.createTemplate(width, height);
			Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
			Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
			chart.draw(graphics2d, rectangle2d);
			graphics2d.dispose();
			contentByte.addTemplate(template, 0, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		document.close();
	}

}
