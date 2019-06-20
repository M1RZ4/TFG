package logic.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dominio.Instancia;
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
		Instancia i;
		try {
			i = new InstanceReader().read(resources + "/instanceReaderTestFile.txt");
			Writer w = new InstanceWriter(i);
			w.write(resources + "/instanceWriterTestFile.txt");
			assertTrue(f.exists());
		} catch (FileNotFoundException e) {
			fail();
		}
	}

}
