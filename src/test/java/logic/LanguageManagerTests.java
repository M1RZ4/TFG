package logic;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.Test;

import logic.LanguageManager;

class LanguageManagerTests {

	@Test
	public void testGetTexts() {
		InstanceManager i = new InstanceManager();
		setText("en");
		assertEquals("Graphic Visualizer & Instance generator", i.getText("menu_title"));
		assertEquals("Separation between marks:", i.getText("label_separation_between_marks"));
		assertEquals("Instance generation successful", i.getText("message_instance_generator"));
		assertEquals('F', i.getMnemonic("mnemonic_file"));
		assertEquals('L', i.getMnemonic("mnemonic_load"));
		assertEquals('S', i.getMnemonic("mnemonic_save"));
		setText("es");
		assertEquals("Visualizador gráfico & Generador de instancias", i.getText("menu_title"));
		assertEquals("Separación entre marcas:", i.getText("label_separation_between_marks"));
		assertEquals("Generación de instancia exitosa", i.getText("message_instance_generator"));
		assertEquals('A', i.getMnemonic("mnemonic_file"));
		assertEquals('C', i.getMnemonic("mnemonic_load"));
		assertEquals('G', i.getMnemonic("mnemonic_save"));
	}

	private void setText(String locale) {
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale(locale)));
	}

}
