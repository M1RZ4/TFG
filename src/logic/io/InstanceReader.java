package logic.io;

import dominio.Gestor;
import dominio.Instancia;

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
	 * Método que retorna un objeto de la clase Instancia al cargarlo
	 * del fichero de nombre fileName
	 * 
	 * @param fileName
	 *            nombre del fichero a leer
	 * @return instancia con los datos leídos
	 */
	public Instancia readInstance(String fileName) {
		return Gestor.cargarInstancia(fileName);
	}

}
