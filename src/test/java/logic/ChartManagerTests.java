package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dominio.Instancia;
import logic.enums.Rule;
import logic.instances.ScheduledInstance;
import logic.io.InstanceReader;
import logic.io.Reader;

public class ChartManagerTests {

	private static File resources;

	@BeforeAll
	public static void setUp() {
		resources = new File("src/test/resources");
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
	}

	@Test
	public void testLoadMainChart() {
		Reader<Instancia> r = new InstanceReader();
		try {
			Instancia i = r.read(resources + "/i9_4_1.txt");
			ChartManager c = new ChartManager();
			assertNull(c.getChart());
			c.loadMainChart(i, 10);
			assertNotNull(c.getChart());
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void testCreateDurationsChart() {
		Reader<Instancia> r = new InstanceReader();
		try {
			Instancia i = r.read(resources + "/i9_4_1.txt");
			ScheduledInstance s = new ScheduledInstance(i.getD().length, i, Rule.EDD, 0);
			ChartManager c = new ChartManager();
			JFreeChart j = c.createDurationsChart(s.getP(), s.getIds());
			assertNotNull(j);
			assertEquals(LanguageManager.getInstance().getTexts().getString("chart_durations"), j.getTitle().getText());
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void testCreateDueDatesChart() {
		Reader<Instancia> r = new InstanceReader();
		try {
			Instancia i = r.read(resources + "/i9_4_1.txt");
			ScheduledInstance s = new ScheduledInstance(i.getD().length, i, Rule.EDD, 0);
			ChartManager c = new ChartManager();
			JFreeChart j = c.createDueDatesChart(s.getD(), s.getIds());
			assertNotNull(j);
			assertEquals(LanguageManager.getInstance().getTexts().getString("chart_due_dates"), j.getTitle().getText());
		} catch (FileNotFoundException e) {
			fail();
		}
	}
}
