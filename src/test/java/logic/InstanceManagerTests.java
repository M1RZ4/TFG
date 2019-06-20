package logic;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InstanceManagerTests {

	private static File resources;

	@BeforeAll
	public static void setUp() {
		resources = new File("src/test/resources");
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
	}

	@Test
	public void testReadInstance() {
		InstanceManager i = new InstanceManager();
	}
}
