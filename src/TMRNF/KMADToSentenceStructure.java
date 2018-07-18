package TMRNF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import simplenlg.aggregation.*;

/**
 * @author sybillecaffiau
 *
 */

public class KMADToSentenceStructure implements KMADToSentenceStructureInter {
	private String fichierKMAD; //Nom "Humain" du fichier en entrée
	
	//racine et document XML (format DOM)
	private static Element racine;
	private static Document docSource=new Document(racine);
	//le document au bon format sera nommé document
	//private static Document docForme;
	private String textMdt="";
	
	
	//ne servent que pour les exemples
	//structure temporaire pour lier les tâches et les objets
	private Liens tache_objet;
	
	//les sujets dans le cas où des valeurs ne sont pas attribué
	private Executants exec = new Executants("le randonneur", "le randonneur", "le randonneur", "le randonneur", "Catherine", "Catherine", "Catherine","Catherine" );
	
	
	private Date dateRando;
	
	
	
	
	
	//liste des s de l'ontologie
	private lesObjetsDuDomaine seq_objet = new lesObjetsDuDomaine("en utilisant l'ontologie");
	
	
	public KMADToSentenceStructure(String nomFichier){
		dateRando=seq_objet.getDateDonnees();
				
		fichierKMAD=nomFichier;
		//parse le fichier kxml pour en obtenir la structure
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			///creation du document JDOM à partir du fichier XML
			docSource=sxb.build(new File(fichierKMAD));
		}
		catch (Exception e){
			System.out.println("Problème lors de la creation du document JDOM");
		}
		
		//initialisation de la racine
		racine=docSource.getRootElement();
		
	}
	
	public void lier(String nomFichierOntologie){
		if(nomFichierOntologie=="null"){
			
		}
		else{
			//il faudra prendre en compte le fichier
			tache_objet = new Liens();
		}
	}
	
	/**
	 * Assesseur au nom du fichier xml traité (temporaire)
	 * @return nom du fichier .xml qui est en traitement (fichier temporaire copié du kxml)
	 */
	public String getNomFichierKMAD(){
		return fichierKMAD;
	}
	
	/**
	 * Méthode qui affiche en console le DOM correspondant aux informations du fichier xml
	 */
	public void afficheDocumentSource(){
		try{
			XMLOutputter sortie=new XMLOutputter(Format.getPrettyFormat());
			sortie.output(docSource, System.out);
		}
		catch (java.io.IOException e){
			System.out.println("Problème lors de l'affichage du document");
		}
	}
	
	/**
	 * Assesseur de la version textuelle généree
	 * @return le texte représentant le modéle de tâches
	 */
	public String getTextMdT(){
		
		return this.textMdt;
	}
	
	/**
	 * Methode de traduction du xml vers du texte
	 */
	public void ecritureMdT(boolean exemple){
		//la racine du document 
		Element root=docSource.getRootElement();		
		List listeTaches = root.getChildren("task");
		//la liste ne retourne qu'un suel objet : la tache racine
		//System.out.println("il y a :"+listeTaches.size()+" filles pour "+root.getName());
		ecritureTacheITer((Element)listeTaches.get(0),exemple);
		
	}
	
	private void ecritureTacheITer(Element mere, boolean exemple){
		
		//System.out.println("iteration sur les filles");
		List listeTaches = mere.getChildren("task");
		
		//System.out.println("il y a "+listeTaches.size()+" filles");
		if(listeTaches.size()>0){
			//si elle a des filles (tache décomposée), on l'affiche puis on lance l'algo sur les filles
			ecritureTache(listeTaches,mere, exemple);
			
			Iterator i = listeTaches.iterator();
			while(i.hasNext()){
				
				Element courant=(Element)i.next();
				
				//System.out.println("Dans ecriture MdT pour la tache "+courant.getChild("task-name").getText());
				ecritureTacheITer(courant, exemple);
				
			}
		}
		
		

	}
	


	//Retourne la phrase dans le cas où l'opérateur de décomposition est la séquence
	private void ecritureTacheSeq(List noeudFille, Element parent, boolean exemple){
		//System.out.println("J'écris les filles");
		Realiser realiser = new Realiser();
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		CoordinatedPhraseElement c=nlgFactory.createCoordinatedPhrase();
		CoordinatedPhraseElement c1=nlgFactory.createCoordinatedPhrase();
		//pour savoir si on est dans une phrase sequentielle
		boolean seq=true;
		//SPhraseSpec proposition= nlgFactory.createClause();
	
			//System.out.println("Je suis dans le cas d'un ET");
			c.setConjunction("et");
		
		
		//ici objectif groupe verbe infinitif
		//c'est un syntagme prépositionnel
		String action=parent.getChild("task-name").getText();
		action=action.toLowerCase();
		
		
		PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
		complementDuVerbe.addPreModifier("pour");
		c.addPreModifier(complementDuVerbe);
		
		//on parcours toutes les taches
		Iterator i = noeudFille.iterator();
		//Liste des propositions pour une meme tache abstraite
		List<NLGElement> elements= new ArrayList();
		List<String> verb_iter= new ArrayList();//tableau des verbes iteratifs
		NLGElement prop1;
		//Liste des tâches elementaires (qui auront des valeurs dans l'exemple)
		List<Element> tache_avec_valeur = new ArrayList();
		while(i.hasNext()){
			Element courant =(Element)i.next();
			
			//Ajout de la tâche à l'ensemble des tâches pour l'exemple (le cas échéant)
			String type = courant.getChild("task-decomposition").getText();
			if(type.compareTo("LEAF")==0){
				tache_avec_valeur.add(courant);
			}
			
			//pour tous les enfants
			//construction de la description des sous taches
			
			SPhraseSpec s=ecritureTacheElementaireSeq(courant,verb_iter,false);
					//System.out.println(s);
					//ajout à la phrase en conception
			
			
			
			NPPhraseSpec on=nlgFactory.createNounPhrase("on");

			//gestion d'évitement de la répétition des sujets
			//if(seq & !s.getSubject().getFeature("head").toString().equals(on.getFeature("head").toString())){
				elements.add(s);
				//System.out.println("dans le if");
			//}
			//else{
				//c.addCoordinate(s);	
				//System.out.println("hors du if");
			//}
		}
		
		if((elements.size()>0) ) {
			ClauseCoordinationRule clauseCoord = new ClauseCoordinationRule();
			List<NLGElement> result = clauseCoord.apply(elements);
			//System.out.println(result.size());
			
			if ((result.size()==1) && (!elements.get(0).equals(null))){
				//System.out.println(result.size());
				//System.out.println(result.get(0));
				//l'aggregation a été possible
				NLGElement aggregated = result.get(0);
				
				//c.addCoordinate(aggregated);
				c.addComplement(aggregated);
			}
			else{
				for(int k=0;  k<elements.size();k++){
					NLGElement aggregated = elements.get(k);
					c.addCoordinate(aggregated);
					//c.addComplement(aggregated);
				}
				
			}
		}
		if(verb_iter.size()>0){
			//System.out.println("iteratif");
			List<NLGElement> verb_iterEL= new ArrayList();
			ClauseCoordinationRule clauseCoordIt = new ClauseCoordinationRule();
			WordElement v=frenchlexicon.getWord(verb_iter.get(0), LexicalCategory.NOUN);
			v.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
			v.setFeature(LexicalFeature.PROPER,true);
			prop1=nlgFactory.createClause(v,"pouvoir","être répété");
			verb_iterEL.add(prop1);
			//System.out.println(verb_iter.get(0));
			for(int t=1; t<verb_iter.size();t++){
				WordElement v1=frenchlexicon.getWord(verb_iter.get(t), LexicalCategory.NOUN);
				v1.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
				v1.setFeature(LexicalFeature.PROPER,true);
				NLGElement prop2=nlgFactory.createClause(v1,"pouvoir","être répété");
				verb_iterEL.add(prop2);
				//System.out.println(verb_iterEL.size());
			}
			
			List<NLGElement> resultIT = clauseCoordIt.apply(verb_iterEL);
			//System.out.println(resultIT.size());
		
			if(verb_iterEL.size()>1){
				NLGElement agIt=resultIT.get(0);
				//System.out.println(agIt);
				c1.addComplement(agIt);
			}
			else{
				c1.addComplement(prop1);
			}
			
		}
	
		
		
		String output = realiser.realiseSentence(c);
		//String output = realiser.realiseSentence(aggregated);
		
		
		
		
		//System.out.println(output);
		this.textMdt+=output;
		this.textMdt+="\n";
		
		
		
		if(exemple){

			if(tache_avec_valeur.size()>0){
			//lier la tâche avec un objet pour laquelle on pourrait avec des valeurs
			
				List<Element> tache_avec_valeurOK=new ArrayList();
			
				for (int z=0; z<tache_avec_valeur.size(); z++){
			
				//tache_objet.get(tache_avec_valeur.get(z).getChild("task-name").getText());
					boolean t=false;
					int seqO=0;
					while(seqO<seq_objet.getSize() && !t){
						if(tache_objet.getObjet(tache_avec_valeur.get(z).getChild("task-name").getText())==seq_objet.getInstance(seqO).getObjet()){
						//on a trouvé l'objet
							t=true;
						}
						else{seqO+=1;}
					}
					if(t){
					//this.textMdt+="Par exemple, ";
					//exemple+=seq_objet.get(seqO).objet;
					//exemple+=" = ";
					//exemple+= seq_objet.get(seqO).valeur;
					//this.textMdt+=tache_objet.get(tache_avec_valeur.get(0).getChild("task-name").getText());
						tache_avec_valeurOK.add(tache_avec_valeur.get(z));
					
					}
				
				
				}	
			//pour tester comment faire avec et sans exemple
				if((tache_avec_valeurOK.size()>0) && (exemple) ){
					ecritureTacheAvecExemple(noeudFille, parent, tache_avec_valeurOK);
				}
			
			}
		}
		
		/*if(exemple.length()>1){
			this.textMdt+="Par exemple, ";
			this.textMdt+=exemple;
			exemple="";
			this.textMdt+="\n";
		}*/
		
		this.textMdt+="\n";
		
		//System.out.println(aggregated);
		String output2=realiser.realiseSentence(c1);
		if(output2.length()>0){
			//System.out.println(output2);
			this.textMdt+=output2;
			this.textMdt+="\n";
			this.textMdt+="\n";
		}
		
	}
	
	
	
	//Retourne la phrase dans le cas où l'opérateur de décomposition est l'alternatif
		private void ecritureTacheAlt(List noeudFille, Element parent, boolean exemple){
			//System.out.println("J'écris les filles");
			Realiser realiser = new Realiser();
			Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
			NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
			
			CoordinatedPhraseElement c=nlgFactory.createCoordinatedPhrase();
			CoordinatedPhraseElement c1=nlgFactory.createCoordinatedPhrase();
			
			//SPhraseSpec proposition= nlgFactory.createClause();
		
				//System.out.println("Je suis dans le cas d'un ET");
				c.setConjunction("ou");
			
			
			//ici le sujet de la phrase (le nom de la tâche décomposée)
			//c'est un syntagme prépositionnel
			String action=parent.getChild("task-name").getText();
			action=action.toLowerCase();
			
			
			SPhraseSpec obj =nlgFactory.createClause(action, "signifier");
			
			//PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
			//complementDuVerbe.addPreModifier("pour");
			//c.addPreModifier(complementDuVerbe);
			
			//on parcours toutes les taches
			Iterator i = noeudFille.iterator();
			//Liste des propositions pour une meme tache abstraite
			List<NLGElement> elements= new ArrayList();
			List<String> verb_iter= new ArrayList();//tableau des verbes iteratifs
			NLGElement prop1;
			//Liste des tâches elementaires (qui auront des valeurs dans l'exemple)
			List<Element> tache_avec_valeur = new ArrayList();
			while(i.hasNext()){
				Element courant =(Element)i.next();
				
				//Ajout de la tâche à l'ensemble des tâches pour l'exemple (le cas échéant)
				String type = courant.getChild("task-decomposition").getText();
				if(type.compareTo("LEAF")==0){
					tache_avec_valeur.add(courant);
				}
				
				//pour tous les enfants
				//construction de la description des sous taches
				
				SPhraseSpec s=ecritureTacheElementaireSeq(courant,verb_iter,false);
						//System.out.println(s);
						//ajout à la phrase en conception
				
				
				
				NPPhraseSpec on=nlgFactory.createNounPhrase("on");

				//gestion d'évitement de la répétition des sujets
				//if(seq & !s.getSubject().getFeature("head").toString().equals(on.getFeature("head").toString())){
					elements.add(s);
					//System.out.println("dans le if");
				//}
				//else{
					//c.addCoordinate(s);	
					//System.out.println("hors du if");
				//}
			}
			
			if((elements.size()>0) ) {
				ClauseCoordinationRule clauseCoord = new ClauseCoordinationRule();
				List<NLGElement> result = clauseCoord.apply(elements);
				//System.out.println(result.size());
				
				if ((result.size()==1) && (!elements.get(0).equals(null))){
					//System.out.println(result.size());
					//System.out.println(result.get(0));
					//l'aggregation a été possible
					NLGElement aggregated = result.get(0);
					
					//c.addCoordinate(aggregated);
					c.addComplement(aggregated);
				}
				else{
					for(int k=0;  k<elements.size();k++){
						NLGElement aggregated = elements.get(k);
						c.addCoordinate(aggregated);
						//c.addComplement(aggregated);
						
					}
					
				}
				obj.addComplement(c);
			}
			
			
			
			
			if(verb_iter.size()>0){
				//System.out.println("iteratif");
				List<NLGElement> verb_iterEL= new ArrayList();
				ClauseCoordinationRule clauseCoordIt = new ClauseCoordinationRule();
				WordElement v=frenchlexicon.getWord(verb_iter.get(0), LexicalCategory.NOUN);
				v.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
				v.setFeature(LexicalFeature.PROPER,true);
				prop1=nlgFactory.createClause(v,"pouvoir","être répété");
				verb_iterEL.add(prop1);
				//System.out.println(verb_iter.get(0));
				for(int t=1; t<verb_iter.size();t++){
					WordElement v1=frenchlexicon.getWord(verb_iter.get(t), LexicalCategory.NOUN);
					v1.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
					v1.setFeature(LexicalFeature.PROPER,true);
					NLGElement prop2=nlgFactory.createClause(v1,"pouvoir","être répété");
					verb_iterEL.add(prop2);
					//System.out.println(verb_iterEL.size());
				}
				
				List<NLGElement> resultIT = clauseCoordIt.apply(verb_iterEL);
				//System.out.println(resultIT.size());
			
				if(verb_iterEL.size()>1){
					NLGElement agIt=resultIT.get(0);
					//System.out.println(agIt);
					c1.addComplement(agIt);
				}
				else{
					c1.addComplement(prop1);
				}
				
			}
		
			
			
			String output = realiser.realiseSentence(obj);
			//String output = realiser.realiseSentence(aggregated);
			
			
			
			
			//System.out.println(output);
			this.textMdt+=output;
			this.textMdt+="\n";
			
			
			
			if(exemple){
				//on est dans un mode pour l'affichage d'exemple
				
				if(tache_avec_valeur.size()>0){
				//lier la tâche avec un objet pour laquelle on pourrait avec des valeurs
				
					List<Element> tache_avec_valeurOK=new ArrayList();
				
					for (int z=0; z<tache_avec_valeur.size(); z++){
				
					//tache_objet.get(tache_avec_valeur.get(z).getChild("task-name").getText());
						boolean t=false;
						int seqO=0;
						while(seqO<seq_objet.getSize() && !t){
							if(tache_objet.getObjet(tache_avec_valeur.get(z).getChild("task-name").getText())==seq_objet.getInstance(seqO).getObjet()){
							//on a trouvé l'objet
								t=true;
							}
							else{seqO+=1;}
						}
						if(t){
						//this.textMdt+="Par exemple, ";
						//exemple+=seq_objet.get(seqO).objet;
						//exemple+=" = ";
						//exemple+= seq_objet.get(seqO).valeur;
						//this.textMdt+=tache_objet.get(tache_avec_valeur.get(0).getChild("task-name").getText());
							tache_avec_valeurOK.add(tache_avec_valeur.get(z));
						
						}
					
					
					}	
				//pour tester comment faire avec et sans exemple
					if((tache_avec_valeurOK.size()>0) && (exemple) ){
						
						ecritureTacheAvecExemple(noeudFille, parent, tache_avec_valeurOK);
					}
				
				}
			}
			
			/*if(exemple.length()>1){
				this.textMdt+="Par exemple, ";
				this.textMdt+=exemple;
				exemple="";
				this.textMdt+="\n";
			}*/
			
			this.textMdt+="\n";
			
			//System.out.println(aggregated);
			String output2=realiser.realiseSentence(c1);
			if(output2.length()>0){
				//System.out.println(output2);
				this.textMdt+=output2;
				this.textMdt+="\n";
				this.textMdt+="\n";
			}
			
		}
		
		
		//Retourne la phrase dans le cas où l'opérateur de décomposition est le parallélisme
		private void ecritureTachePar(List noeudFille, Element parent, boolean exemple){
			//System.out.println("J'écris les filles");
			Realiser realiser = new Realiser();
			Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
			NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
			
			CoordinatedPhraseElement c=nlgFactory.createCoordinatedPhrase();
			CoordinatedPhraseElement c1=nlgFactory.createCoordinatedPhrase();
				c.setConjunction("et");
			
			
			
			
			//ici objectif groupe verbe infinitif
			//c'est un syntagme prépositionnel
			String action=parent.getChild("task-name").getText();
			action=action.toLowerCase();
			
			
			PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
			complementDuVerbe.addPreModifier("pour");
			complementDuVerbe.addPostModifier(", en même temps,");
			c.addPreModifier(complementDuVerbe);
	
			
			//on parcours toutes les taches
			Iterator i = noeudFille.iterator();
			//Liste des propositions pour une meme tache abstraite
			List<NLGElement> elements= new ArrayList();
			List<String> verb_iter= new ArrayList();//tableau des verbes iteratifs
			NLGElement prop1;
			//Liste des tâches elementaires (qui auront des valeurs dans l'exemple)
			List<Element> tache_avec_valeur = new ArrayList();
			while(i.hasNext()){
				Element courant =(Element)i.next();
				
				//Ajout de la tâche à l'ensemble des tâches pour l'exemple (le cas échéant)
				String type = courant.getChild("task-decomposition").getText();
				if(type.compareTo("LEAF")==0){
					tache_avec_valeur.add(courant);
				}
				
				//pour tous les enfants
				//construction de la description des sous taches
				
				SPhraseSpec s=ecritureTacheElementaireSeq(courant,verb_iter,false);
						//System.out.println(s);
						//ajout à la phrase en conception
				
				
				
				NPPhraseSpec on=nlgFactory.createNounPhrase("on");

				//gestion d'évitement de la répétition des sujets
				//if(seq & !s.getSubject().getFeature("head").toString().equals(on.getFeature("head").toString())){
					elements.add(s);
					//System.out.println("dans le if");
				//}
				//else{
					//c.addCoordinate(s);	
					//System.out.println("hors du if");
				//}
			}
			
			if((elements.size()>0) ) {
				ClauseCoordinationRule clauseCoord = new ClauseCoordinationRule();
				List<NLGElement> result = clauseCoord.apply(elements);
				//System.out.println(result.size());
				
				if ((result.size()==1) && (!elements.get(0).equals(null))){
					//System.out.println(result.size());
					//System.out.println(result.get(0));
					//l'aggregation a été possible
					NLGElement aggregated = result.get(0);
					
					//c.addCoordinate(aggregated);
					c.addComplement(aggregated);
				}
				else{
					for(int k=0;  k<elements.size();k++){
						NLGElement aggregated = elements.get(k);
						c.addCoordinate(aggregated);
						//c.addComplement(aggregated);
					}
					
				}
			}
			if(verb_iter.size()>0){
				//System.out.println("iteratif");
				List<NLGElement> verb_iterEL= new ArrayList();
				ClauseCoordinationRule clauseCoordIt = new ClauseCoordinationRule();
				WordElement v=frenchlexicon.getWord(verb_iter.get(0), LexicalCategory.NOUN);
				v.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
				v.setFeature(LexicalFeature.PROPER,true);
				prop1=nlgFactory.createClause(v,"pouvoir","être répété");
				verb_iterEL.add(prop1);
				//System.out.println(verb_iter.get(0));
				for(int t=1; t<verb_iter.size();t++){
					WordElement v1=frenchlexicon.getWord(verb_iter.get(t), LexicalCategory.NOUN);
					v1.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
					v1.setFeature(LexicalFeature.PROPER,true);
					NLGElement prop2=nlgFactory.createClause(v1,"pouvoir","être répété");
					verb_iterEL.add(prop2);
					//System.out.println(verb_iterEL.size());
				}
				
				List<NLGElement> resultIT = clauseCoordIt.apply(verb_iterEL);
				//System.out.println(resultIT.size());
			
				if(verb_iterEL.size()>1){
					NLGElement agIt=resultIT.get(0);
					//System.out.println(agIt);
					c1.addComplement(agIt);
				}
				else{
					c1.addComplement(prop1);
				}
				
			}
		
			
			
			String output = realiser.realiseSentence(c);
			//String output = realiser.realiseSentence(aggregated);
			
			
			
			
			//System.out.println(output);
			this.textMdt+=output;
			this.textMdt+="\n";
			
			
			
			if(exemple){

				if(tache_avec_valeur.size()>0){
				//lier la tâche avec un objet pour laquelle on pourrait avec des valeurs
				
					List<Element> tache_avec_valeurOK=new ArrayList();
				
					for (int z=0; z<tache_avec_valeur.size(); z++){
				
					//tache_objet.get(tache_avec_valeur.get(z).getChild("task-name").getText());
						boolean t=false;
						int seqO=0;
						while(seqO<seq_objet.getSize() && !t){
							if(tache_objet.getObjet(tache_avec_valeur.get(z).getChild("task-name").getText())==seq_objet.getInstance(seqO).getObjet()){
							//on a trouvé l'objet
								t=true;
							}
							else{seqO+=1;}
						}
						if(t){
						//this.textMdt+="Par exemple, ";
						//exemple+=seq_objet.get(seqO).objet;
						//exemple+=" = ";
						//exemple+= seq_objet.get(seqO).valeur;
						//this.textMdt+=tache_objet.get(tache_avec_valeur.get(0).getChild("task-name").getText());
							tache_avec_valeurOK.add(tache_avec_valeur.get(z));
						
						}
					
					
					}	
				//pour tester comment faire avec et sans exemple
					if((tache_avec_valeurOK.size()>0) && (exemple) ){
						ecritureTacheAvecExemple(noeudFille, parent, tache_avec_valeurOK);
					}
				
				}
			}
			
			/*if(exemple.length()>1){
				this.textMdt+="Par exemple, ";
				this.textMdt+=exemple;
				exemple="";
				this.textMdt+="\n";
			}*/
			
			this.textMdt+="\n";
			
			//System.out.println(aggregated);
			String output2=realiser.realiseSentence(c1);
			if(output2.length()>0){
				//System.out.println(output2);
				this.textMdt+=output2;
				this.textMdt+="\n";
				this.textMdt+="\n";
			}
			
		}
		
		
		//Retourne la phrase dans le cas où l'opérateur de décomposition est la sans ordre
		private void ecritureTacheSO(List noeudFille, Element parent, boolean exemple){
			//System.out.println("J'écris les filles");
			Realiser realiser = new Realiser();
			Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
			NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
			
			CoordinatedPhraseElement c=nlgFactory.createCoordinatedPhrase();
			CoordinatedPhraseElement c1=nlgFactory.createCoordinatedPhrase();
			//pour savoir si on est dans une phrase sequentielle
			boolean seq=true;
			//SPhraseSpec proposition= nlgFactory.createClause();
		
				//System.out.println("Je suis dans le cas d'un ET");
				c.setConjunction("et");
			
			
			//ici objectif groupe verbe infinitif
			//c'est un syntagme prépositionnel
			String action=parent.getChild("task-name").getText();
			action=action.toLowerCase();
			
			
			PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
			complementDuVerbe.addPreModifier("pour");
			complementDuVerbe.addPostModifier(", sans ordre,");
			c.addPreModifier(complementDuVerbe);
		
			
			//on parcours toutes les taches
			Iterator i = noeudFille.iterator();
			//Liste des propositions pour une meme tache abstraite
			List<NLGElement> elements= new ArrayList();
			List<String> verb_iter= new ArrayList();//tableau des verbes iteratifs
			NLGElement prop1;
			//Liste des tâches elementaires (qui auront des valeurs dans l'exemple)
			List<Element> tache_avec_valeur = new ArrayList();
			while(i.hasNext()){
				Element courant =(Element)i.next();
				
				//Ajout de la tâche à l'ensemble des tâches pour l'exemple (le cas échéant)
				String type = courant.getChild("task-decomposition").getText();
				if(type.compareTo("LEAF")==0){
					tache_avec_valeur.add(courant);
				}
				
				//pour tous les enfants
				//construction de la description des sous taches
				
				SPhraseSpec s=ecritureTacheElementaireSeq(courant,verb_iter,false);
						//System.out.println(s);
						//ajout à la phrase en conception
				
				
				
				NPPhraseSpec on=nlgFactory.createNounPhrase("on");

				//gestion d'évitement de la répétition des sujets
				//if(seq & !s.getSubject().getFeature("head").toString().equals(on.getFeature("head").toString())){
					elements.add(s);
					//System.out.println("dans le if");
				//}
				//else{
					//c.addCoordinate(s);	
					//System.out.println("hors du if");
				//}
			}
			
			if((elements.size()>0) ) {
				ClauseCoordinationRule clauseCoord = new ClauseCoordinationRule();
				List<NLGElement> result = clauseCoord.apply(elements);
				//System.out.println(result.size());
				
				if ((result.size()==1) && (!elements.get(0).equals(null))){
					//System.out.println(result.size());
					//System.out.println(result.get(0));
					//l'aggregation a été possible
					NLGElement aggregated = result.get(0);
					
					//c.addCoordinate(aggregated);
					c.addComplement(aggregated);
				}
				else{
					for(int k=0;  k<elements.size();k++){
						NLGElement aggregated = elements.get(k);
						c.addCoordinate(aggregated);
						//c.addComplement(aggregated);
					}
					
				}
			}
			if(verb_iter.size()>0){
				//System.out.println("iteratif");
				List<NLGElement> verb_iterEL= new ArrayList();
				ClauseCoordinationRule clauseCoordIt = new ClauseCoordinationRule();
				WordElement v=frenchlexicon.getWord(verb_iter.get(0), LexicalCategory.NOUN);
				v.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
				v.setFeature(LexicalFeature.PROPER,true);
				prop1=nlgFactory.createClause(v,"pouvoir","être répété");
				verb_iterEL.add(prop1);
				//System.out.println(verb_iter.get(0));
				for(int t=1; t<verb_iter.size();t++){
					WordElement v1=frenchlexicon.getWord(verb_iter.get(t), LexicalCategory.NOUN);
					v1.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
					v1.setFeature(LexicalFeature.PROPER,true);
					NLGElement prop2=nlgFactory.createClause(v1,"pouvoir","être répété");
					verb_iterEL.add(prop2);
					//System.out.println(verb_iterEL.size());
				}
				
				List<NLGElement> resultIT = clauseCoordIt.apply(verb_iterEL);
				//System.out.println(resultIT.size());
			
				if(verb_iterEL.size()>1){
					NLGElement agIt=resultIT.get(0);
					//System.out.println(agIt);
					c1.addComplement(agIt);
				}
				else{
					c1.addComplement(prop1);
				}
				
			}
		
			
			
			String output = realiser.realiseSentence(c);
			//String output = realiser.realiseSentence(aggregated);
			
			
			
			
			//System.out.println(output);
			this.textMdt+=output;
			this.textMdt+="\n";
			
			
			
			if(exemple){

				if(tache_avec_valeur.size()>0){
				//lier la tâche avec un objet pour laquelle on pourrait avec des valeurs
				
					List<Element> tache_avec_valeurOK=new ArrayList();
				
					for (int z=0; z<tache_avec_valeur.size(); z++){
				
					//tache_objet.get(tache_avec_valeur.get(z).getChild("task-name").getText());
						boolean t=false;
						int seqO=0;
						while(seqO<seq_objet.getSize() && !t){
							if(tache_objet.getObjet(tache_avec_valeur.get(z).getChild("task-name").getText())==seq_objet.getInstance(seqO).getObjet()){
							//on a trouvé l'objet
								t=true;
							}
							else{seqO+=1;}
						}
						if(t){
						//this.textMdt+="Par exemple, ";
						//exemple+=seq_objet.get(seqO).objet;
						//exemple+=" = ";
						//exemple+= seq_objet.get(seqO).valeur;
						//this.textMdt+=tache_objet.get(tache_avec_valeur.get(0).getChild("task-name").getText());
							tache_avec_valeurOK.add(tache_avec_valeur.get(z));
						
						}
					
					
					}	
				//pour tester comment faire avec et sans exemple
					if((tache_avec_valeurOK.size()>0) && (exemple) ){
						ecritureTacheAvecExemple(noeudFille, parent, tache_avec_valeurOK);
					}
				
				}
			}
			
			/*if(exemple.length()>1){
				this.textMdt+="Par exemple, ";
				this.textMdt+=exemple;
				exemple="";
				this.textMdt+="\n";
			}*/
			
			this.textMdt+="\n";
			
			//System.out.println(aggregated);
			String output2=realiser.realiseSentence(c1);
			if(output2.length()>0){
				//System.out.println(output2);
				this.textMdt+=output2;
				this.textMdt+="\n";
				this.textMdt+="\n";
			}
			
		}
	
	
	
	
	private void ecritureTache(List noeudFille, Element parent, boolean exemple){
		String op=parent.getChild("task-decomposition").getText();
		if(op.compareTo("SEQ")==0){
			ecritureTacheSeq(noeudFille, parent,  exemple);
			//c.setFeature(Feature.CONJUNCTION, "ou");
			//System.out.println("Je suis dans le cas d'un ET");
		}
		else if(op.compareTo("PAR")==0){
			ecritureTachePar(noeudFille, parent,  exemple);
			}
			else if(op.compareTo("ALT")==0){ecritureTacheAlt(noeudFille, parent,  exemple);}
			else{//if(op.compareTo("ENT")==0){
			ecritureTacheSO(noeudFille, parent,  exemple);
		}
	
	}
	

	
//les tâches élémentaires pour lesquelles on a des valeurs pour l'exemple
private void ecritureTacheAvecExemple(List noeudFille, Element parent, List noeudElemObjet){
	//System.out.println("J'écris les filles");
	Realiser realiser = new Realiser();
	Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
	NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
	
	CoordinatedPhraseElement c=nlgFactory.createCoordinatedPhrase();
	CoordinatedPhraseElement c1=nlgFactory.createCoordinatedPhrase();
	//pour savoir si on est dans une phrase sequentielle
	boolean seq=false;
	boolean alt=false;
	boolean trouveAlt=false;
	//SPhraseSpec proposition= nlgFactory.createClause();
	
	//Pour le traitement en fonction du type d'operateur
	String op=parent.getChild("task-decomposition").getText();
	String action=parent.getChild("task-name").getText();
	action=action.toLowerCase();
	
	
	//dans le cas de l'exemple on va traiter un seul des cas (dans on supprimera le ou)
	if(op.compareTo("ALT")==0){
			seq=false;
			alt=true;
			//PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
			//complementDuVerbe.addPreModifier("pour");
			//c.addPreModifier(complementDuVerbe);
			
			
	}
	
	else{
		
		c.setConjunction("et");
		
		if(op.compareTo("SEQ")==0){
			seq=true;
			alt=false;
			PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
			complementDuVerbe.addPreModifier("pour");
			c.addPreModifier(complementDuVerbe);
			
		}
		if(op.compareTo("PAR")==0){
		
			seq=false;
			alt=false;
			PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
			complementDuVerbe.addPreModifier("pour");
			complementDuVerbe.addPostModifier(", en même temps,");
			c.addPreModifier(complementDuVerbe);
	
		}
	
		if(op.compareTo("ENT")==0){
			seq=false;
			alt=false;
			PPPhraseSpec complementDuVerbe=nlgFactory.createPrepositionPhrase(action);
			complementDuVerbe.addPreModifier("pour");
			complementDuVerbe.addPostModifier(", sans ordre,");
			c.addPreModifier(complementDuVerbe);
		}
		
	}
		
		//on parcours toutes les taches
		Iterator i = noeudFille.iterator();
		//Liste des propositions pour une meme tache abstraite
		List<NLGElement> elements= new ArrayList();
		List<String> verb_iter= new ArrayList();//tableau des verbes iteratifs
		NLGElement prop1;
		SPhraseSpec s=null;
		
		//pour toutes les filles
		while(i.hasNext() ){
			Element courant =(Element)i.next();
			//Ajout de la tâche à l'ensemble des tâches pour l'exemple (le cas échéant)
			String type = courant.getChild("task-decomposition").getText();
			
			//si c'est une feuille
			if(type.compareTo("LEAF")==0){
				//remplacer avec les valeurs
				if(noeudElemObjet.contains((Element)courant)){
					
					if(!courant.getChild("task-name").getText().equals("[1]")){
						s=ecritureTacheElementaire(courant,true, noeudElemObjet);
						elements.add(s);
						if(alt){
							trouveAlt=true;
						}
					}
					else{
						s=ecritureTacheElementaire(courant,false, noeudElemObjet);
					elements.add(s);
						if(alt){
							trouveAlt=true;
						}
					}
				}
				else{
					if(!alt){
					s=ecritureTacheElementaireSeq(courant,verb_iter,true);
					s.setSubject(exec.getUtilisateurEx());
					elements.add(s);
					}
						//System.out.println(s);
						//ajout à la phrase en conception
				
				}
				
				
			}
			//sinon on lance l'ecriture sur elle
			else{
				s=ecritureTacheElementaire(courant,verb_iter);
				s.setSubject(exec.getUtilisateurEx());
				elements.add(s);
					//System.out.println(s);
					//ajout à la phrase en conception
			
			}
			
			//pour tous les enfants
			//construction de la description des sous taches
			
			
			
			NPPhraseSpec on=nlgFactory.createNounPhrase("on");
	
			
		}
		

		if((elements.size()>0) ) {
			if(!alt){
			
				ClauseCoordinationRule clauseCoord = new ClauseCoordinationRule();
				List<NLGElement> result = clauseCoord.apply(elements);
				//System.out.println(result.size());
				
			
				if ((result.size()==1) && (!elements.get(0).equals(null))){
					//System.out.println(result.size());
					//System.out.println(result.get(0));
					//l'aggregation a été possible
					NLGElement aggregated = result.get(0);
				
					//aggregated.setFeature(Feature.TENSE, Tense.PAST);
				
				
					//c.addCoordinate(aggregated);
					c.addComplement(aggregated);
				}
				else{
					for(int k=0;  k<elements.size();k++){
						NLGElement aggregated = elements.get(k);
						//aggregated.setFeature(Feature.TENSE, Tense.PAST);
						
						c.addCoordinate(aggregated);
						//c.addComplement(aggregated);
					}
				
				}
			}
			else{
				for(int k=0;  k<elements.size();k++){
					System.out.println(elements.get(k));
					
				}
				c.addCoordinate((NLGElement)elements.get(0));
			}
		}
		
		if(verb_iter.size()>0){
			//System.out.println("iteratif");
			List<NLGElement> verb_iterEL= new ArrayList();
			ClauseCoordinationRule clauseCoordIt = new ClauseCoordinationRule();
			WordElement v=frenchlexicon.getWord(verb_iter.get(0), LexicalCategory.NOUN);
			v.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
			v.setFeature(LexicalFeature.PROPER,true);
			prop1=nlgFactory.createClause(v,"pouvoir","être répété");
			verb_iterEL.add(prop1);
			//System.out.println(verb_iter.get(0));
			for(int t=1; t<verb_iter.size();t++){
				WordElement v1=frenchlexicon.getWord(verb_iter.get(t), LexicalCategory.NOUN);
				v1.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
				v1.setFeature(LexicalFeature.PROPER,true);
				NLGElement prop2=nlgFactory.createClause(v1,"pouvoir","être répété");
				verb_iterEL.add(prop2);
				//System.out.println(verb_iterEL.size());
			}
			
			List<NLGElement> resultIT = clauseCoordIt.apply(verb_iterEL);
			//System.out.println(resultIT.size());
		
			if(verb_iterEL.size()>1){
				NLGElement agIt=resultIT.get(0);
				//System.out.println(agIt);
				c1.addComplement(agIt);
			}
			else{
				c1.addComplement(prop1);
			}
			
		}

	
	
	c.setFeature(Feature.TENSE, Tense.PAST);
	String output = realiser.realiseSentence(c);
	//String output = realiser.realiseSentence(aggregated);
	
	
	
	
	//System.out.println(output);
	this.textMdt+="Par exemple, ";
	SimpleDateFormat formater = new SimpleDateFormat("'le' dd MMMM yyyy ");
	this.textMdt+=formater.format(dateRando);
	
	char[] char_table=output.toCharArray();
	//mettre premiere lettre de l'output en minuscule
	if(!alt){
		char_table[0]=Character.toLowerCase(char_table[0]);
	}
	output=new String(char_table);
	this.textMdt+=output;
	this.textMdt+="\n";
	
	
	this.textMdt+="\n";
	
	
	
}


	//ex à true signifie que la phrase sera intégrée à une exemplification
	private SPhraseSpec ecritureTacheElementaireSeq(Element tache, List verb_iter, Boolean ex){
		Realiser realiser = new Realiser();
		String sujet=exec.getUtilisateur();//acteur de la tache : en fonction de l'executant
		String verbe;//on considére le premier mot du nom de la tâche comme étant le verbe
		String complement;//le reste du nom de la tâche est un complement
		String nomTache;//le nom complet de la tâche (tel que dans le MdT)
		String typeTache;//l'executant
		String optionnel;//caractére optionnel ou non
		String iter;//caractére iteratif 
		
		
nomTache=tache.getChild("task-name").getText();
		
		typeTache=tache.getChild("task-executant").getText();
		
		optionnel=tache.getChild("task-optional").getText();
		try{
			//System.out.println(tache.getChild("task-descriptioniteration").getText());
			iter=tache.getChild("task-descriptioniteration").getText();
		}
		catch(Exception e){
			iter=tache.getChild("task-iteration").getText();
		}
		
		
		if(!ex){
			System.out.println("Cas général");
			if(typeTache.compareTo("INT")==0){sujet=exec.getInteract();}
		
			if(typeTache.compareTo("ABS")==0){sujet=exec.getAbstrait();}
			if(typeTache.compareTo("SYS")==0){sujet=exec.getSysteme();}
			if(typeTache.compareTo("USER")==0){sujet=exec.getUtilisateur();}
		}
		else{
			System.out.println("Cas de l'exemple");
			if(typeTache.compareTo("INT")==0){
				sujet=exec.getInteractEx();
			}
			
			if(typeTache.compareTo("ABS")==0){
				sujet=exec.getAbstraitEx();
			}
			if(typeTache.compareTo("SYS")==0){
				sujet=exec.getSystemeEx();
			}
			if(typeTache.compareTo("USER")==0){
				sujet=exec.getUtilisateurEx();
			}
		}
		
		
		complement="";
		if(optionnel.compareTo("true")==0){
			//tâche optionelle
			complement+="éventuellement";
			complement+=" ";
		}
		
		
		
		String mot[]=nomTache.split(" ");
		
		//System.out.println(mot[0]);
		if((mot[0].compareTo("Se")!=0) && (mot[0].compareTo("se")!=0)){
			
			verbe=mot[0];
			if(mot.length>1){
				complement+=mot[1];
				complement+=" ";
				for(int k=2;k<mot.length;k++){
					complement+=mot[k];
					complement+=" ";
				}
			}
			}
		//cas des verbes réfléchis
		else{
			
			verbe=mot[0]+" "+mot[1];
			
			if(mot.length>2){
				complement+=mot[2];
				complement+=" ";
				for(int k=3;k<mot.length;k++){
					complement+=mot[k];
					complement+=" ";
				}
			}
			
		}
		verbe=verbe.toLowerCase();
		
		
		if(iter.compareTo("[1]")!=0){
			//tâche iterative
			nomTache=nomTache.toLowerCase();
			verb_iter.add(nomTache);
		}
		
		
		SPhraseSpec ph1;
		
		if(complement.compareTo("")!=0){
			//il y a un complement
			
			ph1=FairePhrase(sujet, verbe,complement,false);
		}
		else{
			
			ph1=FairePhrase(sujet, verbe);
		}
		
		return ph1;
	}
		
	
	
	
	//sans exemple
	private SPhraseSpec ecritureTacheElementaire(Element tache, List verb_iter){
		Realiser realiser = new Realiser();
		String sujet=exec.getUtilisateur();//acteur de la tache : en fonction de l'executant
		String verbe;//on considére le premier mot du nom de la tâche comme étant le verbe
		String complement;//le reste du nom de la tâche est un complement
		String nomTache;//le nom complet de la tâche (tel que dans le MdT)
		String typeTache;//l'executant
		String optionnel;//caractére optionnel ou non
		String iter;//caractére iteratif 
		
		
		
		nomTache=tache.getChild("task-name").getText();
		
		typeTache=tache.getChild("task-executant").getText();
		
		optionnel=tache.getChild("task-optional").getText();
		try{
			//System.out.println(tache.getChild("task-descriptioniteration").getText());
			iter=tache.getChild("task-descriptioniteration").getText();
		}
		catch(Exception e){
			iter=tache.getChild("task-iteration").getText();
		}
		
		
		
		if(typeTache.compareTo("INT")==0){
			sujet=exec.getAbstraitEx();
		}
		
		if(typeTache.compareTo("ABS")==0){
			sujet=exec.getAbstraitEx();
		}
		if(typeTache.compareTo("SYS")==0){
			sujet=exec.getSystemeEx();
		}
		if(typeTache.compareTo("USER")==0){
			sujet=exec.getUtilisateurEx();
		}
		
		
		complement="";
		if(optionnel.compareTo("true")==0){
			//tâche optionelle
			complement+="éventuellement";
			complement+=" ";
		}
		
		
		
		String mot[]=nomTache.split(" ");
		
		//System.out.println(mot[0]);
		if((mot[0].compareTo("Se")!=0) && (mot[0].compareTo("se")!=0)){
			
			verbe=mot[0];
			if(mot.length>1){
				complement+=mot[1];
				complement+=" ";
				for(int k=2;k<mot.length;k++){
					complement+=mot[k];
					complement+=" ";
				}
			}
			}
		//cas des verbes réfléchis
		else{
			
			verbe=mot[0]+" "+mot[1];
			
			if(mot.length>2){
				complement+=mot[2];
				complement+=" ";
				for(int k=3;k<mot.length;k++){
					complement+=mot[k];
					complement+=" ";
				}
			}
			
		}
		verbe=verbe.toLowerCase();
		
		
		if(iter.compareTo("[1]")!=0){
			//tâche iterative
			nomTache=nomTache.toLowerCase();
			verb_iter.add(nomTache);
		}
		
		
		SPhraseSpec ph1;
		
		if(complement.compareTo("")!=0){
			//il y a un complement
			
			ph1=FairePhrase(sujet, verbe,complement,false);
		}
		else{
			
			ph1=FairePhrase(sujet, verbe);
		}
		
		return ph1;
	}
	
	//avec exemple
	private SPhraseSpec ecritureTacheElementaire(Element tache, boolean iter, List no_valeur){
		Realiser realiser = new Realiser();
		String sujet="l'utilisateur";//acteur de la tache : en fonction de l'executant
		String verbe;//on considére le premier mot du nom de la tâche comme étant le verbe
		String complement;//le reste du nom de la tâche est un complement
		String nomTache;//le nom complet de la tâche (tel que dans le MdT)
		String typeTache;//l'executant
		String optionnel;//caractére optionnel ou non
		//String iter;//caractére iteratif 
		
		int exIter;//pour l'exemple d'iteration nombre d' de l'itération
		
		nomTache=tache.getChild("task-name").getText();
		
		typeTache=tache.getChild("task-executant").getText();
		
		optionnel=tache.getChild("task-optional").getText();
		
		
		
		complement="";
		
		
		
		String mot[]=nomTache.split(" ");
		
		System.out.println(mot[0]);
		if((mot[0].compareTo("Se")!=0) && (mot[0].compareTo("se")!=0)){
			
			verbe=mot[0];
			
			if ((mot[1].compareTo("à")==0) || (mot[1].compareTo("au")==0)){
				//c'est un verbe suivi d'une préposition pour complement de lieu
				verbe+=" à";
			}
			
			if (mot[1].compareTo("vers")==0) {
				//c'est un verbe suivi d'une préposition pour complement de lieu
				verbe+=" vers";
			}
			}
		//cas des verbes réfléchis
		else{
			
			verbe=mot[0]+" "+mot[1];
			
			if ((mot[2].compareTo("à")==0) || (mot[2].compareTo("au")==0)){
				//c'est un verbe suivi d'une préposition pour complement de lieu
				verbe+=" à";
			}
			
			if (mot[1].compareTo("vers")==0) {
				//c'est un verbe suivi d'une préposition pour complement de lieu
				verbe+=" vers";
			}
			
		}
		
		String exemple="";
		
		//if(tache_avec_valeur.size()>0){
			//lier la tâche avec un objet pour laquelle on pourrait avec des valeurs
			
			for (int z=0; z<no_valeur.size(); z++){
				//System.out.println("objet "+z);
				//tache_objet.get(tache_avec_valeur.get(z).getChild("task-name").getText());
				boolean t=false;
				int seqO=0;
				
				
				System.out.println("nous sommes dans l'édition de l'exemple pour : "+ tache.getChild("task-name").getText());
				if(tache.getChild("task-decomposition").getText().compareTo("ALT")==0){
					System.out.println("cas de ou");
					iter=false;
				}
				
				
				if(!iter ){
					
					exIter=1;
					while(seqO<seq_objet.getSize() && !t){
					
						Element e = (Element)no_valeur.get(z);
				
						if(tache_objet.getObjet(e.getChild("task-name").getText())==seq_objet.getInstance(seqO).getObjet() && nomTache==e.getChild("task-name").getText() ){
						//on a trouvé l'objet
						//System.out.println("tache mere "+nomTache+" tache fille "+e.getChild("task-name").getText());
						
							t=true;
						}
						else{seqO+=1;}
					}
					if(t){
					complement+= seq_objet.getInstance(seqO).getValeur();
					//this.textMdt+=tache_objet.get(tache_avec_valeur.get(0).getChild("task-name").getText());
					sujet=seq_objet.getInstance(seqO).getProtagoniste();
					}
				}
				
				else{
					//iteratif
					exIter=1;
					
					while(seqO<seq_objet.getSize() && !t){
						
						Element e = (Element)no_valeur.get(z);
					
						if(tache_objet.getObjet(e.getChild("task-name").getText())==seq_objet.getInstance(seqO).getObjet() && nomTache==e.getChild("task-name").getText() ){
							//on a trouvé l'objet
							//System.out.println("tache mere "+nomTache+" tache fille "+e.getChild("task-name").getText());
							t=true;
							
							if(seqO+1<seq_objet.getSize()){
								
								if(tache_objet.getObjet(e.getChild("task-name").getText())==seq_objet.getInstance(seqO+1).getObjet() && nomTache==e.getChild("task-name").getText() ){
									
									exIter=2;
								}
								
							}
							
						}
						else{seqO+=1;}
					}
					if(t){
						complement+= seq_objet.getInstance(seqO).getValeur();
						sujet=seq_objet.getInstance(seqO).getProtagoniste();
						System.out.println(complement);
						for (int numC=1; numC<exIter;numC++){
							complement+= " et ";
							complement+= seq_objet.getInstance(seqO+numC).getValeur();
							sujet=seq_objet.getInstance(seqO).getProtagoniste();
						}
						
						
						//this.textMdt+=tache_objet.get(tache_avec_valeur.get(0).getChild("task-name").getText());
						
						
					}
					}
				
			}	
			
		//}*/
		
		
		verbe=verbe.toLowerCase();
			SPhraseSpec ph1;

			
			ph1=FairePhrase(sujet, verbe,complement, true);

		
			
			ph1.getVerb().setFeature(Feature.TENSE, Tense.PAST);
			//System.out.println(ph1.getVerb().toString());
			
		return ph1;
	}
		
	private SPhraseSpec ecritureTacheElementaireOU(Element tache, List verb_iter, List no_valeur){
		Realiser realiser = new Realiser();
		String sujet="Catherine";//acteur de la tache : en fonction de l'executant
		String verbe;//on considére le premier mot du nom de la tâche comme étant le verbe
		String complement;//le reste du nom de la tâche est un complement
		String nomTache;//le nom complet de la tâche (tel que dans le MdT)
		String typeTache;//l'executant
		String optionnel;//caractére optionnel ou non
		String iter;//caractére iteratif 
		
		
		
		nomTache=tache.getChild("task-name").getText();
		
		typeTache=tache.getChild("task-executant").getText();
		
		optionnel=tache.getChild("task-optional").getText();
		try{
			//System.out.println(tache.getChild("task-descriptioniteration").getText());
			iter=tache.getChild("task-descriptioniteration").getText();
		}
		catch(Exception e){
			iter=tache.getChild("task-iteration").getText();
		}
		
		
		
		if(typeTache.compareTo("INT")==0){
			sujet="Catherine avec le GPS";
		}
		
		if(typeTache.compareTo("ABS")==0){
			sujet="Catherine";
		}
		if(typeTache.compareTo("SYS")==0){
			sujet="le GPS";
		}
		if(typeTache.compareTo("USER")==0){
			sujet="Catherine";
		}
		
		
		complement="";
		/*if(optionnel.compareTo("true")==0){
			//tâche optionelle
			complement+="optionnellement";
			complement+=" ";
		}*/
		
		
		
		String mot[]=nomTache.split(" ");
		
		System.out.println(mot[0]);
		if((mot[0].compareTo("Se")!=0) && (mot[0].compareTo("se")!=0)){
			
			verbe=mot[0];
			/*if(mot.length>1){
				complement+=mot[1];
				complement+=" ";
				for(int k=2;k<mot.length;k++){
					complement+=mot[k];
					complement+=" ";
				}
			}*/
			if ((mot[1].compareTo("à")==0) || (mot[1].compareTo("au")==0)){
				//c'est un verbe suivi d'une préposition pour complement de lieu
				verbe+=" à";
			}
			}
		//cas des verbes réfléchis
		else{
			
			verbe=mot[0]+" "+mot[1];
			
			if ((mot[2].compareTo("à")==0) || (mot[2].compareTo("au")==0)){
				//c'est un verbe suivi d'une préposition pour complement de lieu
				verbe+=" à";
			}
			
			/*if(mot.length>2){
				complement+=mot[2];
				complement+=" ";
				for(int k=3;k<mot.length;k++){
					complement+=mot[k];
					complement+=" ";
				}
			}*/
			
		}
		
		String exemple="";
		
		//if(tache_avec_valeur.size()>0){
			//lier la tâche avec un objet pour laquelle on pourrait avec des valeurs
			
			for (int z=0; z<no_valeur.size(); z++){
				//System.out.println("objet "+z);
				//tache_objet.get(tache_avec_valeur.get(z).getChild("task-name").getText());
				boolean t=false;
				int seqO=0;
				while(seqO<seq_objet.getSize() && !t){
					Element e = (Element)no_valeur.get(z);
				
					if(tache_objet.getObjet(e.getChild("task-name").getText())==seq_objet.getInstance(seqO).getObjet() && nomTache==e.getChild("task-name").getText() ){
						//on a trouvé l'objet
						//System.out.println("tache mere "+nomTache+" tache fille "+e.getChild("task-name").getText());
						t=true;
					}
					else{seqO+=1;}
				}
				if(t){
					//this.textMdt+="Par exemple, ";
					//exemple+=seq_objet.get(seqO).objet;
					//exemple+=" = ";
					//complement+= "le ";
					
					
					
					complement+= seq_objet.getInstance(seqO).getValeur();
					//this.textMdt+=tache_objet.get(tache_avec_valeur.get(0).getChild("task-name").getText());
					
					
				}
				
				
			}	
			
		//}*/
		
		
		verbe=verbe.toLowerCase();
		
		
		if(iter.compareTo("[1]")!=0){
			//tâche iterative
			nomTache=nomTache.toLowerCase();
			verb_iter.add(nomTache);
		}
		
		
		SPhraseSpec ph1;
		
		//if(complement.compareTo("")!=0){
			//il y a un complement
			
			ph1=FairePhrase(sujet, verbe,complement, true);
		//}
		//else{
			
			//ph1=FairePhrase(sujet, verbe);
		//}
		
			
			ph1.getVerb().setFeature(Feature.TENSE, Tense.PAST);
			//System.out.println(ph1.getVerb().toString());
			
		return ph1;
	}
	
	

	
	
	//Toutes les méthodes utiles pour l'écriture en phrase (utilisation de simpleNLG)
	
	private static SPhraseSpec FairePhrase(String sujet, String verbe){
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		SPhraseSpec phrase;
		//phrase=nlgFactory.createClause();
		//phrase.setSubject(sujet);
		
		
		/////////////////////Traitement du verbe
		String verbal[]=verbe.split(" ");
		
		if (verbal.length>1){
			//cas de verbe réfléchi
			phrase=nlgFactory.createClause(sujet, verbal[1], "se");
			
		}
		else{
			phrase=nlgFactory.createClause(sujet, verbe);
		}
		
		


		//System.out.println(verbe);
		//phrase.setVerb(verbe);

		
		return phrase;
	}
	
	//boolean à true si on cherche à faire l'exemple
	private static SPhraseSpec FairePhrase(String sujet, String verbe,String Comp, boolean exemple){
		//Ressources utilisables par tout le programme
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		SPhraseSpec phrase;
		//phrase=nlgFactory.createClause();
		//phrase.setSubject(sujet);
		String prep="";
		
		/////////////////////Traitement du verbe
		String verbal[]=verbe.split(" ");
		
		System.out.println(verbal[0]);
		
		if (verbal[0].compareTo("se")==0){
			//cas de verbe réfléchi
			phrase=nlgFactory.createClause(sujet, verbal[1], "se");
			if(verbal.length>2){
				prep=verbal[2];
			}
		}
		else{
			phrase=nlgFactory.createClause(sujet, verbe);
			if(verbal.length>1){
				prep=verbal[1];
			}
		}
		
		NPPhraseSpec snLeComp = nlgFactory.createNounPhrase(Comp);
		
		if(exemple){
		
		if(prep.compareTo("")!=0){
		PPPhraseSpec spAuComp = nlgFactory.createPrepositionPhrase(prep, snLeComp);
		//System.out.println(verbe);
		//phrase.setVerb(verbe);
		
		phrase.setComplement(spAuComp);
		}
		else{
			phrase.setComplement(snLeComp);
		}
		
		}
		else
			phrase.setComplement(Comp);

		
		return phrase;
	}
	
	private static SPhraseSpec FairePhrase(String sujet, String verbe,String Comp, String COI){
		//Ressources utilisables par tout le programme
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		SPhraseSpec phrase;
		phrase=nlgFactory.createClause();
		phrase.setSubject(sujet);
		phrase.setVerb(verbe);
		//phrase.setObject(COD);
		phrase.setIndirectObject(COI);
		phrase.setComplement(Comp);
		
		return phrase;
	}
	

	/**
	 * Méthode de génération d'un document contenant la version textuelle d'un modéle de tâches
	 * @param nomFichier : nom du document généré
	 * @param auteur : auteur du modéle
	 * @param activite : activité modélisée
	 */
	public void produitDocument(String nomFichier, String auteur, String activite){	
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		Realiser realiser=new Realiser();
		
		String p1="ce document a été généré par la version 0.1 de l'application libre TaskModelReader développée au sein du LIG (Grenoble, France) en s'appuyant sur la librairie JAVA SimpleNLG-EnFr";
		String p2 = "Le modèle de tâches qui fait l'objet de ce rapport est celui de l'activité ";
		p2+=activite;
		p2+=" modélisée par ";
		p2+=auteur;
		p2+=" avec le logiciel k-made (";
		p2+="non renseigné";
		p2+=") dans le fichier ";
		p2+=fichierKMAD;
		String pd="date de génération du rapport : ";
		String format="dd/MM/yy H:mm:ss";
		java.text.SimpleDateFormat formater=new java.text.SimpleDateFormat(format);
		java.util.Date date=new java.util.Date();
		pd+=formater.format(date);
		String t="\nModèle de tâches de l'activité ";
		t+=activite;
		t+="\n";
		
		
		DocumentElement infosPhrase1=nlgFactory.createSentence(p1);
		DocumentElement infosPhrase2=nlgFactory.createDocument(p2);
		DocumentElement infosPhrase3=nlgFactory.createDocument(pd);
		
		DocumentElement descMdT=nlgFactory.createDocument(textMdt);
		
		
		DocumentElement paragraphe1=nlgFactory.createParagraph(infosPhrase1);
		DocumentElement paragraphe2=nlgFactory.createParagraph(infosPhrase2);
		DocumentElement paragraphe3=nlgFactory.createParagraph(infosPhrase3);
		DocumentElement paragraphe4=nlgFactory.createParagraph(descMdT);
		
		List <DocumentElement> listePara=Arrays.asList(paragraphe1,paragraphe2, paragraphe3);
		DocumentElement section1 = nlgFactory.createSection("\nInformations Générales\n",listePara);
		DocumentElement section2 = nlgFactory.createSection("\nDescription textuelle du modèle de tâches\n",paragraphe4);
		
		DocumentElement document=nlgFactory.createDocument(t,section1);
		document.addComponent(section2);
		String realised=realiser.realise(document).getRealisation();
		
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(nomFichier+".txt"));
			out.write(realised);
			out.close();
					
		}
		catch(IOException io){
			System.out.println("pb dans la préparation du fichier");
		}				

	}
}
