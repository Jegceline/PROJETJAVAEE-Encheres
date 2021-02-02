package fr.eni.javaee.encheres.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.UtilisateurDAO;

public class UtilisateurManager {

	private ModelException modelBllException = new ModelException();
	private DAO<Utilisateur> utilisateurDAO = DAOFactory.getUtilisateurDAO();

	public UtilisateurManager() {
	}

	/* ----------------------------------------- */
	/* ------------- Méthodes CRUD ------------- */
	/* ----------------------------------------- */

	public Utilisateur ajouteUtilisateur(Utilisateur utilisateur, String motDePasseBis) throws ModelException {

		/* vérifier que les contraintes imposées par la database sont respectées */

		valideNomPrenom(utilisateur.getNom(), utilisateur.getPrenom());
		validePseudo(utilisateur.getPseudo());
		valideEmail(utilisateur.getEmail());
		valideTelephone(utilisateur.getTelephone());
		valideAdresse(utilisateur.getRue(), utilisateur.getCodePostal());
		valideMdp(utilisateur.getMotDePasse(), motDePasseBis);

		/* si les étapes de validation ont été passées avec succès, aappeler le DAO */

		if (!modelBllException.contientErreurs()) {

			System.out.println("\nMANAGER // Les paramètres sont ok, le DAO va être appelé.");
			// on attribue un crédit de 100 à l'utilisateur
			utilisateur.setCredit(100);

			try {
				utilisateurDAO.insert(utilisateur);

			} catch (ModelException e) {
				e.printStackTrace();
			}

		} else { // si une des étapes de validation a échoué

			utilisateur = null;
			throw modelBllException;
		}

		// System.out.println("\nTEST // Utilisateur après insertion dans la BDD : " +
		// utilisateur);

		return utilisateur;
	}

	/**
	 * recherche un hypothétique utilisateur dans la base de donnée méthode appelée
	 * lors de la connexion
	 * 
	 * @param identifiant
	 *            (pseudo ou email)
	 * @return null si l'utilisateur n'existe pas, un objet Utilisateur si l'utilisateur existe
	 * @throws ModelException
	 */
	public Utilisateur rechercheUtilisateur(String identifiant) throws ModelException {
		Utilisateur utilisateur = null;

		try {
			utilisateur = ((UtilisateurDAO) utilisateurDAO).retrieveUserInfo(identifiant);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		return utilisateur;
	}

	/**
	 * recherche et récupère un utilisateur grâce à sa primary key
	 * 
	 * @param noUtilisateur
	 *            (pk)
	 * @return un objet Utilisateur
	 * @throws ModelException
	 */
	public Utilisateur recupereUtilisateur(Integer noUtilisateur) throws ModelException {
		Utilisateur utilisateur = null;

		try {
			utilisateur = utilisateurDAO.selectById(noUtilisateur);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		return utilisateur;
	}

	public void supprimeUtilisateur(Integer noUtilisateur) throws ModelException {
		try {
			utilisateurDAO.delete(noUtilisateur);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public Utilisateur metAJourUtilisateur(Utilisateur utilisateurSession, Utilisateur utilisateurAvecModif, String confirmationMdp)
			throws ModelException {

		/* Vérification des nouveaux paramètres */

		if (!utilisateurSession.getEmail().equals(utilisateurAvecModif.getEmail())) {
			valideEmail(utilisateurAvecModif.getEmail());
		}

		if (!utilisateurSession.getPseudo().equals(utilisateurAvecModif.getPseudo())) {
			validePseudo(utilisateurAvecModif.getPseudo());
		}

		valideNomPrenom(utilisateurAvecModif.getNom(), utilisateurAvecModif.getPrenom());
		valideTelephone(utilisateurAvecModif.getTelephone());
		valideAdresse(utilisateurAvecModif.getRue(), utilisateurAvecModif.getCodePostal());
		valideMdp(utilisateurAvecModif.getMotDePasse(), confirmationMdp);

		// si les paramères sont intègres, appeler le DAO
		if (!modelBllException.contientErreurs()) {

			System.out.println("\nMANAGER // Les paramètres sont ok, le DAO va être appelé.");

			try {
				utilisateurAvecModif.setCredit(utilisateurSession.getCredit());
				utilisateurAvecModif.setAdministrateur(utilisateurSession.isAdministrateur());

				System.out.println("\nTEST MANAGER // Utilisateur modifié qui va être envoyé au DAO : + " + utilisateurAvecModif);
				utilisateurDAO.update(utilisateurAvecModif);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}

		} else {
			// si un des paramètres n'est pas valide
			utilisateurAvecModif = null;
			throw modelBllException;
		}

		System.out.println("\nTEST // Utilisateur après insertion dans la BDD :  " + utilisateurAvecModif);

		return utilisateurAvecModif;
	}

	/*
	 * récupère le crédit d'un utilisateur
	 */
	public Integer recupereCredit(Integer noUtilisateur) throws ModelException {
		Integer credit;
		try {
			credit = ((UtilisateurDAO) utilisateurDAO).selectCredit(noUtilisateur);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		return credit;

	}
	
	/**
	 * Vérifie que le crédit de l'utilisateur est suffisant pour effectuer l'enchère
	 * qu'il souhaite
	 * 
	 * @param noUtilisateur
	 * @param montantEnchere
	 * @return
	 * @throws ModelException
	 */
	public Integer verifieSoldeCredits(Integer noUtilisateur, Integer montantEnchere) throws ModelException {

		Integer credit;
		UtilisateurManager utilisateurManager = new UtilisateurManager();

		try {
			credit = utilisateurManager.recupereCredit(noUtilisateur);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

		if (credit < montantEnchere) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_CREDIT_INSUFFISANT, "Votre crédit est insuffisant !");
			throw modelBllException;
		}

		return credit;
	}

	/**
	 * Déduit le montant de l'enchère de l'enchérisseur
	 * 
	 * @param enchere
	 * @param derniereEnchere
	 * @throws ModelException
	 */
	public void actualiseCredit(Enchere enchere, Enchere derniereEnchere) throws ModelException {

		try {
			((UtilisateurDAO) utilisateurDAO).updateCredit(enchere, derniereEnchere);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/* ----------------------------------------- */
	/* ------- Méthodes de vérification -------- */
	/* ----------------------------------------- */

	public String recupereEtVerifieMdp(String identifiant, String motDePasse) throws ModelException {
		String motDePasseBdd = null;

		try {
			motDePasseBdd = ((UtilisateurDAO) utilisateurDAO).selectPassword(identifiant);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

		if (motDePasseBdd == null) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_UTILISATEUR_INEXISTANT, "Cet utilisateur n'existe pas.");
			throw modelBllException;

		} else if (!motDePasseBdd.equals(motDePasse)) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_MOTDEPASSE_INCORRECT, "Le mot de passe est incorrect.");
			System.out.println(modelBllException);
			throw modelBllException;
		}
		
		return motDePasseBdd;

	}

	/**
	 * Vérifie que le mot de passe ne fait pas plus de 30 caractères Vérifie que les
	 * deux mots de passe sont identiques
	 * 
	 * @param mdp
	 * @param mdp2
	 */
	private void valideMdp(String mdp, String mdp2) {

		if (!mdp.trim().equals(mdp2.trim())) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_MOTDEPASSE_CONFIRMATION, "Les deux mots de passe ne sont pas identiques.");
			// System.out.println("\nTEST // Les deux mots de passe ne sont pas
			// identiques.");
		} else {
			if (mdp.trim().length() > 30) {
				modelBllException.ajouterErreur(CodesErreurs.ERREUR_MOTDEPASSE_LONGUEUR, "Le mot de passe est trop long.");
				// System.out.println("\nTEST // Le mot de passe est trop long.");
			}
		}

	}

	/**
	 * Vérifie que l'adresse ne fait pas plus de 50 caractères Vérifie que le code
	 * postale ne fait pas plus de 5 caractères
	 * 
	 * @param rue
	 * @param cpo
	 */
	private void valideAdresse(String rue, String cpo) {
		if (rue.length() > 50) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ADRESSE_LONGUEUR, "L'intitulé de la rue est trop long.");
			System.out.println("\nTEST // L'intitulé de la rue est trop long.");
		}
		if (cpo.trim().length() > 5) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ADRESSE_CPO, "Le code postal n'est pas valide.");
			System.out.println("\nTEST // Le code postal n'est pas valide.");
		}
	}

	/**
	 * Vérifie que le numéro de téléphone ne fait pas plus de 15 caractères
	 * 
	 * @param telephone
	 */
	private void valideTelephone(String telephone) {
		if (telephone.trim().length() > 15) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_TELEPHONE_INVALIDE, "Le numéro de téléphone n'est pas valide.");
			System.out.println("\nTEST // Le numéro de téléphone n'est pas valide.");
		}

	}

	/**
	 * Vérifie que l'adresse mail ne fait pas plus de 50 caractères Vérifie que
	 * l'adresse mail n'est pas déjà utilisée vérifie que l'adresse mail comporte
	 * un @
	 * 
	 * @param email
	 * @throws ModelException
	 */
	private void valideEmail(String email) throws ModelException {

		List<String> listeEmails = new ArrayList<String>();

		if (email.trim().length() > 50) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_EMAIL_INVALIDE, "L'adresse e-mail n'est pas valide.");
			System.out.println("\nTEST // L'adresse email n'est pas valide.");
		}

		// récupérer une liste de tous les emails de la base de données
		try {
			listeEmails = ((UtilisateurDAO) utilisateurDAO).selectAllEmails();
			// System.out.println("\nTEST // Liste des emails : " + listeEmails);
		} catch (ModelException e) {
			throw e;
		}

		// vérifier que l'adresse email n'est pas déjà utilisée
		for (String emailCourant : listeEmails) {
			if (emailCourant.equals(email)) {
				modelBllException.ajouterErreur(CodesErreurs.ERREUR_EMAIL_EXISTANT, "Cette adresse mail est déjà utilisée.");
				System.out.println("\nMANAGER : Cette adresse e-mail est déjà utilisée !");
			}

		}
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
		Matcher emailMatcher = pattern.matcher(email);

		if (!emailMatcher.matches()) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_EMAIL_FORMAT, "Veuillez renseigner une adresse e-mail valide.");
			// System.out.println("MANAGER : L'adresse e-mail ne respecte pas format d'une
			// adresse e-mail.");
		}
	}

	/**
	 * Vérifie que le pseudo ne fait pas plus de 30 caractères Vérifie que le pseudo
	 * n'est pas déjà utilisé vérifie que le pseudo ne contient que des caractères
	 * alphanumériques
	 * 
	 * @param pseudo
	 * @throws ModelException
	 */
	private void validePseudo(String pseudo) throws ModelException {

		List<String> listePseudo = new ArrayList<String>();

		if (pseudo.trim().length() > 30) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_PSEUDO_INVALIDE, "Le pseudonyme est trop long.");
			System.out.println("\nTEST // Le pseudo n'est pas valide.");
		}

		try {
			listePseudo = ((UtilisateurDAO) utilisateurDAO).selectAllPseudo();

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

		for (String pseudoCourant : listePseudo) {
			if (pseudo.equals(pseudoCourant)) {
				modelBllException.ajouterErreur(CodesErreurs.ERREUR_PSEUDO_EXISTANT, "Ce pseudo est déjà utilisé.");
				// System.out.println("MANAGER : Ce pseudo est déjà utilisé");
			}

		}

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
		Matcher pseudoMatcher = pattern.matcher(pseudo);

		if (!pseudoMatcher.matches()) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_PSEUDO_FORMAT,
					"Votre pseudo doit uniquement contenir des caractères alphanumériques.");
			// System.out.println("MANAGER : Le pseudo de l'uilisateur ne respecte pas le
			// format autorisé.");
		}
	}

	/**
	 * Vérifie que le nom et le prénom ne font pas plus de 30 caractères
	 * 
	 * @param nom
	 * @param prenom
	 */
	private void valideNomPrenom(String nom, String prenom) {
		if (nom.trim().length() > 30) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_NOM_INVALIDE, "Le nom de famille est trop long.");
			System.out.println("\nTEST // Le nom est trop long.");
		}
		if (prenom.trim().length() > 30) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_PRENOM_INVALIDE, "Le prénom est trop long.");
			System.out.println("\nTEST // Le prénom est trop long.");
		}

	}

}
