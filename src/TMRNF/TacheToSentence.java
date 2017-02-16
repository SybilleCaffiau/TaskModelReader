package TMRNF;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.phrasespec.*;


/**
 * @author sybillecaffiau
 *
 */




public class TacheToSentence implements TacheToSentenceInter {
	private KMADToSentenceStructureInter mdt;
	
	/**
	 * Produit une Phrase simpleNLG composée d'un sujet et d'un verbe
	 * @param sujet : groupe de mot désignant le sujet
	 * @param verbe : groupe verbal
	 * @return : phrase simpleNLG
	 */
	public SPhraseSpec FairePhrase(String sujet, String verbe){
		//Ressources utilisables par tout le programme
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		SPhraseSpec phrase;
		phrase=nlgFactory.createClause();
		phrase.setSubject(sujet);
		phrase.setVerb(verbe);
		
		return phrase;
	}
	
	/**
	 * Produit une Phrase simpleNLG composée d'un sujet, d'un verbe et d'un complement
	 * @param sujet : groupe de mot désignant le sujet
	 * @param verbe : groupe verbal
	 * @param Comp : groupe nominal complément du verbe
	 * @return : phrase simpleNLG
	 */
	public SPhraseSpec FairePhrase(String sujet, String verbe,String Comp){
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

	/**
	 * Produit une Phrase simpleNLG composée d'un sujet, d'un verbe, d'un complement et d'une complement d'objet indirect
	 * @param sujet : groupe de mot désignant le sujet
	 * @param verbe : groupe verbal
	 * @param Comp : groupe nominal complément du verbe
	 * @param COI : groupe nominal complément d'objet indirect
	 * @return : phrase simpleNLG
	 */
	public SPhraseSpec FairePhrase(String sujet, String verbe,String Comp, String COI){
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
		
		
		
		
		//NPPhraseSpec s=nlgFactory.createNounPhrase("le",sujet);
		//NPPhraseSpec v=nlgFactory.createVerbPhrase(verbe);
		//NPPhraseSpec complement=nlgFactory.createNounPhrase("le", "foule");
		

		
		return phrase;
	}

	
	//Peut être à supprimer
	/*public void FaireParagraphe(){
		Realiser realiser = new Realiser();
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
		
		SPhraseSpec s1=nlgFactory.createClause("l'acteur", "regarder", "des photos");
		s1.setFeature(Feature.COMPLEMENTISER, "pour");
		SPhraseSpec s2=nlgFactory.createClause("l'acteur", "regarder", "une photo");
		SPhraseSpec s3=nlgFactory.createClause("l'acteur", "regarder", "un sensemble (pré-défini) de photos");
		
		CoordinatedPhraseElement c=nlgFactory.createCoordinatedPhrase();
		c.addCoordinate(s1);
		c.addCoordinate(s2);
		c.addCoordinate(s3);
		
		String output = realiser.realiseSentence(c);
		System.out.println(output);
	}*/
	
	
	public TacheToSentence(KMADToSentenceStructureInter m){
		mdt=m;
	}

}
