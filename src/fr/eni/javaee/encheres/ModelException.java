package fr.eni.javaee.encheres;

import java.util.HashMap;
import java.util.Map;

public class ModelException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, String> mapErreurs = new HashMap<Integer, String>();
	
	
	/* Constructeur */

	public ModelException() {
	}
	
	
	/* Getters & Setters */
	
	public Map<Integer, String> getMapErreurs() {
		return mapErreurs;
	}

	public void setMapErreurs(Map<Integer, String> mapErreurs) {
		this.mapErreurs = mapErreurs;
	}

	
	/* Méthodes */

	public boolean contientErreurs() {
		return this.mapErreurs.size()>0;
	}
	
	public void ajouterErreur(Integer codeErreur, String messageErreur) {
		mapErreurs.put(codeErreur, messageErreur);
	}
	

}
