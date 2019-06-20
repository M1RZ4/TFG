package logic.io;

/**
 * Interfaz Writer encargada de la escritura de ficheros de diversos formatos
 * por parte de la aplicación
 * 
 * @author Mirza Ojeda Veira
 *
 */
public interface Writer {

	/**
	 * Método que crea un fichero con el nombre dado
	 * 
	 * @param fileName
	 *            nombre del fichero a escribir
	 */
	public void write(String fileName);

}
