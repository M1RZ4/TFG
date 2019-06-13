package logic.io;

import java.io.FileNotFoundException;

import dominio.Gestor;
import dominio.Instancia;
import logic.LanguageManager;

/**
 * Clase InstanceReader encargada de la lectura de archivos de texto para
 * generar objetos de tipo Instancia en función de los parámetros de dichos
 * ficheros
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceReader {

	/**
	 * Método que retorna un objeto de la clase Instancia al cargarlo del fichero de
	 * nombre fileName
	 * 
	 * @param fileName
	 *            nombre del fichero a leer
	 * @return instancia con los datos leídos
	 * @throws FileNotFoundException
	 *             si el fichero especificado no existe
	 */
	public Instancia readInstance(String fileName) throws FileNotFoundException {
		if (!fileName.endsWith(".txt"))
			throw new IllegalArgumentException(LanguageManager.getInstance().getTexts().getString("error_file_extension"));
		return Gestor.cargarInstancia(fileName);
	}

}
