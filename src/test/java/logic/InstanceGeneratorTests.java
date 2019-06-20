package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import dominio.Instancia;

public class InstanceGeneratorTests {

	@Test
	public void testCreateTasks() {
		InstanceGenerator i = new InstanceGenerator(15, 5);
		i.createTasks();
		assertEquals(15, i.getDueDates().length);
		assertEquals(15, i.getDurations().length);
	}

	@Test
	public void testCreateIntervals() {
		InstanceGenerator i = new InstanceGenerator(15, 5);
		i.createTasks();
		i.createIntervals();
		assertEquals(15, i.getDueDates().length);
		assertEquals(15, i.getDurations().length);
		assertEquals(i.getMaxInterval(), i.getIntervalCapacities().size());
		assertEquals(i.getMaxInterval(), i.getIntervalDurations().size());
	}

	@Test
	public void testGenerateInstances() {
		InstanceGenerator i = new InstanceGenerator(10, 4);
		i.createTasks();
		i.createIntervals();
		List<List<Instancia>> list = i
				.generateInstances(new Analysis(10, new int[] { 10, 15, 20 }, new int[] { 4, 6, 8 }));
		assertEquals(3, list.size());
		assertEquals(10, list.get(0).size());
	}
}
