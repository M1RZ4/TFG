package main.java.logic.io;

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

	public T read(String fileName) throws FileNotFoundException;
}
