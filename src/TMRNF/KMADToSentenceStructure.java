package TMRNF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
	
	
	public KMADToSentenceStructure(String nomFichier){
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
		//enregistre();
		//editXML();
		
	}
	
	//peut être supprimer
	/*private Element getRacine(){
		return racine;
	}*/
	
	
	//peut être supprimer
	/*private void enregistre(){
		try{
			XMLOutputter sortie=new XMLOutputter(Format.getPrettyFormat());
			sortie.output(docSource, new FileOutputStream("document.xml"));
		}
		catch (java.io.IOException e){
			System.out.println("probléme lors de l'enregistrement");
		}
	}*/
	
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
	public void ecritureMdT(){
		//la racine du document 
		Element root=docSource.getRootElement();		
		List listeTaches = root.getChildren("task");
		//la liste ne retourne qu'un suel objet : la tache racine
		ecritureTacheITer((Element)listeTaches.get(0));
		
	}
	
	private void ecritureTacheITer(Element mere){
		List listeTaches = mere.getChildren("task");
		
		
		if(listeTaches.size()>0){
			//si elle a des filles (tache décomposée), on l'affiche puis on lance l'algo sur les filles
			ecritureTache(listeTaches,mere);
			
			Iterator i = listeTaches.iterator();
			while(i.hasNext()){
				
				Element courant=(Element)i.next();
				
				//System.out.println("Dans ecriture MdT pour la tache "+courant.getChild("task-name").getText());
				ecritureTacheITer(courant);
				
			}
		}
		
		

	}
	


	
	
private void ecritureTache(List noeudFille, Element parent){
		
		Realiser realiser = new Realiser();
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		CoordinatedPhraseElement c=nlgFactory.createCoordinatedPhrase();
		CoordinatedPhraseElement c1=nlgFactory.createCoordinatedPhrase();
		//pour savoir si on est dans une phrase sequentielle
		boolean seq=false;
		//SPhraseSpec proposition= nlgFactory.createClause();
		
		//Pour le traitement en fonction du type d'operateur
		String op=parent.getChild("task-decomposition").getText();
		if(op.compareTo("SEQ")==0){
			seq=true;
			//c.setFeature(Feature.CONJUNCTION, "ou");
			//System.out.println("Je suis dans le cas d'un ET");
			c.setConjunction("et");
		}
		if(op.compareTo("PAR")==0){
			//lien="et en même temps ";
			seq=false;
			//System.out.println("Je suis dans le cas d'un ET en même temps");
			c.setFeature(Feature.CONJUNCTION, "et en même temps");
		}
		if(op.compareTo("ALT")==0){
			seq=false;
			//c.setFeature(Feature.CONJUNCTION, "ou");
			//System.out.println("Je suis dans le cas d'un OU");
			c.setConjunction("ou");
		}
		if(op.compareTo("ENT")==0){
			seq=false;
			//lien="et sans ordre ";
			//System.out.println("Je suis dans le cas d'un ET sans ordre");
			c.setFeature(Feature.CONJUNCTION, "et sans ordre");
		}
		
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
		while(i.hasNext()){
			Element courant =(Element)i.next();
			
			//pour tous les enfants
			//construction de la description des sous taches
			
			SPhraseSpec s=ecritureTacheElementaire(courant,verb_iter);
					//System.out.println(s);
					//ajout à la phrase en conception
			
			
			
			NPPhraseSpec on=nlgFactory.createNounPhrase("on");

			
			if(seq & !s.getSubject().getFeature("head").toString().equals(on.getFeature("head").toString())){
				elements.add(s);
			}
			else{
				c.addCoordinate(s);	
				
			}
		}
		
		if((elements.size()>0) ) {
			ClauseCoordinationRule clauseCoord = new ClauseCoordinationRule();
			List<NLGElement> result = clauseCoord.apply(elements);
			System.out.println(result.size());
			
			if ((result.size()==1) && (!elements.get(0).equals(null))){
				System.out.println(result.size());
				System.out.println(result.get(0));
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
		
		//System.out.println(aggregated);
		String output2=realiser.realiseSentence(c1);
		if(output2.length()>0){
			//System.out.println(output2);
			this.textMdt+=output2;
			this.textMdt+="\n";
		}
		
	}
	
	
	
	
	
	private SPhraseSpec ecritureTacheElementaire(Element tache, List verb_iter){
		Realiser realiser = new Realiser();
		String sujet="l'utilisateur";//acteur de la tache : en fonction de l'executant
		String verbe;//on considére le premier mot du nom de la tâche comme étant le verbe
		String complement;//le reste du nom de la tâche est un complement
		String nomTache;//le nom complet de la tâche (tel que dans le MdT)
		String typeTache;//l'executant
		String optionnel;//caractére optionnel ou non
		String iter;//caractére iteratif 
		
		
		
		nomTache=tache.getChild("task-name").getText();
		
		typeTache=tache.getChild("task-executant").getText();
		
		optionnel=tache.getChild("task-optional").getText();
		
		iter=tache.getChild("task-iteration").getText();
		
		
		if(typeTache.compareTo("INT")==0){
			sujet="l'utilisateur";
		}
		
		if(typeTache.compareTo("ABS")==0){
			sujet="on";
		}
		if(typeTache.compareTo("SYS")==0){
			sujet="le système";
		}
		if(typeTache.compareTo("USER")==0){
			sujet="l'utilisateur sans le logiciel";
		}
		
		String mot[]=nomTache.split(" ");
		verbe=mot[0];
		verbe=verbe.toLowerCase();
		complement="";
		if(optionnel.compareTo("true")==0){
			//tâche optionelle
			complement+="optionnellement";
			complement+=" ";
		}
		if(iter.compareTo("[1]")!=0){
			//tâche iterative
			nomTache=nomTache.toLowerCase();
			verb_iter.add(nomTache);
		}
		if(mot.length>1){
			complement+=mot[1];
			complement+=" ";
			for(int k=2;k<mot.length;k++){
				complement+=mot[k];
				complement+=" ";
			}
		}
		
		SPhraseSpec ph1;
		
		if(mot.length>1){
			//il y a un complement
			ph1=FairePhrase(sujet, verbe,complement);
		}
		else{
			ph1=FairePhrase(sujet, verbe);
		}
		
		return ph1;
	}
	

	
	
	//Toutes les méthodes utiles pour l'écriture en phrase (utilisation de simpleNLG)
	
	private static SPhraseSpec FairePhrase(String sujet, String verbe){
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		SPhraseSpec phrase;
		phrase=nlgFactory.createClause();
		
		phrase.setSubject(sujet);
		phrase.setVerb(verbe);
		
		return phrase;
	}
	
	
	private static SPhraseSpec FairePhrase(String sujet, String verbe,String Comp){
		//Ressources utilisables par tout le programme
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		SPhraseSpec phrase;
		phrase=nlgFactory.createClause();
		phrase.setSubject(sujet);
		phrase.setVerb(verbe);
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
