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
import fr.eni.javaee.encheres.bo.Utilisateur;

public class UtilisateurDAOJdbcImpl implements UtilisateurDAO {

	/* Attributs */

	private ModelException modelDalException = new ModelException();
	private static final String INSERT_USER = "INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, "
			+ "code_postal, ville, mot_de_passe, credit, administrateur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_ALL_EMAILS = "SELECT email FROM Utilisateurs";
	private static final String SELECT_ALL_PSEUDO = "SELECT pseudo FROM Utilisateurs";
	private static final String SELECT_PASSWORD = "SELECT mot_de_passe FROM Utilisateurs WHERE email = ? OR pseudo = ?";
	private static final String SELECT_USER = "SELECT * FROM Utilisateurs WHERE email = ? OR pseudo = ?";
	private static final String DELETE_USER = "DELETE FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String UPDATE_USER = "UPDATE Utilisateurs SET pseudo = ?, nom = ?, prenom = ?, email = ?, telephone = ?, rue = ?, "  
			+ "code_postal = ?, ville = ?, mot_de_passe = ?, credit = ?, administrateur = ? WHERE no_utilisateur = ?";

	/* Constructeur */

	public UtilisateurDAOJdbcImpl() {
	}

	/* Méthodes */

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

				// récupération de la clé primaire/identity générée par la BDD
				ResultSet rs = query.getGeneratedKeys();
				if (rs.next()) {
					// valorisation de l'attribut Id de l'objet repas avec cette clé
					utilisateur.setNoUtilisateur(rs.getInt(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête INSERT a échoué.");
				System.out.println("L'exécution de la requête INSERT a échoué !");
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
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_MAILS_SQL, "L'exécution de la requête SELECT a échoué.");
				System.out.println("L'exécution de la requête SELECT a échoué !");
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
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL, "L'exécution de la requête SELECT a échoué.");
				System.out.println("L'exécution de la requête SELECT a échoué !");
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

				// System.out.println("\nTEST DAO : Le mot de passe de cet utilisateur est : " + password);

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PASSWORD, "L'exécution de la requête SELECT a échoué.");
				System.out.println("\n TEST DAO : L'exécution de la requête SELECT a échoué !");
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

		System.out.println(password);
		return password;

	}

	/**
	 * récupère un utilisateur, construit un objet Utilisateur et le retourne au manager
	 */
	@Override
	public Utilisateur retrieveUserInfo(String identifiant) throws ModelException {

		Utilisateur utilisateur = null;

		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la requête
				PreparedStatement query = cnx.prepareStatement(SELECT_USER);
				
				// valorisation du paramètre
				query.setString(1, identifiant);
				query.setString(2, identifiant);

				// exécution de la requête
				ResultSet rs = query.executeQuery();
				

				utilisateur = utilisateurBuilder(rs);

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL, "L'exécution de la requête SELECT a échoué.");
				System.out.println("L'exécution de la requête SELECT a échoué !");
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

	private Utilisateur utilisateurBuilder(ResultSet rs) throws SQLException {

		Utilisateur utilisateur = new Utilisateur();
		while (rs.next()) {
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
		}

		System.out.println("\nTEST // Utilisateur créé dans la DAO : " + utilisateur);

		return utilisateur;
	}

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

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_DELETE_SQL, "L'exécution de la requête DELETE a échoué.");
				System.out.println("L'exécution de la requête DELETE a échoué !");

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

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_SQL, "L'exécution de la requête UPDATE a échoué.");
				System.out.println("L'exécution de la requête UPDATE a échoué !");

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
}
