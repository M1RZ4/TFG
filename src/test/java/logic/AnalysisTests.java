package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AnalysisTests {

	@Test
	void testInitialize() {
		int numberOfInstances = 100;
		int[] numberOfTasks = new int[] { 10, 15, 20 };
		int[] maxCapacity = new int[] { 4, 6, 8 };
		Analysis a = new Analysis(numberOfInstances, numberOfTasks, maxCapacity);
		assertEquals(numberOfInstances, a.getNumberOfInstances());
		for (int i = 0; i < maxCapacity.length; i++) {
			assertEquals(numberOfTasks[i], a.getNumberOfTasks()[i]);
			assertEquals(maxCapacity[i], a.getMaxCapacity()[i]);
		}
	}

}
