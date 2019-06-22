package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import dominio.Instancia;

public class InstanceGeneratorTests {

	@Test
	public void testCreateTasks() {
		InstanceManager i = new InstanceManager();
		i.initializeInstanceGenerator(15, 5);
		i.createTasks();
		assertEquals(15, i.getDueDates().length);
		assertEquals(15, i.getDurations().length);
	}

	@Test
	public void testCreateIntervals() {
		InstanceManager i = new InstanceManager();
		i.initializeInstanceGenerator(15, 5);
		i.createTasks();
		i.createIntervals();
		assertEquals(15, i.getDueDates().length);
		assertEquals(15, i.getDurations().length);
		assertEquals(i.getMaxInterval(), i.getIntervalCapacities().size());
		assertEquals(i.getMaxInterval(), i.getIntervalDurations().size());
	}

	@Test
	public void testGenerateInstances() {
		InstanceManager i = new InstanceManager();
		i.initializeInstanceGenerator(10, 4);
		i.createTasks();
		i.createIntervals();
		i.setAnalysis(new Analysis(10, new int[] { 10, 15, 20 }, new int[] { 4, 6, 8 }));
		List<List<Instancia>> list = i.generateInstances();
		i.saveInstances(list);
		assertEquals(3, list.size());
		assertEquals(10, list.get(0).size());
	}
}
