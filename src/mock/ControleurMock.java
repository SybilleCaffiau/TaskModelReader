/**
 * 
 */
package mock;

import java.io.IOException;

import TMRUI.ControleurInter;

/**
 * @author sybillecaffiau
 *
 */
public class ControleurMock implements ControleurInter {
	public void prepareFichier(String chemin) throws IOException {
		
	}
	
	public String ecrit(){
		return("");
	}
	
	public void sauve(String fichier, String auteur, String act){
		
	}

	@Override
	public void SupprFichierTemp() {
		// TODO Auto-generated method stub
		
	}
	
}
