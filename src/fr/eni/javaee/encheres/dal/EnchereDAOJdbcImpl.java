package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;

public class EnchereDAOJdbcImpl implements EnchereDAO {
	
	/* Variables & Constantes */
	
	private ModelException modelDalException = new ModelException();
	private static final String UPDATE_BID = "UPDATE ENCHERES SET date_enchere = ?, montant_enchere = ?, no_article = ?, no_utilisateur = ? WHERE no_article = ?";
	private static final String INSERT_BID = "INSERT INTO ENCHERES VALUES (?, ?, ?, ?)";
	private static final String UPDATE_ARTICLE_PRICE = "UPDATE Articles_vendus SET prix_vente = ? WHERE no_article = ?";
	
	/* Constructeur */
	
	public EnchereDAOJdbcImpl() {
	}
	
	/* Méthodes */
	
	@Override
	public void insert(Enchere enchere) throws ModelException {
		
		try {
			/* obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(INSERT_BID);
				
				/* DEBUG */
				System.out.println("\nDEBUG DAO INSERT_BID // Numéro de l'article " + enchere.getNoArticle());
				System.out.println("\nDEBUG DAO INSERT_BID // Numéro utilisateur " + enchere.getNoUtilisateur());
				
				/* valorisation des paramètres */
				query.setDate(1, Date.valueOf(enchere.getDate()));
				query.setInt(2, enchere.getMontant());
				query.setInt(3, enchere.getNoArticle());
				query.setInt(4, enchere.getNoUtilisateur());
				
				System.out.println("\nTEST DAO // Requête INSERT_BID : " + query);
				
				/* exécution de la requête */
				query.executeUpdate();
				
				/* Préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_ARTICLE_PRICE);
				
				/* valorisation des paramètres */
				query2.setInt(1, enchere.getMontant());
				query2.setInt(2, enchere.getNoArticle());
				
				System.out.println("\nTEST DAO // Requête UPDATE_ARTICLE_PRICE : " + query);

				/* exécution de la requête */
				query2.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERT_BID, "L'exécution de la requête INSERT_BID a échoué.");
				System.out.println("L'exécution de la requête INSERT_BID a échoué !");

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
	public void update(Enchere enchere) throws ModelException {
		
		try {
			/* obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(UPDATE_BID);
				
				/* DEBUG */
				System.out.println("\nDEBUG DAO UPDATE_BID // Numéro de l'article " + enchere.getNoArticle());
				System.out.println("\nDEBUG DAO UPDATE_BID // Numéro utilisateur " + enchere.getNoUtilisateur());
				
				/* valorisation des paramètres */
				query.setDate(1, Date.valueOf(enchere.getDate()));
				query.setInt(2, enchere.getMontant());
				query.setInt(3, enchere.getNoArticle());
				query.setInt(4, enchere.getNoUtilisateur());
				query.setInt(5, enchere.getNoArticle());
				
				/* exécution de la requête */
				query.executeQuery();
				
				/* Préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_ARTICLE_PRICE);
				
				/* valorisation des paramètres */
				query2.setInt(1, enchere.getMontant());
				query2.setInt(2, enchere.getNoArticle());
				
				System.out.println("\nTEST DAO // Requête UPDATE_ARTICLE_PRICE : " + query2);

				/* exécution de la requête */
				query2.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_BID, "L'exécution de la requête UPDATE_BID a échoué.");
				System.out.println("L'exécution de la requête UPDATE_BID a échoué !");

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
	public Enchere selectById(Integer number) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void delete(Integer nb) throws ModelException {
		// TODO Auto-generated method stub
		
	}

}
