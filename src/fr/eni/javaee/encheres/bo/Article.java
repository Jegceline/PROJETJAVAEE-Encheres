package fr.eni.javaee.encheres.bo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Article {

	private Integer noArticle;
	private String nomArticle;
	private String description;
	private LocalDate dateDebutEncheres;
	private LocalTime heureDebutEncheres;
	private LocalDate dateFinEncheres;
	private LocalTime heureFinEncheres;
	private Integer prixInitial;
	private Integer prixVente;
	private Adresse adresseRetrait;
	private Utilisateur vendeur;
	private Categorie categorie;
	private Utilisateur dernierEncherisseur;


	/* Constructeurs */

	public Article() {
	}

	public Article(String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, Integer prixInitial, Categorie categorie, Utilisateur vendeur, Adresse adresseRetrait) {
		this.nomArticle = nomArticle.trim();
		this.description = description.trim();
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.prixInitial = prixInitial;
		this.categorie = categorie;
		this.vendeur = vendeur;
		this.adresseRetrait = adresseRetrait;
	}
	

	public Article(String nomArticle, String description, LocalDate dateDebutEncheres, LocalTime heureDebutEncheres,
			LocalDate dateFinEncheres, LocalTime heureFinEncheres, Integer prixInitial, Categorie categorie, Utilisateur vendeur, Adresse adresseRetrait) {
		super();
		this.nomArticle = nomArticle.trim();
		this.description = description.trim();
		this.dateDebutEncheres = dateDebutEncheres;
		this.heureDebutEncheres = heureDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.heureFinEncheres = heureFinEncheres;
		this.prixInitial = prixInitial;
		this.adresseRetrait = adresseRetrait;
		this.vendeur = vendeur;
		this.categorie = categorie;
	}
	
	public Article(Integer noArticle, String nomArticle, String description, LocalDate dateDebutEncheres, LocalTime heureDebutEncheres,
			LocalDate dateFinEncheres, LocalTime heureFinEncheres, Integer prixInitial, Categorie categorie, Utilisateur vendeur, Adresse adresseRetrait) {
		super();
		this.noArticle = noArticle;
		this.nomArticle = nomArticle.trim();
		this.description = description.trim();
		this.dateDebutEncheres = dateDebutEncheres;
		this.heureDebutEncheres = heureDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.heureFinEncheres = heureFinEncheres;
		this.prixInitial = prixInitial;
		this.adresseRetrait = adresseRetrait;
		this.vendeur = vendeur;
		this.categorie = categorie;
	}

	/* Getters & Setters */
	
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
		this.nomArticle = nomArticle.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
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
	
	public LocalTime getHeureDebutEncheres() {
		return heureDebutEncheres;
	}

	public void setHeureDebutEncheres(LocalTime heureDebutEncheres) {
		this.heureDebutEncheres = heureDebutEncheres;
	}

	public LocalTime getHeureFinEncheres() {
		return heureFinEncheres;
	}

	public void setHeureFinEncheres(LocalTime heureFinEncheres) {
		this.heureFinEncheres = heureFinEncheres;
	}

	public Utilisateur getDernierEncherisseur() {
		return dernierEncherisseur;
	}
	
	public void setDernierEncherisseur(Utilisateur dernierEncherisseur) {
		this.dernierEncherisseur = dernierEncherisseur;
	}
	
	/* Méthode toString() */
	
	@Override
	public String toString() {
		return "Article [noArticle=" + noArticle + ", nomArticle=" + nomArticle + ", description=" + description + ", dateDebutEncheres="
				+ dateDebutEncheres + ", heureDebutEncheres=" + heureDebutEncheres + ", dateFinEncheres=" + dateFinEncheres
				+ ", heureFinEncheres=" + heureFinEncheres + ", prixInitial=" + prixInitial + ", prixVente=" + prixVente
				+ ", adresseRetrait=" + adresseRetrait + ", vendeur=" + vendeur + ", categorie=" + categorie + "]";
	}




}
