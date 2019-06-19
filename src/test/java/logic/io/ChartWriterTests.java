package logic.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import logic.InstanceManager;
import logic.LanguageManager;

public class ChartWriterTests {

	private static File resources;

	@BeforeAll
	static void setUp() {
		resources = new File("src/test/resources");
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
	}

	@Test
	void testWrite() throws FileNotFoundException {
		File f = new File(resources + "/chartWriterTestFile.pdf");
		if (f.exists())
			f.delete();
		InstanceManager i = new InstanceManager();
		i.readInstance(resources + "/i9_4_1.txt");
		i.loadMainChart(10);
		Writer w = new ChartWriter(i.getChart());
		w.write(resources + "/chartWriterTestFile.pdf");
		assertTrue(f.exists());
	}
}
