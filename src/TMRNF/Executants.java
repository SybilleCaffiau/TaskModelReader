package TMRNF;


public class Executants {
	private String operateur;
	private String systeme;
	private String abs;
	private String interact;
	
	
	
	private String operateurExemple;
	private String systemeExemple;
	private String absExemple;
	private String interactExemple;
	
	//pour le domaine, puis lié aux exemples
	public Executants(String utilisateur, String systeme, String abstrait, String interaction, String uE, String sE, String aE, String iE){
		this.operateur=utilisateur;
		this.systeme=systeme;
		this.abs=abstrait;
		this.interact=interaction;
		
		
		this.operateurExemple=uE;
		this.systemeExemple=sE;
		this.absExemple=aE;
		this.interactExemple=iE;
	}

	public String getUtilisateur(){
		return this.operateur;
	}
	public String getAbstrait(){
		return this.abs;
	}
	public String getSysteme(){
		return this.systeme;
	}
	public String getInteract(){
		return this.interact;
	}
	
	public String getUtilisateurEx(){
		return this.operateurExemple;
	}
	public String getAbstraitEx(){
		return this.absExemple;
	}
	public String getSystemeEx(){
		return this.systemeExemple;
	}
	public String getInteractEx(){
		return this.interactExemple;
	}
	
}
