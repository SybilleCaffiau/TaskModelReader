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
			String t=c.ecritSansExemple();
			
			assertNotEquals("cas texte ecrit null", t, " ");
			String tex="Pour gérer ses photos numériques on ajoute une photo, on regarde un album, et en même temps on organise ses photos."
			+"\n"+ "\n"+
"Ajouter une photo, regarder un album et organiser ses photos peuvent être répété."
			+"\n"+"\n"+
"Pour ajouter une photo l'utilisateur sélectionne une photo et le système place la photo dans les photos non triées."
					+"\n"+"\n"+
"Pour regarder un album l'utilisateur sélectionne un album et l'utilisateur visualise l'album."
					+"\n"+"\n"+
"Pour organiser ses photos on met une photos dans un album ou on range les photos d'un album."
					+"\n"+"\n"+
"Pour mettre une photos dans un album l'utilisateur sélectionne une photo et on choisit l'album cible."
					+"\n"+"\n"+
"Sélectionner une photo peut être répété."
					+"\n"+"\n"+
"Pour ranger les photos d'un album l'utilisateur choisit un album et l'utilisateur modifie le placement des photos dans l'album."
					+"\n"+"\n"+
"Modifier le placement des photos dans l'album peut être répété."
+"\n"+"\n";
			
			assertEquals("cas texte ecrit sans exemple", t, tex);
			
			c.prepareFichier("TestMdTRandonneesIterPasDeplacer.kxml");
			String t2=c.ecritSansExemple();
			String tex2="Pour faire une sortie de randonnée à ski l'utilisateur sans le logiciel va au point de départ, on fait la randonnée et l'utilisateur sans le logiciel descend au point de départ."+"\n"+"\n"+ 
			"Pour faire la randonnée on fait le trajet, on interrompt éventuellement le trajet, on rencontre éventuellement d'autres randonneurs, et en même temps l'utilisateur sans le logiciel evalue les conditions de randonnée."+"\n"+"\n"+
					"Pour faire le trajet on se déplace, et en même temps l'utilisateur sans le logiciel choisit éventuellement l'objectif."+"\n"+"\n"+
"Pour se déplacer l'utilisateur sans le logiciel monte à l'objectif ou l'utilisateur sans le logiciel descend à l'objectif."+"\n"+"\n"+
"Monter à l'objectif et descendre à l'objectif peuvent être répété."+"\n"+"\n"+"Pour interrompre le trajet on fait une pause ou on subit un incident."
+"\n"+"\n"+"Pour faire une pause l'utilisateur sans le logiciel admire la vue ou l'utilisateur sans le logiciel mange."+"\n"+"\n";
			
			assertEquals("cas texte ecrit randonnée sans exemple", t2, tex2);
			
			
			String t3=c.ecritAvecExemple("blablabla");
			String tex3="Pour faire une sortie de randonnée à ski l'utilisateur sans le logiciel va au point de départ, on fait la randonnée et l'utilisateur sans le logiciel descend au point de départ."+"\n"+
"Par exemple, le 22 janvier 2017 pour faire une sortie de randonnée à ski Catherine est allé à l'arrêt de bus de La coche, on a fait la randonnée et Catherine a descendu à l'arrêt de bus de La coche."+"\n"+"\n"+"\n"+
"Pour faire la randonnée on fait le trajet, on interrompt éventuellement le trajet, on rencontre éventuellement d'autres randonneurs, et en même temps l'utilisateur sans le logiciel evalue les conditions de randonnée."+"\n"+"\n"+
"Pour faire le trajet on se déplace, et en même temps l'utilisateur sans le logiciel choisit éventuellement l'objectif."+"\n"+"\n"+
"Pour se déplacer l'utilisateur sans le logiciel monte à l'objectif ou l'utilisateur sans le logiciel descend à l'objectif."+"\n"+
"Par exemple, le 22 janvier 2017 pour se déplacer Catherine monte au Grand Rocher."+"\n"+"\n"+"\n"+
"Monter à l'objectif et descendre à l'objectif peuvent être répété."+"\n"+"\n"+"Pour interrompre le trajet on fait une pause ou on subit un incident."+"\n"+"\n"+
"Pour faire une pause l'utilisateur sans le logiciel admire la vue ou l'utilisateur sans le logiciel mange."+"\n"+
"Par exemple, le 22 janvier 2017 pour faire une pause Catherine admire le Grand Rocher."+"\n"+"\n"+"\n";
			
			assertEquals("cas texte ecrit randonnée avec exemple", t3, tex3);
			
			
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
