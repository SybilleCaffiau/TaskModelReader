package TMRNF;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import gnad.data.storyElement.StoryElementOntologyMapping;
import gnad.io.Util;
import uk.ac.abdn.carados.OntologyClassHandler;
import uk.ac.abdn.carados.OntologyException;
import uk.ac.abdn.carados.OntologyInstanceHandler;
import uk.ac.abdn.carados.owl2.Owl2CaradosOntology;

//fichier pour lier les tâches et les objets utilisés par les tâches
public class Liens {
	
	private Map<String, String> tache_objet = new HashMap<>();

	//constructeur pour les liens "à la main"
	public Liens(){
		//à chaque tâche est associé un objet (un seul pour le moment). Tous les objets sont traduisibles en données GPS
		
				//a est le point de départ (en POI)
				tache_objet.put("Aller au point de départ", "a");//OK
				
				//b et c sont l'objectif visé
				tache_objet.put("Descendre à l'objectif", "b");//OK
				tache_objet.put("Monter à l'objectif", "c");//OK
				
				//d est l'objectif visé
				tache_objet.put("Choisir l'objectif", "d");
				
				//e est le lieu de l'arrêt
				//tache_objet.put("S'arrêter", "e");//OK
				//probléme lors de la génération
				
				//f est le lieu où on a mangé
				//tache_objet.put("Manger", "f");
				
				//g est le lieu où l'incident s'est produit
				tache_objet.put("Subir un incident", "g");//OK
				
				//h est le lieu de la rencontre
				tache_objet.put("Rencontrer d'autres randonneurs", "h");//mettre les personnes + le lieu
				
				//i est le lieu des conditions évaluées
				tache_objet.put("Evaluer les conditions de randonnée", "i");//OK
				
				//j est le point de départ 
				tache_objet.put("Retourner au point de départ", "j");//OK
				
				tache_objet.put("Choisir l'objectif", "k");//OK
				
	}
	
	
	
	//retourne le nom de l'objet utilisé dans la tâche 
	public String getObjet(String tache){
		
		return tache_objet.get(tache);
		
	}
	
}
