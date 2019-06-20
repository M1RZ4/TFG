package logic;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.Test;

import logic.LanguageManager;

class LanguageManagerTests {

	@Test
	public void testGetTexts() {
		setText("en");
		assertEquals("Graphic Visualizer & Instance generator", getText("menu_title"));
		assertEquals("Separation between marks:", getText("label_separation_between_marks"));
		assertEquals("Instance generation successful", getText("message_instance_generator"));
		setText("es");
		assertEquals("Visualizador gráfico & Generador de instancias", getText("menu_title"));
		assertEquals("Separación entre marcas:", getText("label_separation_between_marks"));
		assertEquals("Generación de instancia exitosa", getText("message_instance_generator"));
	}

	private String getText(String key) {
		return LanguageManager.getInstance().getTexts().getString(key);
	}

	private void setText(String locale) {
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale(locale)));
	}

}
