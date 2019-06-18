package test.java.instances;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dominio.Gestor;
import dominio.Instancia;
import main.java.logic.instances.ScheduledInstance;

public class ScheduledInstanceTests {

	private static File resources;

	@BeforeAll
	static void setUp() {
		resources = new File("src/test/resources");
	}

	@Test
	public void testInitialize() {
		Instancia i = Gestor.cargarInstancia(resources.getAbsolutePath() + "/i9_4_1.txt");
		ScheduledInstance s = new ScheduledInstance(i);
		// TODO comparar parámetros según la instancia cargada
	}
	
	@Test
	public void testInitializeWithSchedule() {
		Instancia i = Gestor.cargarInstancia(resources.getAbsolutePath() + "/i9_4_1.txt");
		//ScheduledInstance s = new ScheduledInstance(step, i, rule, g);
		// TODO lo mismo que la anterior pero con planificación (probar con todas las reglas)
	}
}
