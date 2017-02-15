/**
 * 
 */
package TMRUI;

import java.io.IOException;


/**
 * @author sybillecaffiau
 *
 */
public interface ControleurInter {

	/**
	 * Méthode qui transforme un fichier .kxml en fichier xml interprétable par le système
	 * @param chemin : chemin du fichier .kxml à traiter
	 */
	public void prepareFichier(String chemin) throws IOException ;
	
	/**
	 * Methode qui ordonne la transformation du le contenu du fichier temporaire (xml) en texte en langage naturel (par le NF)
	 */
	public String ecrit();
	
	/**
	 * Methode qui ordonne la transformation du le contenu du fichier temporaire (xml) en texte en langage naturel (par le NF) et le sauvegarde dans un fichier Sauvegarde.txt sous forme de document
	 * 
	 * @param fichier : le nom du fichier de sauvegarde
	 * @param auteur : le nom de l'auteur du modèle
	 * @param act : le nom de l'activité modélisée
	 */
	public void sauve(String fichier, String auteur, String act); 
	
	
	/**
	 * Méthode qui supprime le fichier temporaire .xml (fichier copie de K-MADe mais au format)
	 */
	public void SupprFichierTemp();
	
}
