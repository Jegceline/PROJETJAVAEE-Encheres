package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;

public class EnchereDAOJdbcImpl implements EnchereDAO {

	/* Variables & Constantes */

	private ModelException modelDalException = new ModelException();
	private static final String UPDATE_BID = "UPDATE ENCHERES SET date_enchere = ?, montant_enchere = ?, no_article = ?, no_utilisateur = ? WHERE no_article = ?";
	private static final String INSERT_BID = "INSERT INTO ENCHERES VALUES (?, ?, ?, ?)";
	private static final String UPDATE_ARTICLE_PRICE = "UPDATE Articles_vendus SET prix_vente = ? WHERE no_article = ?";
	private static final String SELECT_LAST_BID = "SELECT TOP(1) * FROM ENCHERES WHERE no_article = ? ORDER BY date_enchere DESC";

	/* Constructeur */

	public EnchereDAOJdbcImpl() {
	}

	
	/* ----------- Méthodes CRUD ------------ */

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
				query.setTimestamp(1, Timestamp.valueOf(enchere.getDate()));
				// query.setDate(1, Date.valueOf(enchere.getDate()));
				query.setInt(2, enchere.getMontant());
				query.setInt(3, enchere.getNoArticle());
				query.setInt(4, enchere.getNoUtilisateur());;

				/* exécution de la requête */
				query.executeUpdate();

				/* Préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_ARTICLE_PRICE);

				/* valorisation des paramètres */
				query2.setInt(1, enchere.getMontant());
				query2.setInt(2, enchere.getNoArticle());

				/* exécution de la requête */
				query2.executeUpdate();
				
				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();
				
				cnx.rollback();
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
				//System.out.println("\nDEBUG DAO UPDATE_BID // Numéro de l'article " + enchere.getNoArticle());
				//System.out.println("\nDEBUG DAO UPDATE_BID // Numéro utilisateur " + enchere.getNoUtilisateur());

				/* valorisation des paramètres */
				query.setTimestamp(1, Timestamp.valueOf(enchere.getDate()));
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

				/* exécution de la requête */
				query2.executeUpdate();
				
				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();
				
				cnx.rollback();
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
	public void delete(Integer nb) throws ModelException {
		// TODO Auto-generated method stub
		
	}
	
	
	/* --------------- SELECT --------------- */
	
	@Override
	public Enchere selectById(Integer number) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enchere returnLastBid(Integer noArticle) throws ModelException {

		Enchere derniereEnchere = new Enchere();

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_LAST_BID);
				
				/* Valorisation des paramètres */
				// System.out.println("\nTEST DAO ENCHERE // Numéro de l'article = " + noArticle);
				query.setInt(1, noArticle);

				/* Exécution de la requête */
				ResultSet rs = query.executeQuery();
				
				cnx.commit();

				/* Récupération du résultat et création d'un objet Enchere*/
				if(rs.next()) {
					
					//result.getTimestamp("value").toLocalDateTime()
										
					derniereEnchere.setDate(rs.getDate(2).toLocalDate().atStartOfDay());
					derniereEnchere.setMontant(rs.getInt(3));
					derniereEnchere.setNoArticle(rs.getInt(4));
					derniereEnchere.setNoUtilisateur(rs.getInt(5));
				}
				
				 System.out.println("\nTEST DAO ENCHERE // La dernière enchère sur l'article était celle-ci : " + derniereEnchere);
				

			} catch (SQLException e) {
				
				// cnx.rollback();
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL,
						"L'exécution de la requête SELECT_LAST_BID a échoué.");
				System.out.println("L'exécution de la requête SELECT_LAST_BID a échoué !");
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

		return derniereEnchere;
	}

	
	/* -------------- BUILDERS -------------- */
	
//	private Enchere BidBuilder(ResultSet rs) throws SQLException {
//
//		Enchere derniereEnchere = new Enchere();
//
//		if (rs.next()) {
//
//			try {
//				
//				// System.out.println("\nDEBUG DAO ENCHERE BidBuilder // Valeur de la deuxième colonne = " + rs.getInt(2));
//				
//				derniereEnchere.setMontant(rs.getInt(2));
//				derniereEnchere.setNoArticle(rs.getInt(3));
//				derniereEnchere.setNoUtilisateur(rs.getInt(4));
//				derniereEnchere.setDate(rs.getDate(1).toLocalDate());
//
//			} catch (SQLException e) {
//				e.getMessage();
//			}
//
//		}
//		return derniereEnchere;
//	}

}
