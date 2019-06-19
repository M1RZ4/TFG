package logic.instances;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import dominio.Intervalo;
import logic.instances.AnalysisInstance;

public class AnalysisInstanceTests {

	@Test
	public void testinItialize() {
		AnalysisInstance i = new AnalysisInstance(new double[] { 1, 2, 3 }, new double[] { 1, 2, 3 });
		for (int j = 0; j < 3; j++) {
			assertEquals(j + 1, i.getD()[j]);
			assertEquals(j + 1, i.getP()[j]);
		}
		assertEquals("", i.getNombre());
		assertNull(i.getPerfilMaquina());
	}

	@Test
	public void testAdd() {
		AnalysisInstance i = new AnalysisInstance(new double[0], new double[0]);
		assertEquals(null, i.getPerfilMaquina());
		Intervalo i1 = new Intervalo(1, 0, 1);
		i.add(i1);
		List<Intervalo> list = i.getPerfilMaquina();
		assertNotNull(list);
		assertEquals(i1, list.get(0));
		Intervalo i2 = new Intervalo(2, 1, 2);
		i.add(i2);
		assertEquals(i2, list.get(1));
	}

	@Test
	public void testSetName() {
		AnalysisInstance i = new AnalysisInstance(new double[0], new double[0]);
		assertEquals("", i.getNombre());
		i.setName("test");
		assertEquals("test", i.getNombre());
	}
}
