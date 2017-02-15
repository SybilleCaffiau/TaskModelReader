/**
 * 
 */
package TMRNF;



/**
 * @author sybillecaffiau
 *
 */
public interface KMADToSentenceStructureInter { 

	/**
	 * Assesseur au nom du fichier xml traité (temporaire)
	 * @return nom du fichier .xml qui est en traitement (fichier temporaire copié du kxml)
	 */
	public String getNomFichierKMAD(); 
	
	/**
	 * Méthode qui affiche en console le DOM correspondant aux informations du fichier xml
	 */
	public void afficheDocumentSource(); 
	
	/**
	 * Assesseur de la version textuelle généree
	 * @return le texte représentant le modéle de tâches
	 */
	public String getTextMdT();
	
	/**
	 * Methode de traduction du xml vers du texte
	 */
	public void ecritureMdT(); 
	
	/**
	 * Méthode de génération d'un document contenant la version textuelle d'un modéle de tâches
	 * @param nomFichier : nom du document généré
	 * @param auteur : auteur du modéle
	 * @param activite : activité modélisée
	 */
	public void produitDocument(String nomFichier, String auteur, String activite); 
	
	
}
