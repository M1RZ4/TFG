package logic.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import logic.Analysis;
import logic.InstanceManager;
import logic.LanguageManager;

class AnalysisReaderTests {

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
			readAnalysis("fileWithoutExtension");
		} catch (Exception e) {
			assertEquals(LanguageManager.getInstance().getTexts().getString("error_extension"), e.getMessage());
		}
		// Archivo sin extensión .txt
		try {
			readAnalysis("fileWithoutTxtExtension.pdf");
		} catch (Exception e) {
			assertEquals(LanguageManager.getInstance().getTexts().getString("error_extension"), e.getMessage());
		}
		// Archivo inexistente
		try {
			readAnalysis("nonExistentFile.txt");
		} catch (Exception e) {
			assertEquals(LanguageManager.getInstance().getTexts().getString("error_not_found"), e.getMessage());
		}
		// Archivo con formato incorrecto
		try {
			readAnalysis("i9_4_1.txt");
		} catch (Exception e) {
			assertEquals(LanguageManager.getInstance().getTexts().getString("error_analysis"), e.getMessage());
		}
		// Archivo correcto
		try {
			Analysis a = readAnalysis("analysisReaderTestFile.txt");
			assertEquals(100, a.getNumberOfInstances());
			assertEquals(15, a.getNumberOfTasks()[0]);
			assertEquals(30, a.getNumberOfTasks()[1]);
			assertEquals(45, a.getNumberOfTasks()[2]);
			assertEquals(60, a.getNumberOfTasks()[3]);
			assertEquals(4, a.getMaxCapacity()[0]);
			assertEquals(6, a.getMaxCapacity()[1]);
			assertEquals(8, a.getMaxCapacity()[2]);
			assertEquals(10, a.getMaxCapacity()[3]);
		} catch (Exception e) {
			fail();
		}

	}

	private Analysis readAnalysis(String fileName) throws FileNotFoundException {
		InstanceManager i = new InstanceManager();
		i.readAnalysis(resources + "/" + fileName);
		return i.getAnalysis();
	}

}
