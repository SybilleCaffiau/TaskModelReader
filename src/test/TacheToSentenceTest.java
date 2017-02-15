/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import TMRNF.KMADToSentenceStructure;
import TMRNF.KMADToSentenceStructureInter;
import TMRNF.TacheToSentence;
import mock.KMADToSentenceStructureMock;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;

/**
 * @author sybillecaffiau
 *
 */
public class TacheToSentenceTest {
	/**
	 * l'objet qui est testé
	 */
	private static TacheToSentence tts;

	@BeforeClass
	public static void setUpBeforeClass()throws Exception{
		tts=new TacheToSentence(new KMADToSentenceStructureMock());
	}
	/**
	 * Test method for {@link TMRNF.TacheToSentence#FairePhrase(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFairePhraseStringString() {

		//Ressources utilisables par tout le programme
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
				
		SPhraseSpec phrase;
		phrase=nlgFactory.createClause();
		SPhraseSpec phrasetest;
		
		phrase.setSubject("on");
		phrase.setVerb("Gérer");
		
		phrasetest=tts.FairePhrase("on", "Gérer");
		
		assertEquals("cas phrase sujet verbe", phrase, phrasetest);
		
	
		
		phrasetest=tts.FairePhrase("l'utilisateur", "Gérer");
		
		assertNotEquals("cas phrase sujet verbe different", phrase, phrasetest);
		
		phrasetest=tts.FairePhrase("", "Gérer");
		assertNotEquals("cas phrase sujet nul verbe ", phrase, phrasetest);
		assertNotNull("cas phrase sujet nul verbe ", phrasetest);


	}

	/**
	 * Test method for {@link TMRNF.TacheToSentence#FairePhrase(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFairePhraseStringStringString() {
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
				
		SPhraseSpec phrase;
		phrase=nlgFactory.createClause();
		SPhraseSpec phrasetest;
		
		phrase.setSubject("on");
		phrase.setVerb("Gérer");
		
		
		phrasetest=tts.FairePhrase("on", "Gérer","les messages");
		
		assertNotEquals("cas phrase sujet verbe complement et non complement", phrase, phrasetest);
		
		phrase.setComplement("les messages");
		assertEquals("cas phrase sujet verbe complement", phrase, phrasetest);
	}
	
	/**
	 * Test method for {@link TMRNF.TacheToSentence#FairePhrase(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFairePhraseStringStringStringString() {
		Lexicon frenchlexicon = new simplenlg.lexicon.french.XMLLexicon();
		NLGFactory nlgFactory = new NLGFactory(frenchlexicon);
				
		SPhraseSpec phrase;
		phrase=nlgFactory.createClause();
		SPhraseSpec phrasetest;
		
		phrase.setSubject("on");
		phrase.setVerb("Gérer");
		
		
		phrasetest=tts.FairePhrase("on", "Gérer","les messages", "Sophie");
		
		assertNotEquals("cas phrase sujet verbe complement et non complement", phrase, phrasetest);
		
		phrase.setComplement("les messages");
		phrase.setIndirectObject("Sophie");
		assertEquals("cas phrase sujet verbe complement", phrase, phrasetest);
	}


}
