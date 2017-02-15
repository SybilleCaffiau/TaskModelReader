package TMRUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import TMRNF.KMADToSentenceStructure;

/**
 * @author sybillecaffiau
 *
 */
public class Controleur implements ControleurInter {
	
	//NF est pour un seul modele de tâches (un fichier)
	private KMADToSentenceStructure NF;

	//Nom du fichier fixé et fichier non supprimé pour le moment
	private String nomFichierTemp;
	private File temp;
	
	
	//le NF est associé à un fichier K-MAD donc on n'associe le controleur avec le NF que lorsqu'on veut faire un traitement dessus
	public Controleur(){
		//NF=new KMADToSentenceStructure();
		nomFichierTemp="testTR.xml";
	}
	
	/**
	 * Méthode qui transforme un fichier .kxml en fichier xml interprétable par le système
	 * @param chemin : chemin du fichier .kxml à traiter
	 */
	public void prepareFichier(String chemin) throws IOException{
		//ouvre le fichier kxml à traiter, cree une copie, fais les modifs
		String line;
		StringBuffer sb = new StringBuffer();
		int nbLinesRead=0;
		
		System.out.println("dans controleur");
		System.out.println(chemin);
		try{
			FileInputStream fis = new FileInputStream(chemin);
			BufferedReader reader = new BufferedReader (new InputStreamReader(fis));
			
			while((line=reader.readLine())!=null){
				nbLinesRead++;
				if(nbLinesRead!=2){
					sb.append(line+"\n");
				}
			}
			reader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(nomFichierTemp));
			out.write(sb.toString());
			out.close();
			
		}
		catch(IOException io){
			System.out.println("pb dans la préparation du fichier");
		}
	}
	
	/**
	 * Methode qui ordonne la transformation du contenu du fichier temporaire (xml) en texte en langage naturel (par le NF)
	 */
	public String ecrit(){

		NF = new KMADToSentenceStructure(nomFichierTemp.toString());
		NF.ecritureMdT();
		return(NF.getTextMdT());
	}
	
	/**
	 * Methode qui ordonne la transformation du le contenu du fichier temporaire (xml) en texte en langage naturel (par le NF) et le sauvegarde dans un fichier Sauvegarde.txt sous forme de document
	 * 
	 * @param fichier : le nom du fichier de sauvegarde
	 * @param auteur : le nom de l'auteur du modèle
	 * @param act : le nom de l'activité modélisée
	 */
	public void sauve(String fichier, String auteur, String act){
		NF = new KMADToSentenceStructure(nomFichierTemp.toString());
		
		NF.ecritureMdT();
		
		NF.produitDocument(fichier,auteur, act);
	}
	
	/**
	 * Méthode qui supprime le fichier temporaire .xml (fichier copie de K-MADe mais au format)
	 */
	public void SupprFichierTemp(){
		temp=new File(nomFichierTemp);
		temp.delete();
	}

}
