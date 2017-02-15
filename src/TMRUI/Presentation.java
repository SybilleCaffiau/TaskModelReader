package TMRUI;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

/**
 * @author sybillecaffiau
 *
 */
public class Presentation extends JFrame {
	
	private JPanel pannelPrincipal;
	
	//private JTabbedPane onglets;
	private JPanel panelOngletSemantique;
	
	private JPanel panelTitreSemantique, panelKxmlSemantique, panelTexteSemantique;
	
	//les composants du panel panelTitreSemantique
	private JLabel labelNomTitreSemantique;
	private JTextField tfNomTitreSemantique;
	
	//Les composants du panel panelkxmlSemantique
	private JLabel labelTitreKxmlSemantique;
	private JLabel labelTitreCheminKxmlSemantique, labelCheminKxmlSemantique;
	private JButton bCheminKxmlSemantique, bSupprimerKxmlSemantique;
	private JFileChooser fenRechercheKxml;
	private JFileChooser fenRechercheSave;
	
	//Les composants du panel panelTexteSemantique
	private JLabel labelTitreTexteSemantique;
	private JButton bCreerTexteSemantique, bSauvegarderTexteSemantique;
	private JTextArea taTexteGenereTexteSemantique;
	
	//pour transmettre
	File fichier,fichierSave;
	private Controleur monControleur;
	
	Presentation(){
		super("TaskModelReader");
		monControleur=new Controleur();
		init();
		this.setSize(800,400);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				//supprimer le fichier temporaire testTR
				monControleur.SupprFichierTemp();
				System.exit(0);
			}
			public void windowClosed(WindowEvent e){
				//supprimer le fichier temporaire testTR
				monControleur.SupprFichierTemp();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Méthode d'initialisation de tous les widgets de l'UI
	 */
	private void init(){
		//Le pannel principal
		pannelPrincipal=new JPanel();
		
		//les onglets
		//onglets = new JTabbedPane(SwingConstants.TOP);
		
		panelOngletSemantique=new JPanel();
		//onglets.addTab("Sémantique du modèle de tâches", panelOngletSemantique);
		
		//onglets.setOpaque(true);
		
		
		panelTitreSemantique=new JPanel(); 
		panelKxmlSemantique= new JPanel(); 
		panelTexteSemantique=new JPanel();
		
		panelTitreSemantique.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		panelKxmlSemantique.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		panelTexteSemantique.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		//BorderLayout layoutPannelOnglet=new BorderLayout();
		
		//BorderLayout layoutTexteSemantique=new BorderLayout();
		
		
		
		
		//les composants du panel panelTitreSemantique
		labelNomTitreSemantique= new JLabel("Modèle de tâches de l'activité");
		labelNomTitreSemantique.setFont(new Font("Courier New", Font.BOLD, 16));
		tfNomTitreSemantique=new JTextField(20);
		
		//Les composants du panel panelkxmlSemantique
		labelTitreKxmlSemantique=new JLabel("Le modèle K-MAD (au formal kxml) : ");
		labelTitreKxmlSemantique.setFont(new Font("Courier New", Font.BOLD, 14));
		labelTitreCheminKxmlSemantique=new JLabel("Chemin d'accés :");
		labelCheminKxmlSemantique= new JLabel("");
		labelCheminKxmlSemantique.setFont(new Font("Courier New", Font.ITALIC, 12));
		bCheminKxmlSemantique=new JButton("Rechercher sur ordinateur"); 

		bSupprimerKxmlSemantique=new JButton("Supprimer le fichier");
		bSupprimerKxmlSemantique.setEnabled(false);
		bSupprimerKxmlSemantique.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				labelCheminKxmlSemantique.setText("");
				taTexteGenereTexteSemantique.setText("");
				bSauvegarderTexteSemantique.setEnabled(false);
				bCreerTexteSemantique.setEnabled(false);
				bCheminKxmlSemantique.setEnabled(true);
				bCheminKxmlSemantique.setText("Rechercher sur ordinateur");
				bSupprimerKxmlSemantique.setEnabled(false);
			}
		});
		
		
		bCheminKxmlSemantique.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fenRechercheKxml=new JFileChooser(new File("."));
				//fenRechercheKxml.addChoosableFileFilter(new FileFilter("Fichiers KMAD", ".kxml"));
				
				if(fenRechercheKxml.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
					fichier=fenRechercheKxml.getSelectedFile();
					bCheminKxmlSemantique.setText("Changer le fichier");
					labelCheminKxmlSemantique.setText(fenRechercheKxml.getSelectedFile().getAbsolutePath());
					bCreerTexteSemantique.setEnabled(true);
	
					bSupprimerKxmlSemantique.setEnabled(true);
				}
			}
			
		});
		
		
		//Les composants du panel panelTexteSemantique
		labelTitreTexteSemantique=new JLabel("Le modèle textuel :");
		labelTitreTexteSemantique.setFont(new Font("Courier New", Font.BOLD, 14));
		bCreerTexteSemantique= new JButton("Créer le texte"); 
		bCreerTexteSemantique.setEnabled(false);
		bCreerTexteSemantique.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				try{
					monControleur.prepareFichier(labelCheminKxmlSemantique.getText());
					taTexteGenereTexteSemantique.setText(monControleur.ecrit());
					bSauvegarderTexteSemantique.setEnabled(true);
					bCreerTexteSemantique.setEnabled(false);
				}
				catch(IOException ie){}
			}
		});
		bSauvegarderTexteSemantique= new JButton("Sauvegarder dans un document text (format txt)");
		bSauvegarderTexteSemantique.setEnabled(false);
		bSauvegarderTexteSemantique.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String cheminFichier;
				String nomFichier="Sauvegarde.txt";
				
				fenRechercheSave=new JFileChooser(new File("."));
				fenRechercheSave.setDialogTitle("Enregistrer...");
				//fenRechercheKxml.addChoosableFileFilter(new FileFilter("Fichiers KMAD", ".kxml"));
				
				if(fenRechercheSave.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
					
					
					
					
					fichierSave=fenRechercheSave.getSelectedFile();
					
					cheminFichier = fenRechercheSave.getSelectedFile().getAbsolutePath();
					
					monControleur.sauve(cheminFichier, "non renseigné", tfNomTitreSemantique.getText());
				
				}
				
				
				
				
				
				
			}
		});
		taTexteGenereTexteSemantique=new JTextArea();
		
		
		panelTitreSemantique.add(labelNomTitreSemantique);
		panelTitreSemantique.add(tfNomTitreSemantique);
		
		panelKxmlSemantique.setLayout(new GridLayout(2,1,5,5));
		JPanel panelKxmlSemantique2=new JPanel();
		panelKxmlSemantique2.setLayout(new GridLayout(1,4,5,5));
		
		panelKxmlSemantique2.add(labelTitreCheminKxmlSemantique);
		panelKxmlSemantique2.add(labelCheminKxmlSemantique);
		panelKxmlSemantique2.add(bCheminKxmlSemantique);

	
		panelKxmlSemantique2.add(bSupprimerKxmlSemantique);
		panelKxmlSemantique.add(labelTitreKxmlSemantique);
		panelKxmlSemantique.add(panelKxmlSemantique2);
		
		panelTexteSemantique.setLayout(new BorderLayout(5,5));
		JPanel panelTextHaut=new JPanel();
		//panelTextHaut.setLayout(new GridLayout(2,1));
		panelTextHaut.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelTextHaut.add(labelTitreTexteSemantique);
		panelTextHaut.add(bCreerTexteSemantique);
		JPanel panelTextBas=new JPanel();
		panelTextBas.add(bSauvegarderTexteSemantique);
		
		panelTexteSemantique.add(panelTextHaut, BorderLayout.NORTH);
		panelTexteSemantique.add(panelTextBas, BorderLayout.SOUTH);
		panelTexteSemantique.add(taTexteGenereTexteSemantique, BorderLayout.CENTER);
		
		JPanel panelOngletHaut=new JPanel();
		panelOngletHaut.setLayout(new GridLayout(2,1, 5, 5));
		panelOngletHaut.add(panelTitreSemantique);
		
		panelOngletHaut.add(panelKxmlSemantique);
		panelOngletSemantique.setLayout(new BorderLayout(5,5));

		panelOngletSemantique.add(panelOngletHaut, BorderLayout.NORTH);
		panelOngletSemantique.add(panelTexteSemantique, BorderLayout.CENTER);
		
		pannelPrincipal.setLayout(new BorderLayout(5,5));
		
		//pannelPrincipal.add(onglets);
		pannelPrincipal.add(panelOngletSemantique, BorderLayout.CENTER);
		
		this.getContentPane().add(pannelPrincipal);
		//this.pack();
		
	}
	

}
