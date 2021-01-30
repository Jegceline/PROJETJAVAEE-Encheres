package fr.eni.javaee.encheres;

public abstract class CodesErreurs {
	
	// Erreurs de la BLL (entre 100 et 199)
	
	public static int ERREUR_PRENOM_INVALIDE = 100;
	public static int ERREUR_NOM_INVALIDE = 101;
	public static int ERREUR_MOTDEPASSE_LONGUEUR = 102;
	public static int ERREUR_MOTDEPASSE_CONFIRMATION = 103;
	public static int ERREUR_TELEPHONE_INVALIDE = 104;
	public static int ERREUR_EMAIL_INVALIDE = 105;
	public static int ERREUR_PSEUDO_INVALIDE = 106;
	public static int ERREUR_ADRESSE_LONGUEUR = 107;
	public static int ERREUR_ADRESSE_CPO = 108;
	public static int ERREUR_EMAIL_EXISTANT = 109;
	public static int ERREUR_MOTDEPASSE_INCORRECT = 110;
	public static int ARTICLE_DATE_DEBUT_ENCHERES_INVALIDE = 113;
	public static int ARTICLE_DATE_FIN_ENCHERES_INVALIDE = 114;
	public static int ARTICLE_NOM_LONGUEUR = 115;
	public static int ARTICLE_DESCRIPTION_LONGUEUR = 116;
	public static int ARTICLE_CATEGORIE_INEXISTANTE = 117;
	public static int ARTICLE_PRIX_INVALIDE = 118;
	public static int ERREUR_CHARGEMENT_ENCHERES_EC = 119;
	public static int ERREUR_PSEUDO_FORMAT = 120;
	public static int ERREUR_EMAIL_FORMAT = 121;
	public static int ERREUR_CHARGEMENT_TRI_ARTICLES = 122;
	public static int ERREUR_CREDIT_INSUFFISANT = 123;
	public static int ERREUR_MONTANT_ENCHERE = 124;



	// Erreurs de la DAL (entre 0 et 99)
	
	public static int ERREUR_INSERTION_SQL = 0;
	public static int ERREUR_CONNEXION_BASE = 1;
	public static int ERREUR_SELECT_MAILS_SQL = 2;
	public static int ERREUR_SELECT_PSEUDO_SQL = 3;
	public static int ERREUR_PSEUDO_EXISTANT = 4;
	public static int ERREUR_UTILISATEUR_INEXISTANT = 5;
	public static int ERREUR_UPDATE_SQL = 6;
	public static int ERREUR_DELETE_SQL = 7;
	public static int ERREUR_SELECT_PASSWORD = 8;
	public static int ERREUR_SELECTION_SQL = 9;
	public static int ERREUR_SELECT_CREDIT = 10;
	public static int ERREUR_SELECT_INITIAL_PRICE = 11;
	public static int ERREUR_SELECT_CURRENT_PRICE = 12;
	public static int ERREUR_UPDATE_BID = 13;
	public static int ERREUR_INSERT_BID = 14;
	public static int ERREUR_UPDATE_CREDIT = 15;
	public static int ERREUR_SELECT_USER = 16;

		
}
