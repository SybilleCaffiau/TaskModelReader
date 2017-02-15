package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import TMRUI.Controleur;

/**
 * @author sybillecaffiau
 *
 */

public class ControleurTest {
	
	
	//l'objet testé
	private Controleur c=new Controleur();

	@Test 
	public void testPrepareFichier() {
		try{
			c.prepareFichier("testTache.kxml");
		}
		catch(IOException io){
			System.out.println("pb dans le texte de la préparation du fichier");
		};
		
	}

	@Test
	public void testEcrit() {
		try{
			c.prepareFichier("testTache.kxml");
			String t=c.ecrit();
			System.out.println(t);
			assertNotEquals("cas texte ecrit null", t, " ");
			String tex="Pour gérer ses photos numériques on ajoute une photo, on regarde un album, et en même temps on organise ses photos."
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
			assertEquals("cas texte ecrit", t, tex);
		}
		catch(IOException io){
			System.out.println("pb dans le texte de la préparation du fichier");
		};
	}

	@Test
	public void testSauve() {
		//non testé
	}
	
	@Test
	public void SupprFichierTemp(){
		//non testé
	}

}
