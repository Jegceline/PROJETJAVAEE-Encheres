package fr.eni.javaee.encheres.bo;

public class Trieur {

	private Utilisateur utilisateur;
	private String keyword;
	private Integer categorie;

	private String encheresEC;
	
	private String encheresUtilisateurEC;
	private String encheresUtilisateurG;
	
	private String ventesUtilisateurAttente;
	private String ventesUtilisateurEC;
	private String ventesUtilisateurT;

	// private boolean currentBids;
	// private boolean currentUserBids;
	// private boolean endedUserBids;

	// private boolean ventesUtilisateurAV;
	// private boolean ventesUtilisateurEC;
	// private boolean ventesUtilisateurT;

	/* Constructeur */

	public Trieur() {
	}

	public Trieur(Utilisateur utilisateur, String keyword, Integer categorie, String encheresEC, String encheresUtilisateurEC,
			String encheresUtilisateurT) {
		super();
		this.utilisateur = utilisateur;
		this.keyword = keyword;
		this.categorie = categorie;
		this.encheresEC = encheresEC;
		this.encheresUtilisateurEC = encheresUtilisateurEC;
		this.encheresUtilisateurG = encheresUtilisateurT;
	}
	
	
	public Trieur(Utilisateur utilisateur, String keyword, Integer categorie, String encheresEC, String encheresUtilisateurEC,
			String encheresUtilisateurT, String ventesUtilisateurAttente, String ventesUtilisateurEC, String ventesUtilisateurT) {
		super();
		this.utilisateur = utilisateur;
		this.keyword = keyword;
		this.categorie = categorie;
		this.encheresEC = encheresEC;
		this.encheresUtilisateurEC = encheresUtilisateurEC;
		this.encheresUtilisateurG = encheresUtilisateurT;
		this.setVentesUtilisateurAttente(ventesUtilisateurAttente);
		this.setVentesUtilisateurEC(ventesUtilisateurEC);
		this.setVentesUtilisateurT(ventesUtilisateurT);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getCategorie() {
		return categorie;
	}

	public void setCategorie(Integer categorie) {
		this.categorie = categorie;
	}

	public String getEncheresEC() {
		return encheresEC;
	}

	public void setEncheresEC(String encheresEC) {
		this.encheresEC = encheresEC;
	}

	public String getEncheresUtilisateurEC() {
		return encheresUtilisateurEC;
	}

	public void setEncheresUtilisateurEC(String encheresUtilisateurEC) {
		this.encheresUtilisateurEC = encheresUtilisateurEC;
	}

	public String getEncheresUtilisateurG() {
		return encheresUtilisateurG;
	}

	public void setEncheresUtilisateurG(String encheresUtilisateurG) {
		this.encheresUtilisateurG = encheresUtilisateurG;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getVentesUtilisateurT() {
		return ventesUtilisateurT;
	}

	public void setVentesUtilisateurT(String ventesUtilisateurT) {
		this.ventesUtilisateurT = ventesUtilisateurT;
	}

	public String getVentesUtilisateurEC() {
		return ventesUtilisateurEC;
	}

	public void setVentesUtilisateurEC(String ventesUtilisateurEC) {
		this.ventesUtilisateurEC = ventesUtilisateurEC;
	}

	public String getVentesUtilisateurAttente() {
		return ventesUtilisateurAttente;
	}

	public void setVentesUtilisateurAttente(String ventesUtilisateurAttente) {
		this.ventesUtilisateurAttente = ventesUtilisateurAttente;
	}

}
