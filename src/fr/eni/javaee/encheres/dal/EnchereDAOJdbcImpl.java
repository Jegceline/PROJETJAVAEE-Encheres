package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
		+ "e.no_article, a.nom_article, a.no_categorie, libelle " + "FROM ENCHERES e " + "INNER JOIN UTILISATEURS u on u.no_utilisateur = e.no_utilisateur "
		+ "INNER JOIN ARTICLES_VENDUS a on a.no_article = e.no_article " + "INNER JOIN Categories c on c.no_categorie = a.no_categorie "
		+ "WHERE a.no_article = ? ORDER BY date_enchere DESC";
	private static final String SELECT_ALL_USER_BIDS = "SELECT no_enchere FROM Encheres WHERE no_utilisateur = ?";
	private static final String SELECT_ALL_BIDS_USER_HAVE_ON_ITEMS_CURRENTLY_ON_SELL = "SELECT no_enchere FROM Encheres e INNER JOIN Articles_vendus a "
		+ "ON e.no_article = a.no_article WHERE a.date_debut_encheres < getdate() AND a.date_fin_encheres > getdate() AND e.no_utilisateur = ?";
	private static final String DELETE_BID = "DELETE FROM Encheres WHERE no_enchere = ?";
	private static final String SELECT_ALL_ITEM_BIDS = "SELECT date_enchere, montant_enchere, e.no_utilisateur, pseudo "
		+ "FROM Encheres e INNER JOIN UTILISATEURS u on u.no_utilisateur = e.no_utilisateur WHERE no_article = ? ORDER BY date_enchere DESC";
	
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
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			cnx.setAutoCommit(false);

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(INSERT_BID);

				/* valorisation des paramètres */

				//				System.out.println("\nTEST DAO ENCHERE insert // enchere = " + enchere);

				query.setTimestamp(1, Timestamp.valueOf(enchere.getDate()));
				query.setInt(2, enchere.getMontant());
				query.setInt(3, enchere.getArticle().getNoArticle());
				query.setInt(4, enchere.getEncherisseur().getNoUtilisateur());

				/* exécution de la requête */
				query.executeUpdate();

				/* préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_ARTICLE_PRICE);

				/* valorisation des paramètres */
				query2.setInt(1, enchere.getMontant());
				query2.setInt(2, enchere.getArticle().getNoArticle());

				/* exécution de la requête */
				query2.executeUpdate();

				/* commit des deux requêtes */
				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERT_BID, "L'exécution de la requête INSERT_BID a échoué.");
				// System.out.println("L'exécution de la requête INSERT_BID a échoué !");

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
	public void update(Enchere enchere) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			cnx.setAutoCommit(false);

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(UPDATE_BID);

				/* valorisation des paramètres */
				query.setTimestamp(1, Timestamp.valueOf(enchere.getDate()));
				query.setInt(2, enchere.getMontant());
				query.setInt(3, enchere.getArticle().getNoArticle());
				query.setInt(4, enchere.getEncherisseur().getNoUtilisateur());
				query.setInt(5, enchere.getArticle().getNoArticle());

				/* exécution de la requête */
				query.executeQuery();

				/* préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_ARTICLE_PRICE);

				/* valorisation des paramètres */
				query2.setInt(1, enchere.getMontant());
				query2.setInt(2, enchere.getArticle().getNoArticle());

				/* exécution de la requête */
				query2.executeUpdate();

				/* on commit les deux requêtes */
				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_BID, "L'exécution de la requête UPDATE_BID ou UPDATE_ARTICLE_PRICE a échoué.");
				// System.out.println("L'exécution de la requête UPDATE_BID ou UPDATE_ARTICLE_PRICE a échoué !");

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

	@Override
	public void delete(Integer noUtilisateur) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(DELETE_BID);

				/* valorisation du paramètre */
				query.setInt(1, noUtilisateur);

				/* exécution de la requête */
				query.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_DELETE_BID, "L'exécution de la requête DELETE_BID a échoué.");
				// System.out.println("L'exécution de la requête DELETE_BID a échoué !");

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

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_LAST_BID);

				/* valorisation des paramètres */
				query.setInt(1, noArticle);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				/* récupération du résultat et création d'un objet Enchere */
				if (rs.next()) {

					derniereEnchere = EnchereBuilder(rs);
					//					System.out.println("\nTEST DAO ENCHERE retrieveItemLastBid // La dernière enchère sur l'article était celle-ci : " + derniereEnchere);
				}

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL, "L'exécution de la requête SELECT_LAST_BID a échoué.");
				// System.out.println("L'exécution de la requête SELECT_LAST_BID a échoué !");

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

		return derniereEnchere;
	}

	/**
	 * récupère et retourne les numéros des enchères émises par l'utilisateur sur des articles en cours de vente
	 */
	@Override
	public List<Integer> retrieveAllBidsUserHavePutOnCurrentlyOnSaleItems(Integer noUtilisateur) throws ModelException {

		List<Integer> listeNoEncheres = new ArrayList<Integer>();

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ALL_BIDS_USER_HAVE_ON_ITEMS_CURRENTLY_ON_SELL);

				/* valorisation des paramètres */
				query.setInt(1, noUtilisateur);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				/* récupération du résultat et création d'un objet Enchere */
				while (rs.next()) {
					listeNoEncheres.add(rs.getInt(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_PSEUDO_SQL, "L'exécution de la requête SELECT_LAST_BID a échoué.");
				// System.out.println("L'exécution de la requête SELECT_LAST_BID de retrieveAllBidsUserHavePutOnCurrentlyOnSaleItems a échoué !");

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

		return listeNoEncheres;

	}

	/**
	 * récupère et supprime toutes les enchères émises par un utilisateur 
	 * @param noUtilisateur
	 * @throws ModelException
	 */
	public void retrieveAndDeleteAllUserBids(Integer noUtilisateur, Connection cnx) throws ModelException {
	
			try {
	
				try {
					/* préparation de la requête */
					PreparedStatement query = cnx.prepareStatement(SELECT_ALL_USER_BIDS);
	
					/* valorisation des paramètres */
					query.setInt(1, noUtilisateur);
	
					/* exécution de la requête */
					ResultSet rs = query.executeQuery();
	
					/* supression de chacune des enchères récupérées */
					while (rs.next()) {
						delete(rs.getInt(1));
					}
	
				} catch (SQLException e) {
					e.printStackTrace();
					
					modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_ALL_USER_BIDS, "L'exécution de la requête SELECT_ALL_USER_BIDS a échoué.");
					// System.out.println("L'exécution de la requête SELECT_LAST_BID a échoué !");
					
					throw modelDalException;
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

	public List<Enchere> retrieveAllItemBids(Integer noArticle) throws ModelException{
		List<Enchere> listeEncheres = new ArrayList<Enchere>();
		
		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ALL_ITEM_BIDS);

				/* valorisation des paramètres */
				query.setInt(1, noArticle);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				/* récupération du résultat, création d'un objet Enchere, ajout dans la liste */
				while (rs.next()) {
					Enchere enchere = EnchereBuilder2(rs);
					listeEncheres.add(enchere);
				}

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_ALL_ITEM_BIDS, "L'exécution de la requête SELECT_ALL_ITEM_BIDS a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ALL_ITEM_BIDS de retrieveAllItemBids a échoué !");

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
		return listeEncheres;
		
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

		Enchere enchere = new Enchere();

		try {

			enchere.setDate(rs.getDate(1).toLocalDate().atStartOfDay());
			enchere.setMontant(rs.getInt(2));

			Utilisateur encherisseur = new Utilisateur();
			encherisseur.setNoUtilisateur(rs.getInt(3));
			encherisseur.setPseudo(rs.getString(4));

			enchere.setEncherisseur(encherisseur);

			Article article = new Article();
			article.setNoArticle(rs.getInt(5));
			article.setNomArticle(rs.getString(6));

			Categorie categorie = new Categorie();
			categorie.setNoCategorie(rs.getInt(7));
			categorie.setLibelle(rs.getString(8));

			article.setCategorie(categorie);

			enchere.setArticle(article);

		} catch (SQLException e) {
			e.getMessage();
			modelDalException.ajouterErreur(CodesErreurs.ERREUR_ENCHEREBUILDER, "Une erreur est survenue dans la méthode EnchereBuilder().");
		}

		return enchere;
	}

	private Enchere EnchereBuilder2(ResultSet rs) throws SQLException {

		Enchere enchere = new Enchere();

		try {

			enchere.setDate(rs.getDate(1).toLocalDate().atStartOfDay());
			enchere.setMontant(rs.getInt(2));

			Utilisateur encherisseur = new Utilisateur();
			encherisseur.setNoUtilisateur(rs.getInt(3));
			encherisseur.setPseudo(rs.getString(4));

			enchere.setEncherisseur(encherisseur);

		} catch (SQLException e) {
			e.printStackTrace();
			modelDalException.ajouterErreur(CodesErreurs.ERREUR_ENCHEREBUILDER, "Une erreur est survenue dans la méthode EnchereBuilder2().");
		}
		return enchere;
	}

}
