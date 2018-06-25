package TMRNF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//classe qui permet de créer/récupérer la liste des objets (avec leur valeur)
public class lesObjetsDuDomaine {
	
	
	private List<Instance> seq_objet = new ArrayList();
	private Date dateDonnees;
	
	//creation du contenu "à la main"
	public lesObjetsDuDomaine(){
		
		//date de la randonnée
				try{
					dateDonnees=new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-22");
				} catch(ParseException e){
					e.printStackTrace();
				}
		//dans un premier temps, j'impose que le déterminant soit mis dans la valeur
				//dans l'ordre séquentiel
				//issu du traitement de François sur les données de Catherine (tous les activity event n'ayant pas de POI ont été supprimés
				
				seq_objet.add(new Instance("a","arrêt de bus de La coche", "2017-01-22 09:01:20"));
				
				seq_objet.add(new Instance("c","Grand Rocher", "2017-01-22 09:17:40"));
				seq_objet.add(new Instance("e","Grand Rocher", "2017-01-22 12:05:43"));
				seq_objet.add(new Instance("b","bergerie", "2017-01-22 12:18:07"));
				seq_objet.add(new Instance("c","fontaine", "2017-01-22 09:40:12"));
				seq_objet.add(new Instance("j","arrêt de bus de La coche", "2017-01-22 12:57:45"));
				
	}
	
	public Instance getInstance(int pos){
		return seq_objet.get(pos);
	}
	
	public int getSize(){
		return seq_objet.size();
	}
	
	public Date getDateDonnees(){
		return dateDonnees;
	}

}
