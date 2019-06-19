package logic;

import java.util.ResourceBundle;

/**
 * Clase LanguageManager que gestiona la internacionalización de la aplicación.
 * Sigue un patrón Singleton
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class LanguageManager {

	private static LanguageManager instance;
	private ResourceBundle texts;

	private LanguageManager() {
	}

	public static LanguageManager getInstance() {
		if (instance == null)
			instance = new LanguageManager();
		return instance;
	}

	public ResourceBundle getTexts() {
		return texts;
	}

	public void setTexts(ResourceBundle texts) {
		this.texts = texts;
	}

}
