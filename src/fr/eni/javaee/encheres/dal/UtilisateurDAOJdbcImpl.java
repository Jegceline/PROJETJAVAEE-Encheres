package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

public class UtilisateurDAOJdbcImpl implements UtilisateurDAO {

	private DAO<Article> articleDAO = DAOFactory.getArticleDAO();
	private DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();

	/* Variables & Constantes */

	private ModelException modelDalException = new ModelException();
	private static final String INSERT_USER = "INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, "
		+ "code_postal, ville, mot_de_passe, credit, administrateur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_ALL_EMAILS = "SELECT email FROM Utilisateurs";
	private static final String SELECT_ALL_PSEUDO = "SELECT pseudo FROM Utilisateurs";
	private static final String SELECT_PASSWORD = "SELECT mot_de_passe FROM Utilisateurs WHERE email = ? OR pseudo = ?";
	private static final String FOUND_USER_ID_CONNECTION = "SELECT * FROM Utilisateurs WHERE email = ? OR pseudo = ?";
	private static final String SELECT_USER = "SELECT * FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String DELETE_USER = "DELETE FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String UPDATE_USER = "UPDATE Utilisateurs SET pseudo = ?, nom = ?, prenom = ?, email = ?, telephone = ?, rue = ?, "
		+ "code_postal = ?, ville = ?, mot_de_passe = ?, credit = ?, administrateur = ? WHERE no_utilisateur = ?";
	private static final String SELECT_CREDIT = "SELECT credit FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String UPDATE_CREDIT = "UPDATE Utilisateurs SET credit = ? WHERE no_utilisateur = ?";

	/* Constructeur */

	public UtilisateurDAOJdbcImpl() {
	}

	/* ----------- Méthodes C-U-D ------------ */

	/**
	 * ajoute un utilisateur en base de données
	 */
	@Override
	public void insert(Utilisateur utilisateur) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

				/* valorisation des paramètres */
				query.setString(1, utilisateur.getPseudo());
				query.setString(2, utilisateur.getNom());
				query.setString(3, utilisateur.getPrenom());
				query.setString(4, utilisateur.getEmail());
				query.setString(5, utilisateur.getTelephone());
				query.setString(6, utilisateur.getRue());
				query.setString(7, utilisateur.getCodePostal());
				query.setString(8, utilisateur.getVille());
				query.setString(9, utilisateur.getMotDePasse());
				query.setInt(10, utilisateur.getCredit());
				query.setBoolean(11, utilisateur.isAdministrateur());

				/* exécution de la requête */
				query.executeUpdate();

				/* récupération de la clé primaire/identity générée par la BDD */
				ResultSet rs = query.getGeneratedKeys();
				
				if (rs.next()) {
					/* valorisation de l'attribut Id de l'objet repas avec cette clé */
					utilisateur.setNoUtilisateur(rs.getInt(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête INSERT_USER a échoué.");
				// System.out.println("L'exécution de la requête INSERT_USER a échoué !");
				
				throw modelDalException;

			} finally {
				/* fermeture de la connexion */
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}

	/**
	 * supprime un utilisateur de la base de données
	 */
	@Override
	public void delete(Integer noUtilisateur) throws ModelException {
		
		// 1 - Supprimer toutes les enchères de l'utilisateur
		// 2 - Supprimer les ventes en attente et achevées de l'utilisateur
		// 3 - Supprimer l'utilisateur
		
		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			cnx.setAutoCommit(false);

			try {
				/* suppression de toutes les enchères émises par l'utilisateur */
				((EnchereDAO) enchereDAO).retrieveAndDeleteAllUserBids(noUtilisateur, cnx);
				
				/* suppression de tous les articles de l'utilisateur */
				((ArticleDAO) articleDAO).retrieveAndDeleteAllUserSells(noUtilisateur, cnx);
				
				/* préparation de la requête de suppression de l'utilisateur */
				PreparedStatement query = cnx.prepareStatement(DELETE_USER);

				/* valorisation du paramètre */
				query.setInt(1, noUtilisateur);

				/* exécution de la requête */
				query.executeUpdate();
				
				/* commit de toutes les requêtes effectuées sur la connexion */
				cnx.commit();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_DELETE_SQL, "L'exécution de la requête DELETE_USER a échoué.");
				// System.out.println("L'exécution de la requête DELETE_USER a échoué !");

				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}

	/**
	 * met à jour les données d'un utilisateur
	 */
	@Override
	public void update(Utilisateur utilisateur) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(UPDATE_USER);

				/* valorisation des paramètres */
				query.setString(1, utilisateur.getPseudo());
				query.setString(2, utilisateur.getNom());
				query.setString(3, utilisateur.getPrenom());
				query.setString(4, utilisateur.getEmail());
				query.setString(5, utilisateur.getTelephone());
				query.setString(6, utilisateur.getRue());
				query.setString(7, utilisateur.getCodePostal());
				query.setString(8, utilisateur.getVille());
				query.setString(9, utilisateur.getMotDePasse());
				query.setInt(10, utilisateur.getCredit());
				query.setBoolean(11, utilisateur.isAdministrateur());
				query.setInt(12, utilisateur.getNoUtilisateur());

				/* exécution de la requête */
				query.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_SQL, "L'exécution de la requête UPDATE_USER a échoué.");
				// System.out.println("L'exécution de la requête UPDATE_USER a échoué !");

				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}

	/**
	 * actualise le crédit du nouvel enchérisseur en lui retirant le montant de son enchère 
	 * récrédite le crédit du précédent enchérisseur du montant de l'enchère qu'il avait faite sur l'article
	 */
	@Override
	public void updateCredit(Enchere enchere, Enchere precedenteEnchere) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			cnx.setAutoCommit(false);

			try {

				/* crédit du nouvel enchérisseur */
				Integer creditActuel = retrieveUserCredit(enchere.getEncherisseur().getNoUtilisateur());
				Integer creditNouveau = creditActuel - enchere.getMontant();
				System.out.println("\nTEST DAO UTILISATEUR updateCredit() // Crédit de l'enchérisseur après enchere : " + creditNouveau);

				/* préparation de la requête pour diminuer le solde de l'enchérisseur */
				PreparedStatement query = cnx.prepareStatement(UPDATE_CREDIT);

				/* valorisation des paramètres */
				query.setInt(1, creditNouveau);
				query.setInt(2, enchere.getEncherisseur().getNoUtilisateur());

				/* exécution de la requête */
				query.executeUpdate();

				/* s'il y avait eu une précédente enchère sur l'article */
				if (precedenteEnchere != null) {
					Integer montantDerniereEnchere = precedenteEnchere.getMontant();
					// System.out.println("\nTEST DAO UTILISATEUR updateCredit() // Montant de l'enchère qu'avait fait le précédent enchérisseur : " + montantDerniereEnchere);

					Integer creditActuel2 = retrieveUserCredit(precedenteEnchere.getEncherisseur().getNoUtilisateur(), cnx);
					// System.out.println("\nTEST DAO UTILISATEUR updateCredit() // Crédit actuel du précédent enchérisseur : " + creditActuel2);

					Integer creditNouveau2 = creditActuel2 + montantDerniereEnchere;
					// System.out.println("\nTEST DAO UTILISATEUR updateCredit() // Crédit de cet enchérisseur après lui avoir récrédité ce montant : " + creditNouveau2);

					/* préparation de la seconde requête */
					PreparedStatement query2 = cnx.prepareStatement(UPDATE_CREDIT);

					/* valorisation des paramètres */
					query2.setInt(1, creditNouveau2);
					query2.setInt(2, precedenteEnchere.getEncherisseur().getNoUtilisateur());

					/* exécution de la seconde requête */
					query2.executeUpdate();
				}

				/* commit des deux requêtes */
				cnx.commit();

			} catch (Exception e) {
				e.printStackTrace();
				
				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_CREDIT, "L'exécution de la requête UPDATE_CREDIT a échoué.");
				// System.out.println("L'exécution de la requête UPDATE_CREDIT a échoué !");

				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}

	/* crédite le vendeur des points de ses ventes (un article n'est vendu que s'il a fait l'objet d'au moins une enchère) */
	@Override
	public void updateSellerCredit(Integer noVendeur) throws ModelException {

		List<Article> listeArticlesEncheresCloturees = new ArrayList<Article>();
		Enchere derniereEnchere = null;
		Integer totalACrediter = 0;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {

				/* on récupère le crédit actuel du vendeur */
				Integer creditActuel = retrieveUserCredit(noVendeur);
				// System.out.println("\nTEST DAO UTILISATEUR updateSellerCredit // Crédit actuel du vendeur : " + creditActuel);

				/* on récupère les articles du vendeur pour lesquels les enchères sont clôturées */
				listeArticlesEncheresCloturees = ((ArticleDAO) articleDAO).retrieveSellerYetNotPaidItems(noVendeur);

				/* pour chaque article, on récupère le montant de la dernière enchère émise et on l'ajoute au total à créditer */
				for (Article article : listeArticlesEncheresCloturees) {
					derniereEnchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(article.getNoArticle());

					if (derniereEnchere != null) {
						// System.out.println("\nTEST DAO UTILISATEUR updateSellerCredit // Montant de la dernière enchère : " + derniereEnchere.getMontant());
						totalACrediter += derniereEnchere.getMontant();
					}

					// System.out.println("\nTEST DAO UTILISATEUR updateSellerCredit // Total à créditer au vendeur : " + totalACrediter);

					/* on passe le statut du paiement de l'article à true */
					((ArticleDAO) articleDAO).updateItemPaymentStatus(article.getNoArticle());
				}

				/* préparation de la requête pour créditer le vendeur */
				PreparedStatement query = cnx.prepareStatement(UPDATE_CREDIT);

				/* valorisation des paramètres */
				query.setInt(1, creditActuel + totalACrediter);
				query.setInt(2, noVendeur);

				/* exécution de la requête */
				query.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_CREDIT, "L'exécution de la requête UPDATE_CREDIT a échoué.");
				// System.out.println("L'exécution de la requête UPDATE_CREDIT a échoué !");

				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}

	/* --------------- METHODES SELECT --------------- */

	/**
	 * récupère et retourne une liste des e-mails de tous les utilisateurs en BDD
	 */
	@Override
	public List<String> retrieveUsersEmails() throws ModelException {

		List<String> listeEmails = new ArrayList<>();

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ALL_EMAILS);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					listeEmails.add(rs.getString(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_MAILS_SQL, "L'exécution de la requête SELECT_ALL_EMAILS a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ALL_EMAILS a échoué !");
				
				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");

			}
			throw modelDalException;

		}

		return listeEmails;
	}

	/**
	 * récupère et retourne une liste de tous les pseudo des utilisateurs en BDD
	 */
	public List<String> retrieveUsersAlias() throws ModelException {

		List<String> listePseudo = new ArrayList<>();

		try {

			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ALL_PSEUDO);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					listePseudo.add(rs.getString(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL, "L'exécution de la requête SELECT_ALL_PSEUDO a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ALL_PSEUDO a échoué !");
				
				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}
			
			throw modelDalException;
		}

		return listePseudo;
	}

	/**
	 * retourne le mot de passe d'un utilisateur identifié par sa primary key
	 */
	@Override
	public String retrieveUserPassword(String identifiant) throws ModelException {

		String password = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_PASSWORD);

				/* valorisation des paramètres */
				query.setString(1, identifiant);
				query.setString(2, identifiant);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					password = rs.getString(1);
				}

				// System.out.println("\nTEST DAO : Le mot de passe de cet utilisateur est : " + password);

			} catch (SQLException e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PASSWORD, "L'exécution de la requête SELECT_PASSWORD a échoué.");
				// System.out.println("\n TEST DAO : L'exécution de la requête SELECT_PASSWORD a échoué !");
				
				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("\n DEBUG DAO : Impossible de se connecter à la base de données !");
			}
			
			throw modelDalException;
		}

		return password;
	}

	/**
	 * va chercher en base s'il existe un utilisateur dont l'identifiant est identique à celui passé en paramètre
	 */
	@Override
	public Utilisateur retrieveUserInfo(String identifiantConnexion) throws ModelException {

		Utilisateur utilisateur = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			// System.out.println("\nTEST DAO UTILISATEUR retrieveUsersAlias // cnx  = " + cnx);

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(FOUND_USER_ID_CONNECTION);

				/* valorisation du paramètre */
				query.setString(1, identifiantConnexion);
				query.setString(2, identifiantConnexion);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					utilisateur = utilisateurBuilder(rs);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL, "L'exécution de la requête SELECT_USER a échoué.");
				System.out.println("L'exécution de la requête SELECT_USER a échoué !");
				
				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
				// System.out.println("\nTEST DAO UTILISATEUR retrieveUsersAlias après fermeture // cnx  = " + cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}
			
			throw modelDalException;
		}
		
		return utilisateur;
	}

	/*
	 * récupère et retourne un utilisateur identifié par sa primary key
	 */
	@Override
	public Utilisateur selectById(Integer noUtilisateur) throws ModelException {
		Utilisateur utilisateur = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_USER);

				/* valorisation du paramètre */
				query.setInt(1, noUtilisateur);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					utilisateur = utilisateurBuilder(rs);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_USER, "L'exécution de la requête SELECT_USER a échoué.");
				// System.out.println("L'exécution de la requête SELECT_USER a échoué !");
				
				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				// System.out.println("Impossible de se connecter à la base de données !");
			}
			
			throw modelDalException;
		}
		
		return utilisateur;
	}

	/**
	 * récupère et retourne le crédit d'un utilisateur identifié par sa primary key
	 */
	@Override
	public Integer retrieveUserCredit(Integer noUtilisateur) throws ModelException {

		Integer credit = null;
		Connection cnx = null;

		try {

			try {

				/* ouverture d'une connexion */
				cnx = ConnectionProvider.getConnection();

				/* on récupère le crédit de l'utilisateur */
				credit = retrieveUserCredit(noUtilisateur, cnx); 

			} catch (Exception e) {
				e.printStackTrace();

				if (!modelDalException.contientErreurs()) {
					modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
					System.out.println("Impossible de se connecter à la base de données !");
				}

				throw modelDalException;

			} finally {
				ConnectionProvider.closeConnection(cnx);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return credit;
	}

	/**
	 * récupère le crédit d'un utilisateur
	 * @param noUtilisateur
	 * @param cnx
	 * @return credit
	 * @throws ModelException
	 * @throws SQLException
	 */
	public Integer retrieveUserCredit(Integer noUtilisateur, Connection cnx) throws ModelException, SQLException {

		Integer credit = null;

		try {
			/* préparation de la requête */
			PreparedStatement query = cnx.prepareStatement(SELECT_CREDIT);

			/* valorisation des paramètres */
			query.setInt(1, noUtilisateur);

			/* exécution de la requête */
			ResultSet rs = query.executeQuery();

			if (rs.next()) {
				credit = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			
			modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_CREDIT, "L'exécution de la requête SELECT_CREDIT a échoué.");
			// System.out.println("L'exécution de la requête SELECT_CREDIT a échoué !");
			
			throw modelDalException;
		}

		return credit;
	}
	
	
	/* -------------- BUILDERS -------------- */

	/**
	 * construit un objet de type Utilisateur à partir du jeu d'enregistrements
	 * remonté par la requête SQL pour utiliser cette méthode, il faut
	 * impérativement que la requête SQL projette les noms de colonnes suivants dans
	 * cet ordre précis : no_utilisateur, pseudo, nom, prenom, email, telephone,
	 * rue, code_postal, ville, mot_de_passe, credit et administrateur
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Utilisateur utilisateurBuilder(ResultSet rs) throws SQLException {

		Utilisateur utilisateur = new Utilisateur();

		utilisateur.setNoUtilisateur(rs.getInt(1));
		utilisateur.setPseudo(rs.getString(2));
		utilisateur.setNom(rs.getString(3));
		utilisateur.setPrenom(rs.getString(4));
		utilisateur.setEmail(rs.getString(5));
		utilisateur.setTelephone(rs.getString(6));
		utilisateur.setRue(rs.getString(7));
		utilisateur.setCodePostal(rs.getString(8));
		utilisateur.setVille(rs.getString(9));
		utilisateur.setMotDePasse(rs.getString(10));
		utilisateur.setCredit(rs.getInt(11));
		utilisateur.setAdministrateur(rs.getBoolean(12));

		// System.out.println("\nTEST DAO UTILISATEUR // Utilisateur créé par utilisateurBuilder : " + utilisateur);

		return utilisateur;
	}

}
