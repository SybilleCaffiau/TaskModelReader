package test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import TMRNF.Instance;
import TMRUI.Controleur;

public class InstanceTest {

	private Instance i=new Instance("chapitre","introduction à l'IHM", "2017-01-22 09:17:40");
	
	@Test
	public void testInstance() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetValeur() {
		String v=i.getValeur();
		assertEquals("valeur de l'instance", v, "introduction à l'IHM");
	}

	@Test
	public void testGetObjet() {
		String o=i.getObjet();
		assertEquals("objet de l'objet", o, "chapitre");
	}

	@Test
	public void testGetDate() {
		
		//assertEquals("valeur de la date", d.toString(), "2017-01-22 09:17:40");
	}

	@Test
	public void testSetValeur() {
		i.setValeur("les modèles de tâches");
		assertNotEquals("affecter valeur", i.getValeur(), "introduction à l'IHM");
	}

	@Test
	public void testSetObjet() {
		i.setObjet("leçons");
		assertNotEquals("affecter objet", i.getObjet(), "chapitre");
	}

	@Test
	public void testSetDate() {
		i.setValeur("2017-01-22 09:17:41");
		assertNotEquals("affecter date", i.getDate().toString(), "2017-01-22 09:17:40");
	}

}
