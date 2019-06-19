package logic.instances;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dominio.Gestor;
import dominio.Instancia;
import logic.enums.Rule;

public class ScheduledInstanceTests {

	private static File resources;
	private static Instancia i;

	@BeforeAll
	static void setUp() {
		resources = new File("src/test/resources");
		i = Gestor.cargarInstancia(resources + "/i9_4_1.txt");
	}

	@Test
	public void testInitialize() {
		ScheduledInstance s = new ScheduledInstance(i);
		assertNull(s.getD());
		assertNull(s.getP());
		assertEquals(i.getNombre(), s.getNombre());
		assertEquals(i.getPerfilMaquina(), s.getPerfilMaquina());
		assertNull(s.getIds());
		assertNull(s.getStartTimes());
	}

	@Test
	public void testInitializeWithSchedule() {
		// EDD
		ScheduledInstance s = new ScheduledInstance(i.getD().length, i, Rule.EDD, 0);
		assertEquals(i.getNombre(), s.getNombre());
		assertEquals(i.getPerfilMaquina(), s.getPerfilMaquina());
		double[] durations = new double[] { 51, 43, 56, 63, 51, 32, 44, 39, 41 };
		double[] dueDates = new double[] { 62, 67, 107, 112, 132, 140, 154, 179, 183 };
		int[] ids = new int[] { 5, 2, 6, 7, 0, 4, 8, 3, 1 };
		double[] startTimes = new double[] { 0, 51, 81, 94, 137, 157, 171, 188, 189 };
		for (int j = 0; j < ids.length; j++) {
			assertEquals(dueDates[j], s.getD()[j]);
			assertEquals(durations[j], s.getP()[j]);
			assertEquals(ids[j], s.getIds()[j]);
			assertEquals(startTimes[j], s.getStartTimes()[j]);
		}

		// SPT
		s = new ScheduledInstance(i.getD().length, i, Rule.SPT, 0);
		assertEquals(i.getNombre(), s.getNombre());
		assertEquals(i.getPerfilMaquina(), s.getPerfilMaquina());
		durations = new double[] { 32, 39, 41, 43, 44, 51, 51, 56, 63 };
		dueDates = new double[] { 140, 179, 183, 67, 154, 62, 132, 107, 112 };
		ids = new int[] { 4, 3, 1, 2, 8, 5, 0, 6, 7 };
		startTimes = new double[] { 0, 32, 71, 81, 112, 124, 156, 171, 175 };
		for (int j = 0; j < ids.length; j++) {
			assertEquals(dueDates[j], s.getD()[j]);
			assertEquals(durations[j], s.getP()[j]);
			assertEquals(ids[j], s.getIds()[j]);
			assertEquals(startTimes[j], s.getStartTimes()[j]);
		}

		// ATC
		s = new ScheduledInstance(i.getD().length, i, Rule.ATC, 0.25);
		assertEquals(i.getNombre(), s.getNombre());
		assertEquals(i.getPerfilMaquina(), s.getPerfilMaquina());
		durations = new double[] { 51, 43, 51, 56, 32, 39, 41, 44, 63 };
		dueDates = new double[] { 62, 67, 132, 107, 140, 179, 183, 154, 112 };
		ids = new int[] { 5, 2, 0, 6, 4, 3, 1, 8, 7 };
		startTimes = new double[] { 0, 51, 81, 94, 132, 150, 164, 171, 189 };
		for (int j = 0; j < ids.length; j++) {
			assertEquals(dueDates[j], s.getD()[j]);
			assertEquals(durations[j], s.getP()[j]);
			assertEquals(ids[j], s.getIds()[j]);
			assertEquals(startTimes[j], s.getStartTimes()[j]);
		}

	}

	@Test
	public void testSetID() {
		ScheduledInstance s = new ScheduledInstance(i);
		assertNull(s.getIds());
		s.setID(2);
		System.out.println(s.getIds()[0] == 2);
		assertEquals(2, s.getIds()[0]);
		s.setID(1);
		assertEquals(2, s.getIds()[0]);
		assertEquals(1, s.getIds()[1]);
	}

	@Test
	public void testSetD() {
		ScheduledInstance s = new ScheduledInstance(i);
		assertNull(s.getD());
		s.setD(2);
		System.out.println(s.getD()[0] == 2);
		assertEquals(2, s.getD()[0]);
		s.setD(1);
		assertEquals(2, s.getD()[0]);
		assertEquals(1, s.getD()[1]);
	}

	@Test
	public void testSetP() {
		ScheduledInstance s = new ScheduledInstance(i);
		assertNull(s.getP());
		s.setP(2);
		System.out.println(s.getP()[0] == 2);
		assertEquals(2, s.getP()[0]);
		s.setP(1);
		assertEquals(2, s.getP()[0]);
		assertEquals(1, s.getP()[1]);
	}

	@Test
	public void testSetSti() {
		ScheduledInstance s = new ScheduledInstance(i);
		assertNull(s.getStartTimes());
		s.setSti(2);
		System.out.println(s.getStartTimes()[0] == 2);
		assertEquals(2, s.getStartTimes()[0]);
		s.setSti(1);
		assertEquals(2, s.getStartTimes()[0]);
		assertEquals(1, s.getStartTimes()[1]);
	}
}
