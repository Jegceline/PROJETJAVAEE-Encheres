package fr.eni.javaee.encheres.bo;

import java.time.LocalDateTime;

public class Enchere {
	
	/* Attributs */
	
	private Integer noEnchere;
	private LocalDateTime date;
	private Integer montant;
	private Integer noArticle;
	private Integer noUtilisateur;
	
	/* Constructeurs */
	
	public Enchere() {
		this.date = LocalDateTime.now();
	}
	
	
	public Enchere(Integer montant, Integer noArticle, Integer noUtilisateur) {
		this();
		this.montant = montant;
		this.noArticle = noArticle;
		this.noUtilisateur = noUtilisateur;
	}


	/* Getters & Setters */

	public Integer getNoEnchere() {
		return noEnchere;
	}

	public void setNoEnchere(Integer noEnchere) {
		this.noEnchere = noEnchere;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Integer getMontant() {
		return montant;
	}

	public void setMontant(Integer montant) {
		this.montant = montant;
	}

	public Integer getNoArticle() {
		return noArticle;
	}

	public void setNoArticle(Integer noArticle) {
		this.noArticle = noArticle;
	}

	public Integer getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(Integer noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	/* Méthode toString */
	
	@Override
	public String toString() {
		return "Enchere [noEnchere=" + noEnchere + ", date=" + date + ", montant=" + montant + ", noArticle=" + noArticle
				+ ", noUtilisateur=" + noUtilisateur + "]";
	}

	
	
	
	
	

}
