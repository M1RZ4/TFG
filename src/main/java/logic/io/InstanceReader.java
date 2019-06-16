package main.java.logic.io;

import java.io.File;
import java.io.FileNotFoundException;

import dominio.Gestor;
import dominio.Instancia;
import main.java.logic.LanguageManager;

/**
 * Clase InstanceReader encargada de la lectura de archivos de texto para
 * generar objetos de tipo Instancia en función de los parámetros de dichos
 * ficheros. Impelmenta la interfaz {@link Reader}
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class InstanceReader implements Reader<Instancia> {

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
	@Override
	public Instancia read(String fileName) throws FileNotFoundException {
		if (!fileName.toLowerCase().endsWith(".txt"))
			throw new IllegalArgumentException(LanguageManager.getInstance().getTexts().getString("error_extension"));
		else if (!new File(fileName).exists())
			throw new FileNotFoundException(LanguageManager.getInstance().getTexts().getString("error_not_found"));
		return Gestor.cargarInstancia(fileName);
	}

}
