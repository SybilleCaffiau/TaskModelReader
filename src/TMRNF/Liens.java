package TMRNF;

import java.util.HashMap;
import java.util.Map;

//fichier pour lier les tâches et les objets utilisés par les tâches
public class Liens {
	
	private Map<String, String> tache_objet = new HashMap<>();

	//constructeur pour les liens "à la main"
	public Liens(){
		//à chaque tâche est associé un objet (un seul pour le moment). Tous les objets sont traduisibles en données GPS
		
				//a est le point de départ (en POI)
				tache_objet.put("Aller au point de départ", "a");
				
				//b et c sont l'objectif visé
				tache_objet.put("Descendre à l'objectif", "b");
				tache_objet.put("Monter à l'objectif", "c");
				
				//d est l'objectif visé
				tache_objet.put("Choisir l'objectif", "d");
				
				//e est ce qui est admiré
				tache_objet.put("Admirer la vue", "e");
				
				//f est le lieu où on a mangé
				tache_objet.put("Manger", "f");
				
				//g est le lieu où l'incident s'est produit
				tache_objet.put("Subir un incident", "g");
				
				//h est le lieu de la rencontre
				tache_objet.put("Rencontrer d'autres randonneurs", "h");
				
				//i est le lieu des conditions évaluées
				tache_objet.put("Evaluer les conditions de randonnée", "i");
				
				//j est le point de départ 
				tache_objet.put("Descendre au point de départ", "j");
				
	}
	
	//retourne le nom de l'objet utilisé dans la tâche 
	public String getObjet(String tache){
		
		return tache_objet.get(tache);
		
	}
	
}
