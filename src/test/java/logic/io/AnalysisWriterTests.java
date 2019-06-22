package logic.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import logic.Analysis;
import logic.InstanceManager;
import logic.LanguageManager;

class AnalysisWriterTests {

	private static File resources;

	@BeforeAll
	public static void setUp() {
		resources = new File("src/test/resources");
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
	}

	@Test
	public void testWrite() {
		File f = new File(resources + "/analysisWriterTestFile.xls");
		if (f.exists())
			f.delete();
		Analysis a = new Analysis(10, new int[] { 10, 15, 20 }, new int[] { 4, 6, 8 });
		InstanceManager i = new InstanceManager();
		i.setAnalysis(a);
		i.initializeInstanceGenerator(10, 4);
		i.createTasks();
		i.createIntervals();
		i.writeAnalysis(resources + "/analysisWriterTestFile");
		assertTrue(f.exists());
	}

}
