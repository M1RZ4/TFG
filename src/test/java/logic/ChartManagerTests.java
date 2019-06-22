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

import logic.enums.Rule;
import logic.instances.ScheduledInstance;

public class ChartManagerTests {

	private static File resources;

	@BeforeAll
	public static void setUp() {
		resources = new File("src/test/resources");
		LanguageManager.getInstance().setTexts(ResourceBundle.getBundle("texts", new Locale("en")));
	}

	@Test
	public void testLoadMainChart() {
		InstanceManager i = new InstanceManager();
		try {
			i.readInstance(resources + "/i9_4_1.txt");
			assertNull(i.getChart());
			i.loadMainChart(10);
			assertNotNull(i.getChart());
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void testCreateDurationsChart() {
		InstanceManager i = new InstanceManager();
		try {
			i.readInstance(resources + "/i9_4_1.txt");
			ScheduledInstance s = new ScheduledInstance(i.getP().length, i.getInstance(), Rule.EDD, 0);
			JFreeChart j = i.createDurationsChart(s.getP(), s.getIds());
			assertNotNull(j);
			assertEquals(i.getText("chart_durations"), j.getTitle().getText());
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void testCreateDueDatesChart() {
		InstanceManager i = new InstanceManager();
		try {
			i.readInstance(resources + "/i9_4_1.txt");
			ScheduledInstance s = new ScheduledInstance(i.getD().length, i.getInstance(), Rule.EDD, 0);
			JFreeChart j = i.createDueDatesChart(s.getD(), s.getIds());
			assertNotNull(j);
			assertEquals(i.getText("chart_due_dates"), j.getTitle().getText());
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void setMainChartTest() {
		InstanceManager i = new InstanceManager();
		try {
			i.readInstance(resources + "/i9_4_1.txt");
			assertNull(i.getChart());
			i.setMainChart(i.getD().length, Rule.EDD, 0, 10);
			assertNotNull(i.getChart());
			
			i = new InstanceManager();
			i.readInstance(resources + "/i9_4_1.txt");
			assertNull(i.getChart());
			ScheduledInstance s = new ScheduledInstance(i.getD().length, i.getInstance(), Rule.EDD, 0);
			i.setMainChart(s.getD().length, s, 10);
			assertNotNull(i.getChart());
		} catch (FileNotFoundException e) {
			fail();
		}
	}
	
	
}
