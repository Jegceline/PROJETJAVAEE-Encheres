package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Categorie;

public class CategorieDAOJdbcImpl implements CategorieDAO {

	private ModelException modelDalException = new ModelException();
	private final String SELECT_ALL_CATEGORIES = "SELECT * FROM Categories";
	private final String INSERT_CATEGORY = "INSERT INTO Categories (libelle) VALUES (?)";

	/* Constructeur */
	public CategorieDAOJdbcImpl() {
	}

	/* ----------------------------- */
	/* ------ Méthodes CRUD -------- */
	/* ----------------------------- */

	@Override
	public void insert(Categorie categorie) throws ModelException {
		
		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(INSERT_CATEGORY);

				/* valorisation du paramètre */
				query.setString(1, categorie.getLibelle());

				/* exécution de la requête */
				query.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERT_CATEGORY, "L'exécution de la requête INSERT_CATEGORY a échoué.");
				// System.out.println("L'exécution de la requête INSERT_CATEGORY a échoué !");

				throw modelDalException;

			} finally {
				/* fermeture de la connexion */
				ConnectionProvider.closeConnection(cnx);
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
	public void delete(Integer nb) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Categorie objet) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public Categorie selectById(Integer number) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/* ------------------ */
	/* ------------------ */
	/* ----------------- -*/

	public List<Categorie> retrieveAllCategories() throws ModelException {

		List<Categorie> listeCategories = new ArrayList<Categorie>();

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ALL_CATEGORIES);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {

					Categorie categorie = categorieBuilder(rs);
					listeCategories.add(categorie);
				}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_ALL_CATEGORIES, "L'exécution de la requête SELECT_ALL_CATEGORIES a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ALL_CATEGORIES de retrieveCategories a échoué !");

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
		return listeCategories;
	}
	
	
	/* ------------------- */
	/* ----- Builder ----- */
	/* ------------------- */

	private Categorie categorieBuilder(ResultSet rs) {

		Categorie categorie = new Categorie();

		try {

			categorie.setNoCategorie(rs.getInt(1));
			categorie.setLibelle(rs.getString(2));

		} catch (SQLException e) {
			e.printStackTrace();
			modelDalException.ajouterErreur(CodesErreurs.ERREUR_ENCHEREBUILDER, "Une erreur est survenue dans la méthode EnchereBuilder2().");
		}
		
		return categorie;
	}

}
