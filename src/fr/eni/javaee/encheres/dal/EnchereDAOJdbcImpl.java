package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

public class EnchereDAOJdbcImpl implements EnchereDAO {

	/* Variables & Constantes */

	private ModelException modelDalException = new ModelException();
	private static final String UPDATE_BID = "UPDATE ENCHERES SET date_enchere = ?, montant_enchere = ?, no_article = ?, no_utilisateur = ? WHERE no_article = ?";
	private static final String INSERT_BID = "INSERT INTO ENCHERES VALUES (?, ?, ?, ?)";
	private static final String UPDATE_ARTICLE_PRICE = "UPDATE Articles_vendus SET prix_vente = ? WHERE no_article = ?";
	private static final String SELECT_LAST_BID = "  SELECT TOP(1) date_enchere, montant_enchere, e.no_utilisateur, pseudo, " 
			+ "e.no_article, a.nom_article, a.no_categorie, libelle " 
			+ "FROM ENCHERES e " 
			+ "INNER JOIN UTILISATEURS u on u.no_utilisateur = e.no_utilisateur "  
			+ "INNER JOIN ARTICLES_VENDUS a on a.no_article = e.no_article " 
			+ "INNER JOIN Categories c on c.no_categorie = a.no_categorie "  
			+ "WHERE a.no_article = ? ORDER BY date_enchere DESC";

	
	/* Constructeur */

	public EnchereDAOJdbcImpl() {
	}

	/* ----------------------------------------- */
	/* ------------- Méthodes CRUD ------------- */
	/* ----------------------------------------- */

	/**
	 * insère une ligne dans la table Encheres
	 * met à jour le prix de vente de l'article dans la table Articles_vendus
	 */
	@Override
	public void insert(Enchere enchere) throws ModelException {

		try {
			/* obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(INSERT_BID);

				/* valorisation des paramètres */
				
//				System.out.println("\nTEST DAO ENCHERE insert // enchere = " + enchere);
				
				query.setTimestamp(1, Timestamp.valueOf(enchere.getDate()));
				query.setInt(2, enchere.getMontant());
				query.setInt(3, enchere.getArticle().getNoArticle());
				query.setInt(4, enchere.getEncherisseur().getNoUtilisateur());

				/* exécution de la requête */
				query.executeUpdate();

				/* Préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_ARTICLE_PRICE);

				/* valorisation des paramètres */
				query2.setInt(1, enchere.getMontant());
				query2.setInt(2, enchere.getArticle().getNoArticle());

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

				/* valorisation des paramètres */
				query.setTimestamp(1, Timestamp.valueOf(enchere.getDate()));
				query.setInt(2, enchere.getMontant());
				query.setInt(3, enchere.getArticle().getNoArticle());
				query.setInt(4, enchere.getEncherisseur().getNoUtilisateur());
				query.setInt(5, enchere.getArticle().getNoArticle());

				/* exécution de la requête */
				query.executeQuery();

				/* Préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_ARTICLE_PRICE);

				/* valorisation des paramètres */
				query2.setInt(1, enchere.getMontant());
				query2.setInt(2, enchere.getArticle().getNoArticle());

				/* exécution de la requête */
				query2.executeUpdate();

				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_BID, "L'exécution de la requête UPDATE_BID ou UPDATE_ARTICLE_PRICE a échoué.");
				System.out.println("L'exécution de la requête UPDATE_BID ou UPDATE_ARTICLE_PRICE a échoué !");

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

	@Override
	public Enchere selectById(Integer number) throws ModelException {
		return null;
	}

	
	/* ----------------------------------------- */
	/* --------------- Méthodes ---------------- */
	/* ----------------------------------------- */
	
	/**
	 * récupère et retourne la dernière enchère effectuée sur un article
	 * s'il n'existe pas de précédente enchère, la valeur retournée sera null
	 */
	@Override
	public Enchere retrieveItemLastBid(Integer noArticle) throws ModelException {

		Enchere derniereEnchere = null;
//		System.out.println("\nTEST DAO ENCHERE // Numéro de l'article = " + noArticle);

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_LAST_BID);

				/* Valorisation des paramètres */
				query.setInt(1, noArticle);

				/* Exécution de la requête */
				ResultSet rs = query.executeQuery();

				/* Récupération du résultat et création d'un objet Enchere */
				if (rs.next()) {
					// System.out.println(rs);
					derniereEnchere = EnchereBuilder(rs);
//					System.out.println("\nTEST DAO ENCHERE // La dernière enchère sur l'article était celle-ci : " + derniereEnchere);
				}

			} catch (SQLException e) {

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

	/**
	 * créé un objet de type Enchere avec le jeu d'enregistrement remonté par la requête SQL
	 * pour utiliser cette méthode, il faut impérativement que la requête SQL projette les noms de colonnes suivants dans cet ordre précis :
	 * date_enchere, montant_enchere, no_utilisateur, pseudo, no_article, nom_article, no_categorie, et libelle
	 * @param rs
	 * @return derniereEnchere
	 * @throws SQLException
	 */
	private Enchere EnchereBuilder(ResultSet rs) throws SQLException {

		Enchere derniereEnchere = new Enchere();

			try {

				derniereEnchere.setDate(rs.getDate(1).toLocalDate().atStartOfDay());
				derniereEnchere.setMontant(rs.getInt(2));

				Utilisateur encherisseur = new Utilisateur();
				encherisseur.setNoUtilisateur(rs.getInt(3));
				encherisseur.setPseudo(rs.getString(4));

				derniereEnchere.setEncherisseur(encherisseur);

				Article article = new Article();
				article.setNoArticle(rs.getInt(5));
				article.setNomArticle(rs.getString(6));

				Categorie categorie = new Categorie();
				categorie.setNoCategorie(rs.getInt(7));
				categorie.setLibelle(rs.getString(8));

				article.setCategorie(categorie);

				derniereEnchere.setArticle(article);

			} catch (SQLException e) {
				e.getMessage();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_ENCHEREBUILDER,
						"Une erreur est survenue dans la méthode EnchereBuilder().");
			}

		return derniereEnchere;
	}

}
