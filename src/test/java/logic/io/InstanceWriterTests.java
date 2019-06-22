package logic.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import logic.InstanceManager;
import logic.LanguageManager;

class InstanceWriterTests {

	private static File resources;

	@BeforeAll
	public static void setUp() {
		resources = new File("src/test/resources");
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
	}

	@Test
	public void testWrite() {
		File f = new File(resources + "/instanceWriterTestFile.txt");
		if (f.exists())
			f.delete();
		InstanceManager i = new InstanceManager();
		i.initializeInstanceGenerator(15, 5);
		i.createTasks();
		i.createIntervals();
		i.writeInstance(resources + "/instanceWriterTestFile.txt");
		assertTrue(f.exists());
	}

}
