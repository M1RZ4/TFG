package logic.io;

import java.io.FileNotFoundException;

/**
 * Interfaz Reader encargada de la lectura de ficheros de diversos formatos por
 * parte de la aplicación
 * 
 * @author Mirza Ojeda Veira
 *
 * @param <T>
 */
public interface Reader<T> {

	/**
	 * Método que lee un objeto T de fichero, lo carga y lo retorna
	 * 
	 * @param fileName
	 *            nombre del fichero que contiene el objeto
	 * @return objeto cargado
	 * @throws FileNotFoundException
	 *             si no encuentra el archivo
	 */
	public T read(String fileName) throws FileNotFoundException;
}
