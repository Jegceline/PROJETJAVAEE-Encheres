package fr.eni.javaee.encheres.bo;

public class Categorie {
	
	/* Attributs */
	private Integer noCategorie;
	private String libelle;
	
	
	/* Constructeurs */
	public Categorie() {
	}

	public Categorie(String libelle) {
		this.libelle = libelle;
	}


	/* Getters & Setters */
	
	public Integer getNoCategorie() {
		return noCategorie;
	}

	public void setNoCategorie(Integer noCategorie) {
		this.noCategorie = noCategorie;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Override
	public String toString() {
		return "Categorie [noCategorie=" + noCategorie + ", libelle=" + libelle + "]";
	}

}
