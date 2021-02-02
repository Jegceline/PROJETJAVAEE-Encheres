package fr.eni.javaee.encheres.bo;

import java.time.LocalDate;

public class Article {

	private Integer noArticle;
	private String nomArticle;
	private String description;
	private LocalDate dateDebutEncheres;
	private LocalDate dateFinEncheres;
	private Integer prixInitial;
	private Integer prixVente;
	private String etatVente;
	private Adresse adresseRetrait;
	private Utilisateur vendeur;
	private Categorie categorie;

	private Integer noUtilisateur;
	private Integer noCategorie;
	// private String pseudoVendeur;

	/* Constructeurs */

	public Article() {
	}

	public Article(String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, Integer prixInitial,
			String etatVente, Integer noUtilisateur, Integer noCategorie, Adresse adresseRetrait) {
		super();
		this.nomArticle = nomArticle;
		this.description = description;
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.prixInitial = prixInitial;
		this.etatVente = etatVente;
		this.noUtilisateur = noUtilisateur;
		this.noCategorie = noCategorie;
		this.adresseRetrait = adresseRetrait;
	}

	public Integer getNoArticle() {
		return noArticle;
	}

	public void setNoArticle(Integer noArticle) {
		this.noArticle = noArticle;
	}

	public String getNomArticle() {
		return nomArticle;
	}

	public void setNomArticle(String nomArticle) {
		this.nomArticle = nomArticle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDateDebutEncheres() {
		return dateDebutEncheres;
	}

	public void setDateDebutEncheres(LocalDate dateDebutEncheres) {
		this.dateDebutEncheres = dateDebutEncheres;
	}

	public LocalDate getDateFinEncheres() {
		return dateFinEncheres;
	}

	public void setDateFinEncheres(LocalDate dateFinEncheres) {
		this.dateFinEncheres = dateFinEncheres;
	}

	public Integer getPrixInitial() {
		return prixInitial;
	}

	public void setPrixInitial(Integer prixInitial) {
		this.prixInitial = prixInitial;
	}

	public Integer getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(Integer prixVente) {
		this.prixVente = prixVente;
	}

	public String getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(String etatVente) {
		this.etatVente = etatVente;
	}

	public Integer getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(Integer noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public Integer getNoCategorie() {
		return noCategorie;
	}

	public void setNoCategorie(Integer noCategorie) {
		this.noCategorie = noCategorie;
	}

	public Adresse getAdresseRetrait() {
		return adresseRetrait;
	}

	public void setAdresseRetrait(Adresse adresseRetrait) {
		this.adresseRetrait = adresseRetrait;
	}

	public Utilisateur getVendeur() {
		return vendeur;
	}

	public void setVendeur(Utilisateur vendeur) {
		this.vendeur = vendeur;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	
	
	@Override
	public String toString() {
		return "Article [noArticle=" + noArticle + ", nomArticle=" + nomArticle + ", description=" + description + ", dateDebutEncheres="
				+ dateDebutEncheres + ", dateFinEncheres=" + dateFinEncheres + ", prixInitial=" + prixInitial + ", prixVente=" + prixVente
				+ ", etatVente=" + etatVente + ", adresseRetrait=" + adresseRetrait + ", vendeur=" + vendeur + ", categorie=" + categorie
				+ ", noUtilisateur=" + noUtilisateur + ", noCategorie=" + noCategorie + "]";
	}


}
