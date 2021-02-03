package fr.eni.javaee.encheres.bo;

import java.time.LocalDateTime;

public class Enchere {
	
	/* Attributs */
	
	private Integer noEnchere;
	private LocalDateTime date;
	private Integer montant;
	private Utilisateur encherisseur;
	private Article article;
	
	/* Constructeurs */
	
	public Enchere() {
		this.date = LocalDateTime.now();
	}
	
	
	
	public Enchere(Integer montant, Utilisateur encherisseur, Article article) {
		this();
		this.montant = montant;
		this.encherisseur = encherisseur;
		this.article = article;
	}

	
	public Enchere(LocalDateTime date, Integer montant, Utilisateur encherisseur, Article article) {
		super();
		this.date = date;
		this.montant = montant;
		this.encherisseur = encherisseur;
		this.article = article;
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

	/* Méthode toString */
	
	public Utilisateur getEncherisseur() {
		return encherisseur;
	}


	public void setEncherisseur(Utilisateur encherisseur) {
		this.encherisseur = encherisseur;
	}


	public Article getArticle() {
		return article;
	}


	public void setArticle(Article article) {
		this.article = article;
	}


	@Override
	public String toString() {
		return "Enchere [noEnchere=" + noEnchere + ", date=" + date + ", montant=" + montant + ", encherisseur=" + encherisseur
				+ ", article=" + article + "]";
	}


}
