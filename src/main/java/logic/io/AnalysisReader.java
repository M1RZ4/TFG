package main.java.logic.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import main.java.logic.Analysis;
import main.java.logic.LanguageManager;

/**
 * Clase AnalysisReader encargada de la lectura de archivos de texto para
 * generar objetos de tipo {@link main.java.logic.Analysis Analysis} en función
 * de los parámetros de dichos ficheros. Implementa la interfaz {@link Reader}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class AnalysisReader implements Reader<Analysis> {

	/**
	 * Método que retorna un objeto de la clase Analysis al cargarlo del fichero de
	 * nombre fileName
	 * 
	 * @param fileName
	 *            nombre del fichero a leer
	 * @return análisis con los datos leídos
	 * @throws FileNotFoundException
	 *             si el fichero especificado no existe
	 */
	@Override
	public Analysis read(String fileName) throws FileNotFoundException {
		if (!fileName.toLowerCase().endsWith(".txt"))
			throw new IllegalArgumentException(LanguageManager.getInstance().getTexts().getString("error_extension"));
		else if (!new File(fileName).exists())
			throw new FileNotFoundException(LanguageManager.getInstance().getTexts().getString("error_not_found"));
		try (BufferedReader f = new BufferedReader(new FileReader(new File(fileName)))) {
			// Número de instancias
			int numberOfInstances = Integer.valueOf(f.readLine().split(":")[1]);
			if (numberOfInstances < 1)
				throw new IllegalStateException(LanguageManager.getInstance().getTexts().getString("error_analysis"));

			// Números de tareas
			String[] line = f.readLine().split(":")[1].split(",");
			int[] numberOfTasks = new int[line.length];
			for (int i = 0; i < line.length; i++) {
				int aux = Integer.valueOf(line[i]);
				if (aux < 1)
					throw new IllegalStateException(LanguageManager.getInstance().getTexts().getString("error_analysis"));
				numberOfTasks[i] = aux;
				}

			// Capacidades máximas
			line = f.readLine().split(":")[1].split(",");
			int[] maxCapacity = new int[line.length];
			for (int i = 0; i < line.length; i++) {
				int aux = Integer.valueOf(line[i]);
				if (aux < 3)
					throw new IllegalStateException(LanguageManager.getInstance().getTexts().getString("error_analysis"));
				maxCapacity[i] = aux;
			}

			if (numberOfTasks.length != maxCapacity.length)
				throw new IllegalStateException(LanguageManager.getInstance().getTexts().getString("error_analysis"));

			return new Analysis(numberOfInstances, numberOfTasks, maxCapacity);
		} catch (Exception e) {
			throw new IllegalStateException(LanguageManager.getInstance().getTexts().getString("error_analysis"));
		}
	}

}
