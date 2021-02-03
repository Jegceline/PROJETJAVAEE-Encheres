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
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

public class UtilisateurDAOJdbcImpl implements UtilisateurDAO {

	/* Variables & Constantes */

	private ModelException modelDalException = new ModelException();
	private static final String INSERT_USER = "INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, "
			+ "code_postal, ville, mot_de_passe, credit, administrateur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_ALL_EMAILS = "SELECT email FROM Utilisateurs";
	private static final String SELECT_ALL_PSEUDO = "SELECT pseudo FROM Utilisateurs";
	private static final String SELECT_PASSWORD = "SELECT mot_de_passe FROM Utilisateurs WHERE email = ? OR pseudo = ?";
	private static final String FOUND_USER_CONNECTION = "SELECT * FROM Utilisateurs WHERE email = ? OR pseudo = ?";
	private static final String SELECT_USER = "SELECT * FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String DELETE_USER = "DELETE FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String UPDATE_USER = "UPDATE Utilisateurs SET pseudo = ?, nom = ?, prenom = ?, email = ?, telephone = ?, rue = ?, "
			+ "code_postal = ?, ville = ?, mot_de_passe = ?, credit = ?, administrateur = ? WHERE no_utilisateur = ?";
	private static final String SELECT_CREDIT = "SELECT credit FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String UPDATE_CREDIT = "UPDATE Utilisateurs SET credit = ? WHERE no_utilisateur = ?";

	/* Constructeur */

	public UtilisateurDAOJdbcImpl() {
	}

	
	/* ----------- Méthodes CRUD ------------ */

	@Override
	public void insert(Utilisateur utilisateur) throws ModelException {
		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);

				// valorisation des paramètres
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

				// exécution de la requête
				query.executeUpdate();
				
				cnx.commit();

				// récupération de la clé primaire/identity générée par la BDD
				ResultSet rs = query.getGeneratedKeys();
				if (rs.next()) {
					// valorisation de l'attribut Id de l'objet repas avec cette clé
					utilisateur.setNoUtilisateur(rs.getInt(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				
				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête INSERT_USER a échoué.");
				System.out.println("L'exécution de la requête INSERT_USER a échoué !");
				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}

	/**
	 * supprime un utilisateur
	 */
	@Override
	public void delete(Integer noUtilisateur) throws ModelException {

		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(DELETE_USER);

				// valorisation du paramètre
				query.setInt(1, noUtilisateur);

				// exécution de la requête
				query.executeUpdate();
				
				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();
				
				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_DELETE_SQL, "L'exécution de la requête DELETE_USER a échoué.");
				System.out.println("L'exécution de la requête DELETE_USER a échoué !");

				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");
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
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(UPDATE_USER);

				// valorisation des paramètres
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

				// exécution de la requête
				query.executeUpdate();
				
				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();
				
				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_SQL, "L'exécution de la requête UPDATE_USER a échoué.");
				System.out.println("L'exécution de la requête UPDATE_USER a échoué !");

				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}
	
	
	/* --------------- UPDATE --------------- */
	
	/**
	 * actualise le crédit du nouvel enchérisseur en lui soustrayant le montant de son enchère
	 * récrédite le crédit du précédent enchérisseur du montant de l'enchère qu'il avait faite 
	 */
	@Override
	public void updateCredit(Enchere enchere, Enchere precedenteEnchere) throws ModelException {

		/* Crédit du nouvel enchérisseur */
		Integer creditActuel = selectCredit(enchere.getEncherisseur().getNoUtilisateur());
		Integer creditNouveau = creditActuel - enchere.getMontant();
		System.out.println("\nTEST DAO UTILISATEUR // Crédit de l'enchérisseur après enchere : " + creditNouveau);

		try {
			/* obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la requête pour diminuer le solde de l'enchérisseur */
				PreparedStatement query = cnx.prepareStatement(UPDATE_CREDIT);

				/* Valorisation des paramètres */
				query.setInt(1, creditNouveau);
				query.setInt(2, enchere.getEncherisseur().getNoUtilisateur());

				/* Exécution de la requête */
				query.executeUpdate();
				
				/* S'il y avait eu une précédente enchère sur l'article */
				if (precedenteEnchere != null) {
					Integer montantDerniereEnchere = precedenteEnchere.getMontant();
					System.out.println("\nTEST DAO UTILISATEUR // Montant de l'enchère qu'avait fait le précédent enchérisseur : " + montantDerniereEnchere);
					
					Integer creditActuel2 = selectCredit(precedenteEnchere.getEncherisseur().getNoUtilisateur());
					System.out.println("\nTEST DAO UTILISATEUR // Crédit actuel du précédent enchérisseur : " + creditActuel2);
					
					Integer creditNouveau2 = creditActuel2 + montantDerniereEnchere;
					System.out.println("\nTEST DAO UTILISATEUR // Crédit de cet enchérisseur après lui avoir récrédité ce montant : " + creditNouveau2);
					
					/* Préparation de la requête */ 
					PreparedStatement query2 = cnx.prepareStatement(UPDATE_CREDIT);
					
					/* Valorisation des paramètres */
					query2.setInt(1, creditNouveau2);
					query2.setInt(2, precedenteEnchere.getEncherisseur().getNoUtilisateur());
					
					/* exécution de la requête */
					query2.executeUpdate();
				}
				
				cnx.commit();

			} catch (Exception e) {
				e.printStackTrace();
				
				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_CREDIT, "L'exécution de la requête UPDATE_CREDIT a échoué.");
				System.out.println("L'exécution de la requête UPDATE_CREDIT a échoué !");

				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");
			}

			throw modelDalException;
		}

	}
	
	
	/* --------------- SELECT --------------- */
	
	/**
	 * récupère et retourne une liste de tous les e-mails
	 */
	@Override
	public List<String> selectAllEmails() throws ModelException {

		List<String> listeEmails = new ArrayList<>();

		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(SELECT_ALL_EMAILS);

				// exécution de la requête
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					listeEmails.add(rs.getString(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_MAILS_SQL,
						"L'exécution de la requête SELECT_ALL_EMAILS a échoué.");
				System.out.println("L'exécution de la requête SELECT_ALL_EMAILS a échoué !");
				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");

			}
			throw modelDalException;

		}

		return listeEmails;
	}

	/**
	 * récupère et retourne une liste de tous les pseudo des personnes inscrites
	 */
	public List<String> selectAllPseudo() throws ModelException {

		List<String> listePseudo = new ArrayList<>();

		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(SELECT_ALL_PSEUDO);

				// exécution de la requête
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					listePseudo.add(rs.getString(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL,
						"L'exécution de la requête SELECT_ALL_PSEUDO a échoué.");
				System.out.println("L'exécution de la requête SELECT_ALL_PSEUDO a échoué !");
				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");

			}
			throw modelDalException;

		}

		return listePseudo;
	}

	/**
	 * retourne le mot de passe d'un utilisateur
	 */
	@Override
	public String selectPassword(String identifiant) throws ModelException {

		String password = null;

		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(SELECT_PASSWORD);

				// Valorisation des paramètres
				query.setString(1, identifiant);
				query.setString(2, identifiant);

				// exécution de la requête
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					password = rs.getString(1);
				}

				// System.out.println("\nTEST DAO : Le mot de passe de cet utilisateur est : " +
				// password);

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PASSWORD, "L'exécution de la requête SELECT_PASSWORD a échoué.");
				System.out.println("\n TEST DAO : L'exécution de la requête SELECT_PASSWORD a échoué !");
				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (

		Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("\n DEBUG DAO : Impossible de se connecter à la base de données !");

			}
			throw modelDalException;
		}

		return password;

	}

	/**
	 * récupère un utilisateur, construit un objet Utilisateur et le retourne au
	 * manager
	 */
	@Override
	public Utilisateur retrieveUserInfo(String identifiant) throws ModelException {

		Utilisateur utilisateur = null;

		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(FOUND_USER_CONNECTION);

				// valorisation du paramètre
				query.setString(1, identifiant);
				query.setString(2, identifiant);

				// exécution de la requête
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
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");

			}
			throw modelDalException;

		}
		return utilisateur;
	}

	/*
	 * récupère et retourne un utilisateur
	 */
	@Override
	public Utilisateur selectById(Integer noUtilisateur) throws ModelException {
		Utilisateur utilisateur = null;

		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(SELECT_USER);

				// valorisation du paramètre
				query.setInt(1, noUtilisateur);

				// exécution de la requête
				ResultSet rs = query.executeQuery();

				if(rs.next()) {
					utilisateur = utilisateurBuilder(rs);					
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_USER, "L'exécution de la requête SELECT_USER a échoué.");
				System.out.println("L'exécution de la requête SELECT_USER a échoué !");
				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");

			}
			throw modelDalException;

		}
		return utilisateur;

	}

	@Override
	public Integer selectCredit(Integer noUtilisateur) throws ModelException {
		Integer credit = null;

		try {
			/* obtention d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_CREDIT);

				/* valorisation des paramètres */
				query.setInt(1, noUtilisateur);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					credit = rs.getInt(1);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_CREDIT, "L'exécution de la requête SELECT_CREDIT a échoué.");
				System.out.println("L'exécution de la requête SELECT_CREDIT a échoué !");
				throw modelDalException;

			} finally {
				if (cnx != null) {
					cnx.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			if (!modelDalException.contientErreurs()) {
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CONNEXION_BASE, "Impossible de se connecter à la base de données.");
				System.out.println("Impossible de se connecter à la base de données !");

			}
			throw modelDalException;

		}

		return credit;
	}

	
	/* -------------- BUILDERS -------------- */

	/**
	 * crée un objet Utilisateur à partir des données retournée par une requête
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

		// System.out.println("\nTEST // TEST DAO : Utilisateur créé par utilisateurBuilder : " + utilisateur);

		return utilisateur;
	}

}
