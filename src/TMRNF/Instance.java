package TMRNF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Instance {
	private String valeur;
	private String objet;
	private Date d;
	
	public Instance(String objet, String valeur, String d){
		this.objet=objet;
		this.valeur=valeur;
		try{
			this.d=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
			//this.d=new SimpleDateFormat("yyyy-MM-dd").parse(d);
		} catch(ParseException e){
			e.printStackTrace();
		}
		
	}
	
	public String getValeur(){
		return this.valeur;
	}
	
	public String getObjet(){
		return this.objet;
	}
	
	
	public Date getDate(){
		return this.d;
	}
	
	
	public void setValeur(String v){
		this.valeur=v;
	}
	
	public void setObjet(String o){
		this.objet=o;
	}
	
	public void setDate(Date d){
		this.d=d;
	}
	
}
