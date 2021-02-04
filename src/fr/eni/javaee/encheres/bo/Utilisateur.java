package fr.eni.javaee.encheres.bo;

public class Utilisateur {
	
	/* Attributs */
	
	private Integer noUtilisateur;
	private String pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse;
	private Integer credit;
	private boolean administrateur;
	
	
	/* Constructeur */

	public Utilisateur() {
		this.administrateur = false; // par défaut, tout nouvel utilisateur ne sera PAS administrateur
	}

	// constructeur utilisé pour ajouter un utilisateur
	
	public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal,
			String ville, String motDePasse) {
		this();
		this.pseudo = pseudo.trim();
		this.nom = nom.toUpperCase();
		this.prenom = prenom;
		this.email = email.trim();
		this.telephone = telephone;
		this.rue = rue.trim();
		this.codePostal = codePostal.trim();
		this.ville = ville.trim();
		this.motDePasse = motDePasse.trim();
	}

	// constructeur avec id
	
	public Utilisateur(Integer noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone, String rue,
			String codePostal, String ville, String motDePasse) {
		super();
		this.noUtilisateur = noUtilisateur;
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
	}
	
	
	public Utilisateur(Integer noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone, String rue,
			String codePostal, String ville, String motDePasse, Integer credit, boolean administrateur) {
		super();
		this.noUtilisateur = noUtilisateur;
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
		this.credit = credit;
		this.administrateur = administrateur;
	}

	/* Getters & Setters */

	public Integer getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(Integer noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo.trim();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom.trim().toUpperCase();
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		String firstLetter = prenom.substring(0, 1);
		String othersLetters = prenom.substring(1, prenom.length());
		firstLetter.toUpperCase();
		this.prenom = (firstLetter+othersLetters).trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone.trim();
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue.trim();
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal.trim();
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville.trim();
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse.trim();
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public boolean isAdministrateur() {
		return administrateur;
	}

	public void setAdministrateur(boolean administrateur) {
		this.administrateur = administrateur;
	}
	
	/* ToString */

	@Override
	public String toString() {
		return "Utilisateur [no_utilisateur=" + noUtilisateur + ", pseudo=" + pseudo + ", nom=" + nom + ", prenom=" + prenom + ", email="
				+ email + ", telephone=" + telephone + ", rue=" + rue + ", code_postal=" + codePostal + ", ville=" + ville
				+ ", mot_de_passe=" + motDePasse + ", credit=" + credit + ", administrateur=" + administrateur + "]";
	}

}
