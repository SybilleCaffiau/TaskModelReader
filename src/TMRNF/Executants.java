package TMRNF;

public class Executants {
	private String operateur;
	private String systeme;
	private String abs;
	private String interact;
	
	public Executants(String utilisateur, String systeme, String abstrait, String interaction){
		this.operateur=utilisateur;
		this.systeme=systeme;
		this.abs=abstrait;
		this.interact=interaction;
	}
	
	public Executants(){
		this.operateur="le randonneur";
		this.systeme="le système";
		this.abs="on";
		this.interact="le randonneur";
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
}
