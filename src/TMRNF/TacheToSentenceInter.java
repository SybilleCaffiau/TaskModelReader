/**
 * 
 */
package TMRNF;


import simplenlg.phrasespec.SPhraseSpec;

/**
 * @author sybillecaffiau
 *
 */
public interface TacheToSentenceInter {

	/**
	 * Produit une Phrase simpleNLG composée d'un sujet et d'un verbe
	 * @param sujet : groupe de mot désignant le sujet
	 * @param verbe : groupe verbal
	 * @return : phrase simpleNLG
	 */
	public SPhraseSpec FairePhrase(String sujet, String verbe);
	
	/**
	 * Produit une Phrase simpleNLG composée d'un sujet, d'un verbe et d'un complement
	 * @param sujet : groupe de mot désignant le sujet
	 * @param verbe : groupe verbal
	 * @param Comp : groupe nominal complément du verbe
	 * @return : phrase simpleNLG
	 */
	public SPhraseSpec FairePhrase(String sujet, String verbe,String Comp);
	
	/**
	 * Produit une Phrase simpleNLG composée d'un sujet, d'un verbe, d'un complement et d'une complement d'objet indirect
	 * @param sujet : groupe de mot désignant le sujet
	 * @param verbe : groupe verbal
	 * @param Comp : groupe nominal complément du verbe
	 * @param COI : groupe nominal complément d'objet indirect
	 * @return : phrase simpleNLG
	 */
	public SPhraseSpec FairePhrase(String sujet, String verbe,String Comp, String COI);
}
