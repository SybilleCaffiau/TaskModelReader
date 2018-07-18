package TMRNF;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import gnad.data.storyElement.StoryElementOntologyMapping;
import gnad.io.Util;
import uk.ac.abdn.carados.OntologyClassHandler;
import uk.ac.abdn.carados.OntologyException;
import uk.ac.abdn.carados.OntologyInstanceHandler;
import uk.ac.abdn.carados.owl2.Owl2CaradosOntology;

//classe qui permet de créer/récupérer la liste des objets (avec leur valeur)
public class lesObjetsDuDomaine {
	
	
	private List<Instance> seq_objet = new ArrayList();
	private Date dateDonnees;
	
	//creation du contenu "à la main"
	/*public lesObjetsDuDomaine(){
		
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
				
	}*/
	
	public lesObjetsDuDomaine(String fichierOnto){
			//Chemin d'accès au fichier de l'ontologie
					//String fileName =  "file:" +  Util.getPWD() +"/files/temp.owl";
					
					
		
					String fileName =  "file:" +  Util.getPWD() +"/files/temp_2013_03_10_Merlet_Lavoire_AvecData.owl";
					
			
					//date de la randonnée
					try{
						dateDonnees=new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-22");
					} 
					catch(ParseException e){
						e.printStackTrace();
					}
					
					//objet pour manipuler l'ontologie
					Owl2CaradosOntology kb = new Owl2CaradosOntology();
					try {
						//le cas du point de départ n'est pas terminé car il n'y en a pas dans l'ontologie (besoin d'un exemple d'instance pour Departure_class)
						
						//chargement en mémoire de l'ontologie
						kb.loadOntology(URI.create(fileName));
						
						//récupération de la classe departure class
						OntologyClassHandler classeDepart = kb.getOntologyClass(StoryElementOntologyMapping.DEPARTURE_CLASS);
						
						//System.out.println("Nombre : "+classeDepart.);
						
						//code EVENT
						
						for (OntologyInstanceHandler i : classeDepart.getInstances(true)){
							
							System.out.println("\n"+i.getName() +" de type " + i.getOntologyClass().getName()+" ");
							
							//si le point de depart a un POI
							
							
							System.out.println(i.hasProperty(StoryElementOntologyMapping.PLACE_RELATION));
							
							
							System.out.println(i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION));
							
							//si le point d'arrivée du déplacement est spécifié
							if (i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION)!=null){
								
								
								String POI;
								OntologyInstanceHandler ne;
								//liste des POI => prendre 1 seul
								if(i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION).getClass()==HashSet.class){
									HashSet POI_Arrivee=(HashSet)i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION);
									ne = (OntologyInstanceHandler)POI_Arrivee.toArray()[0];
									POI = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								}
								else{
								//1 seul
									ne = (OntologyInstanceHandler)i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION);
									System.out.println(i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION));
									System.out.println(ne.toString());
									System.out.println(ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY));
									Collection<String> s=ne.getPropertyNames();
									
									System.out.println("Les propriétés");
									
									for(String si:s){
									
										System.out.println(si);
									
									}
									POI = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								}
								
								
								//String dateEvent = ne.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								//Collection<Object> val = i.getOwnValueAsCollection(StoryElementOntologyMapping.CHARACTER_RELATION);
								
								//la date (à revoir pour le parse dans Instance)
								//String dateEvent = i.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								String dateEvent = "2017-01-22 09:40:12";
								/*Collection<String> s=i.getPropertyNames();
								
								System.out.println("Les propriétés");
								
								for(String si:s){
								
									System.out.println(si);
								
								}*/
								
								//a est le point de départ (en POI)
								//tache_objet.put("Aller au point de départ", "a");
									System.out.println("Le point d'interet du stop " + POI);
									seq_objet.add(new Instance("a",POI, dateEvent));
								
	
								
								System.out.println("le POI d'arrivée est " +ne.getName() + " de " + ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY));
							}
							
				
						}
						
						
						
						
						
						
						//récupération de la classe activity event 
						OntologyClassHandler classeDeplacement = kb.getOntologyClass(StoryElementOntologyMapping.ACTIVITY_EVENT_CLASS);

						
						//toutes les instances de type activity_event (les déplacements)
						for (OntologyInstanceHandler i : classeDeplacement.getInstances(true)){
							
							System.out.println("\n"+i.getName() +" de type " + i.getOntologyClass().getName()+" ");
							
							//si l'activity_event a un POI
							//seq_objet.add(new Instance("a","arrêt de bus de La coche", "2017-01-22 09:01:20"));
							
							System.out.println(i.hasProperty(StoryElementOntologyMapping.END_ELEMENT_RELATION));
							
							
							System.out.println(i.getOwnValue(StoryElementOntologyMapping.END_ELEMENT_RELATION));
							
							//si le point d'arrivée du déplacement est spécifié
							if (i.getOwnValue(StoryElementOntologyMapping.END_ELEMENT_RELATION)!=null){
								//liste des POI => prendre 1 seul
								
								
								
								String POI;
								OntologyInstanceHandler ne;
								if(i.getOwnValue(StoryElementOntologyMapping.END_ELEMENT_RELATION).getClass()==HashSet.class){
									HashSet POI_Arrivee=(HashSet)i.getOwnValue(StoryElementOntologyMapping.END_ELEMENT_RELATION);
									ne = (OntologyInstanceHandler)POI_Arrivee.toArray()[0];
									POI = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								}
								else{
									ne = (OntologyInstanceHandler)i.getOwnValue(StoryElementOntologyMapping.END_ELEMENT_RELATION);
									POI = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								}
								
								
								//String dateEvent = ne.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								//Collection<Object> val = i.getOwnValueAsCollection(StoryElementOntologyMapping.CHARACTER_RELATION);
								
								//la date (à revoir pour le parse dans Instance)
								//String dateEvent = i.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								String dateEvent = "2017-01-22 09:40:12";
								/*Collection<String> s=i.getPropertyNames();
								
								System.out.println("Les propriétés");
								
								for(String si:s){
								
									System.out.println(si);
								
								}*/
								
								//Travel_Mode (pour voir si on se déplace ou si on fait un stop)
								//TRAVEL_MODE_PROPERTY
								System.out.println(i.getOwnValue(StoryElementOntologyMapping.TRAVEL_MODE_PROPERTY));
								
								//si ce n'est pas un arret
								if(i.getOwnValue(StoryElementOntologyMapping.TRAVEL_MODE_PROPERTY).toString().compareTo("stop")!=0){
									//il y a un deplacement
									
									OntologyInstanceHandler act = (OntologyInstanceHandler)i.getOwnValue(StoryElementOntologyMapping.HAS_ACTIVITY_RELATION);
									//System.out.println("l'activité est " +act.getName() + " de " + act.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY));
									
									if(act.getName().compareTo("mount")==0){
										seq_objet.add(new Instance("c",POI, dateEvent));
										
									}
									else{
										seq_objet.add(new Instance("b",POI, dateEvent));
									}
									System.out.println(i.getOwnValue(StoryElementOntologyMapping.HAS_ACTIVITY_RELATION));

									
								}
								//si c'est un arret
								else{
									//System.out.println("Le point d'interet du stop " + POI);
									seq_objet.add(new Instance("e",POI, dateEvent));
								}
	
								
								System.out.println("le POI d'arrivée est " +ne.getName() + " de " + ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY));
							}
							
							
						}
							
									
							//arrivée
							//récupération de la classe departure class
						
						OntologyClassHandler classeArrive = kb.getOntologyClass(StoryElementOntologyMapping.ARRIVAL_CLASS);
						
					

						
						for (OntologyInstanceHandler i : classeArrive.getInstances(true)){
							
							System.out.println("\n"+i.getName() +" de type " + i.getOntologyClass().getName()+" ");
							
							//si le point d'arrivée a un POI
							

							
							Collection<String> s=i.getPropertyNames();
							
							System.out.println("Les propriétés");
							
							for(String si:s){
							
								System.out.println(si);
								System.out.println(i.hasProperty(StoryElementOntologyMapping.PLACE_RELATION));
							
							}
							
							
							
							
							
							
							System.out.println(i.hasProperty(StoryElementOntologyMapping.PLACE_RELATION));
							
							
							System.out.println(i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION));
							
							//si le point d'arrivée du déplacement est spécifié
							if (i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION)!=null){
								
								
								String POI;
								OntologyInstanceHandler ne;
								//liste des POI => prendre 1 seul
								if(i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION).getClass()==HashSet.class){
									HashSet POI_Arrivee=(HashSet)i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION);
									ne = (OntologyInstanceHandler)POI_Arrivee.toArray()[0];
									POI = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								}
								else{
								//1 seul
									ne = (OntologyInstanceHandler)i.getOwnValue(StoryElementOntologyMapping.PLACE_RELATION);
									POI = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								}
								
								
								//String dateEvent = ne.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								//Collection<Object> val = i.getOwnValueAsCollection(StoryElementOntologyMapping.CHARACTER_RELATION);
								
								//la date (à revoir pour le parse dans Instance)
								//String dateEvent = i.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								String dateEvent = "2017-01-22 09:40:12";
								/*Collection<String> s=i.getPropertyNames();
								
								System.out.println("Les propriétés");
								
								for(String si:s){
								
									System.out.println(si);
								
								}*/
								
								//a est le point de départ (en POI)
								//tache_objet.put("Aller au point de départ", "a");
									//System.out.println("Le point d'interet du stop " + POI);
									seq_objet.add(new Instance("j",POI, dateEvent));
								
									System.out.println("le POI d'arrivée est " +ne.getName() + " de " + ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY));
							}
						}	
						
						
						
						OntologyClassHandler classeCondition = kb.getOntologyClass(StoryElementOntologyMapping.CONDITION_EVENT_CLASS);
						
					
						
						for (OntologyInstanceHandler i : classeCondition.getInstances(true)){
							
							System.out.println("\n"+i.getName() +" de type " + i.getOntologyClass().getName()+" ");
							
							//si le point de depart a un POI
							
							
							System.out.println(i.hasProperty(StoryElementOntologyMapping.CONDITION_RELATION));
							
							
							System.out.println(i.getOwnValue(StoryElementOntologyMapping.CONDITION_RELATION));
							
							Collection<String> s=i.getPropertyNames();
							
							System.out.println("Les propriétés");
							
							for(String si:s){
							
								System.out.println(si);
							
							}
							
							//si une condition est spécifiée
							if (i.getOwnValue(StoryElementOntologyMapping.CONDITION_RELATION)!=null){
								
								
								String Description;
								OntologyInstanceHandler ne;
								//liste des conditions => prendre 1 seul
								if(i.getOwnValue(StoryElementOntologyMapping.CONDITION_RELATION).getClass()==HashSet.class){
									HashSet Condition=(HashSet)i.getOwnValue(StoryElementOntologyMapping.CONDITION_RELATION);
									ne = (OntologyInstanceHandler)Condition.toArray()[0];
									System.out.println(ne);
									
								}
								else{
								//1 seul
									ne = (OntologyInstanceHandler)i.getOwnValue(StoryElementOntologyMapping.CONDITION_RELATION);
									System.out.println(ne);
									
									Collection<String> s1=ne.getPropertyNames();
									
									System.out.println("Les propriétés");
									
									for(String si:s1){
									
										System.out.println(si);
									
									}
									
									
								}
								
								System.out.println(ne.getOntologyClass());
								
								Description = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								
								//la date (à revoir pour le parse dans Instance)
								//String dateEvent = i.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								String dateEvent = "2013-03-10 09:40:12";
								
								/*Collection<String> s=i.getPropertyNames();
								
								System.out.println("Les propriétés");
								
								for(String si:s){
								
									System.out.println(si);
								
								}*/
								
								
								
								
								
								//on est en présence d'un accident
								if (ne.getOntologyClass().toString().compareTo("Accident")==0){
									seq_objet.add(new Instance("g",Description, dateEvent));
								}
								else{
									seq_objet.add(new Instance("i",Description, dateEvent));
								}
									
								
								
								
								//String dateEvent = ne.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								//Collection<Object> val = i.getOwnValueAsCollection(StoryElementOntologyMapping.CHARACTER_RELATION);
								
								
								
								
								//a est le point de départ (en POI)
								//tache_objet.put("Aller au point de départ", "a");
								/*	System.out.println("Le point d'interet du stop " + POI);
									seq_objet.add(new Instance("a",POI, dateEvent));
								
	
								
								System.out.println("le POI d'arrivée est " +ne.getName() + " de " + ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY));*/
							}
							
				
						}
						
						
						
						OntologyClassHandler classeRencontre = kb.getOntologyClass(StoryElementOntologyMapping.MEETING_EVENT_CLASS);
						
					
						
						for (OntologyInstanceHandler i : classeRencontre.getInstances(true)){
							
							System.out.println("\n"+i.getName() +" de type " + i.getOntologyClass().getName()+" ");
							
							//si la rencontre a des acteurs
							
							
							System.out.println(i.hasProperty(StoryElementOntologyMapping.CHARACTER_RELATION));
							
							
							System.out.println(i.getOwnValue(StoryElementOntologyMapping.CHARACTER_RELATION));
							
							Collection<String> s=i.getPropertyNames();
							
							System.out.println("Les propriétés");
							
							for(String si:s){
							
								System.out.println(si);
							
							}
							
							//si une condition est spécifiée
							if (i.getOwnValue(StoryElementOntologyMapping.CHARACTER_RELATION)!=null){
								
								
								String POI;
								OntologyInstanceHandler ne;
								//liste des conditions => prendre 1 seul
								if(i.getOwnValue(StoryElementOntologyMapping.CHARACTER_RELATION).getClass()==HashSet.class){
									HashSet Tab_POI=(HashSet)i.getOwnValue(StoryElementOntologyMapping.CHARACTER_RELATION);
									ne = (OntologyInstanceHandler)Tab_POI.toArray()[0];
									System.out.println(ne);
									
								}
								else{
								//1 seul
									ne = (OntologyInstanceHandler)i.getOwnValue(StoryElementOntologyMapping.CHARACTER_RELATION);
									System.out.println(ne);
									
									Collection<String> s1=ne.getPropertyNames();
									
									System.out.println("Les propriétés");
									
									for(String si:s1){
									
										System.out.println(si);
									
									}
									
									
								}
								
								System.out.println(ne.getOntologyClass());
								
								POI = ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY).toString();
								
								//la date (à revoir pour le parse dans Instance)
								//String dateEvent = i.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								String dateEvent = "2017-01-22 09:40:12";
								
								/*Collection<String> s=i.getPropertyNames();
								
								System.out.println("Les propriétés");
								
								for(String si:s){
								
									System.out.println(si);
								
								}*/
								
								
								
								
								
								//on est en présence d'un accident
								
									seq_objet.add(new Instance("h",POI, dateEvent));
								
									
								
								
								
								//String dateEvent = ne.getOwnValue(StoryElementOntologyMapping.END_DATE_PROPERTY).toString();
								//Collection<Object> val = i.getOwnValueAsCollection(StoryElementOntologyMapping.CHARACTER_RELATION);
								
								
								
								
								//a est le point de départ (en POI)
								//tache_objet.put("Aller au point de départ", "a");
								/*	System.out.println("Le point d'interet du stop " + POI);
									seq_objet.add(new Instance("a",POI, dateEvent));
								
	
								
								System.out.println("le POI d'arrivée est " +ne.getName() + " de " + ne.getOwnValue(StoryElementOntologyMapping.NAME_PROPERTY));*/
							}
							
				
						}
						
						
					} 
					catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
		
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
