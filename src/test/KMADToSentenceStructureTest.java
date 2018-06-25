/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import TMRNF.KMADToSentenceStructure;

import org.junit.Test;

/**
 * @author sybillecaffiau
 *
 */
public class KMADToSentenceStructureTest {

	//objet testé et donc utilisé dans toutes les autrs méthodes
	KMADToSentenceStructure transfo = new KMADToSentenceStructure("testTache.xml");
	KMADToSentenceStructure transfo2 = new KMADToSentenceStructure("testTache2.xml");
	
	//transfo.ecritureMdT();
	
	

	/**
	 * Test method for {@link TMRNF.KMADToSentenceStructure#getNomFichierKMAD()}.
	 */
	@Test
	public void testGetNomFichierKMAD() {
		assertEquals("nom fichier correct", transfo.getNomFichierKMAD(), "testTache.xml");
	}

	/**
	 * Test method for {@link TMRNF.KMADToSentenceStructure#afficheDocumentSource()}.
	 */
	@Test
	public void testAfficheDocumentSource() {
		//non testé
	}

	/**
	 * Test method for {@link TMRNF.KMADToSentenceStructure#getTextMdT()}.
	 */
	@Test
	public void testGetTextMdT() {
		assertEquals("avant traitement", transfo.getTextMdT(), "");
	}

	/**
	 * Test method for {@link TMRNF.KMADToSentenceStructure#ecritureMdT()}.
	 */
	@Test
	public void testEcritureMdT() {
		String text ="Pour gérer ses photos numériques on ajoute une photo, on regarde un album, et en même temps on organise ses photos."
				+"\n"+
"Ajouter une photo, regarder un album et organiser ses photos peuvent être répété."
+"\n"+
"Pour ajouter une photo l'utilisateur sélectionne une photo et le système place la photo dans les photos non triées."
+"\n"+
"Pour regarder un album l'utilisateur sélectionne un album et l'utilisateur visualise l'album."
+"\n"+
"Pour organiser ses photos on met une photos dans un album ou on range les photos d'un album."
+"\n"+
"Pour mettre une photos dans un album on choisit l'album cible et l'utilisateur sélectionne une photo."
+"\n"+
"Sélectionner une photo peut être répété."
+"\n"+
"Pour ranger les photos d'un album l'utilisateur choisit un album et l'utilisateur modifie le placement des photos dans l'album."
+"\n"+
"Modifier le placement des photos dans l'album peut être répété."
+"\n";
		transfo.ecritureMdT(false);
		assertNotEquals("vide après écriture", transfo.getTextMdT(), "");
		//assertEquals("cas écriture sans répétition du sujet", transfo.getTextMdT(), text);
		text = "Pour gérer ses photos numériques on ajoute une photo, on regarde un album, et en même temps on organise ses photos."+"\n"+"\n"+
"Ajouter une photo, regarder un album et organiser ses photos peuvent être répété."+"\n"+"\n"+
"Pour ajouter une photo l'utilisateur sélectionne une photo et le système place la photo dans les photos non triées."+"\n"+"\n"+
"Pour regarder un album l'utilisateur sélectionne un album et l'utilisateur visualise l'album."+"\n"+"\n"+
"Pour organiser ses photos on met une photos dans un album ou on range les photos d'un album."+"\n"+"\n"+
"Pour mettre une photos dans un album l'utilisateur sélectionne une photo et on choisit l'album cible."+"\n"+"\n"+
"Sélectionner une photo peut être répété."+"\n"+"\n"+
"Pour ranger les photos d'un album l'utilisateur choisit un album et l'utilisateur modifie le placement des photos dans l'album."+"\n"+"\n"+
"Modifier le placement des photos dans l'album peut être répété."+"\n"+"\n";
		
		assertEquals("cas écriture avec répétition du sujet", transfo.getTextMdT(), text);
		
		
		
		transfo2.ecritureMdT(false);
		text= "Pour gérer ses photos numériques on ajoute une photo, on regarde un album, et en même temps on organise ses photos."+"\n"+"\n"+
		"Ajouter une photo, regarder un album et organiser ses photos peuvent être répété."+"\n"+"\n"+
		"Pour ajouter une photo l'utilisateur sélectionne une photo et le système place la photo dans les photos non triées."+"\n"+"\n"+
		"Pour regarder un album l'utilisateur sélectionne un album et l'utilisateur visualise l'album."+"\n"+"\n"+
		"Pour organiser ses photos on met une photos dans un album ou on range les photos d'un album."+"\n"+"\n"+
		"Pour mettre une photos dans un album l'utilisateur sélectionne une photo et on choisit l'album cible."+"\n"+"\n"+
		"Sélectionner une photo peut être répété."+"\n"+"\n"+
		"Pour ranger les photos d'un album l'utilisateur choisit un album et l'utilisateur modifie le placement des photos dans l'album."+"\n"+"\n"+
		"Modifier le placement des photos dans l'album peut être répété."+"\n"+"\n";
		//transfo2.ecritureMdT();
		//System.out.println(transfo2.getTextMdT());
		//System.out.println(transfo2.getNomFichierKMAD());
		assertEquals("cas écriture avec iteration informelle", transfo2.getTextMdT(), text);
	}

	/**
	 * Test method for {@link TMRNF.KMADToSentenceStructure#produitDocument(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testProduitDocument() {
		//non testé
	}

}
