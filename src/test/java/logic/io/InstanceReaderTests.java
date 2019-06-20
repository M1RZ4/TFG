package logic.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dominio.Instancia;
import logic.LanguageManager;

class InstanceReaderTests {
	private static File resources;

	@BeforeAll
	public static void setUp() {
		resources = new File("src/test/resources");
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
	}

	@Test
	public void testRead() {
		// Archivo sin extensión
		try {
			readInstance("fileWithoutExtension");
		} catch (Exception e) {
			assertEquals(LanguageManager.getInstance().getTexts().getString("error_extension"), e.getMessage());
		}
		// Archivo sin extensión .txt
		try {
			readInstance("fileWithoutTxtExtension.pdf");
		} catch (Exception e) {
			assertEquals(LanguageManager.getInstance().getTexts().getString("error_extension"), e.getMessage());
		}
		// Archivo inexistente
		try {
			readInstance("nonExistentFile.txt");
		} catch (Exception e) {
			assertEquals(LanguageManager.getInstance().getTexts().getString("error_not_found"), e.getMessage());
		}
		// Archivo correcto
		try {
			Instancia i = readInstance("instanceReaderTestFile.txt");
			assertEquals(resources + "/instanceReaderTestFile", i.getNombre());
			assertEquals(50, i.getD()[0]);
			assertEquals(100, i.getD()[1]);
			assertEquals(150, i.getD()[2]);
			assertEquals(40, i.getP()[0]);
			assertEquals(80, i.getP()[1]);
			assertEquals(120, i.getP()[2]);
			assertEquals(1, i.getPerfilMaquina().get(0).getCap());
			assertEquals(0, i.getPerfilMaquina().get(0).getInicio());
			assertEquals(50, i.getPerfilMaquina().get(0).getFin());
			assertEquals(2, i.getPerfilMaquina().get(1).getCap());
			assertEquals(50, i.getPerfilMaquina().get(1).getInicio());
			assertEquals(100, i.getPerfilMaquina().get(1).getFin());
			assertEquals(1, i.getPerfilMaquina().get(2).getCap());
			assertEquals(100, i.getPerfilMaquina().get(2).getInicio());
			assertEquals(300000, i.getPerfilMaquina().get(2).getFin());
		} catch (Exception e) {
			fail();
		}
	}

	private Instancia readInstance(String fileName) throws FileNotFoundException {
		InstanceReader i = new InstanceReader();
		return i.read(resources + "/" + fileName);
	}

}
