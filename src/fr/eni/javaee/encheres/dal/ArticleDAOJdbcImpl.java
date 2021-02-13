package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Adresse;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Trieur;
import fr.eni.javaee.encheres.bo.Utilisateur;

public class ArticleDAOJdbcImpl implements ArticleDAO {

	private ModelException modelDalException = new ModelException();
	// private DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();

	/* Constantes */

	private static final String INSERT_ITEM = "INSERT INTO Articles_Vendus(nom_article, description, date_debut_encheres, "
		+ "date_fin_encheres, prix_initial, no_utilisateur, no_categorie) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_PICKUP_ADDRESS = "INSERT INTO Retraits (no_article, rue, code_postal, ville) VALUES (?, ?, ?, ?)";

	private static final String UPDATE_ITEM = "UPDATE Articles_vendus SET nom_article = ?, description = ?, date_debut_encheres = ?, date_fin_encheres = ?, "
		+ "prix_initial = ?, no_categorie = ? WHERE no_article = ?";
	private static final String UPDATE_PICKUP_ADDRESS = "UPDATE Retraits SET rue = ?, code_postal = ?, ville = ? WHERE no_article = ?";
	private static final String UPDATE_ITEM_PAYMENT_STATUS = "UPDATE Articles_Vendus SET vendeur_paye = ? WHERE no_article = ?";

	private static final String DELETE_ITEM = "DELETE FROM Articles_vendus WHERE no_article = ?";
	private static final String DELETE_PICKUP_ADDRESS = "DELETE FROM Retraits WHERE no_article = ?";

	private static final String SELECT_ARTICLE_BY_ID = "SELECT a.no_article, nom_article, description, date_debut_encheres, date_fin_encheres, "
		+ "prix_initial, prix_vente, a.no_utilisateur, pseudo, a.no_categorie, libelle, r.rue, r.code_postal, r.ville " + "FROM Articles_vendus a "
		+ "INNER JOIN Retraits r ON a.no_article = r.no_article " + "INNER JOIN Utilisateurs u on u.no_utilisateur = a.no_utilisateur "
		+ "INNER JOIN Categories c on a.no_categorie = c.no_categorie " + "WHERE a.no_article = ?";

	private static final String SELECT_ITEM_STARTING_PRICE = "SELECT prix_initial FROM Articles_vendus WHERE no_article = ?";

	private static final String SELECT_ITEM_CURRENT_PRICE = "SELECT prix_vente FROM Articles_vendus WHERE no_article = ?";

	private static final String SELECT_BID_STARTING_DATETIME = "SELECT date_debut_encheres FROM ARTICLES_VENDUS WHERE no_article = ?";

	private static final String SELECT_BID_CLOSING_DATETIME = "SELECT date_fin_encheres FROM ARTICLES_VENDUS WHERE no_article = ?";

	private static final String SELECT_USER_SELLS = "SELECT a.no_article, nom_article, description, date_debut_encheres, "
		+ "date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, u.pseudo, a.no_categorie, libelle, r.rue, r.code_postal, r.ville "
		+ "FROM Articles_vendus a " + "INNER JOIN Retraits r ON a.no_article = r.no_article " + "INNER JOIN Categories c on a.no_categorie = c.no_categorie "
		+ "INNER JOIN Utilisateurs u on u.no_utilisateur = a.no_utilisateur " + "WHERE a.no_utilisateur = ?";

	private static final String SELECT_SELLER_NOT_YET_PAID_ITEMS = "SELECT a.no_article, nom_article, description, date_debut_encheres, "
		+ "date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, u.pseudo, a.no_categorie, libelle, r.rue, r.code_postal, r.ville "
		+ "FROM Articles_vendus a " + "INNER JOIN Retraits r ON a.no_article = r.no_article " + "INNER JOIN Categories c on a.no_categorie = c.no_categorie "
		+ "INNER JOIN Utilisateurs u on u.no_utilisateur = a.no_utilisateur "
		+ "WHERE a.no_utilisateur = ? AND a.date_fin_encheres < GETDATE() AND a.vendeur_paye = 0";

	private static final String SELECT_ITEMS_USER_HAVE_BIDS_ON = "SELECT DISTINCT a.no_article, nom_article, description, "
		+ "date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, u.pseudo, a.no_categorie, libelle, r.rue, r.code_postal, r.ville "
		+ "FROM ENCHERES e " + "INNER JOIN Articles_vendus a on a.no_article = e.no_article " + "INNER JOIN Categories c on a.no_categorie = c.no_categorie "
		+ "INNER JOIN Retraits r ON a.no_article = r.no_article " + "INNER JOIN Utilisateurs u on u.no_utilisateur = a.no_utilisateur "
		+ "WHERE e.no_utilisateur = ?";

	private static final String SELECT_USER_PURCHASED_ITEMS = "SELECT a.no_article, nom_article, description, date_debut_encheres, "
		+ "date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, u.pseudo, " + "a.no_categorie, libelle, r.rue, r.code_postal, r.ville "
		+ "FROM Articles_vendus a " + "INNER JOIN Encheres e on e.no_article = a.no_article " + "INNER JOIN RETRAITS r on a.no_article = r.no_article "
		+ "INNER JOIN Categories c on a.no_categorie = c.no_categorie " + "INNER JOIN Utilisateurs u on u.no_utilisateur = a.no_utilisateur "
		+ "WHERE e.no_utilisateur = ? " + "AND date_fin_encheres < getdate()";

	private static final String SELECT_CURRENTLY_ON_SALE_ITEMS = "SELECT a.no_article, nom_article, description, date_debut_encheres, "
		+ "date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, u.pseudo, a.no_categorie, libelle, r.rue, r.code_postal, r.ville "
		+ "FROM Articles_vendus a " + "INNER JOIN Retraits r ON a.no_article = r.no_article " + "INNER JOIN Categories c on a.no_categorie = c.no_categorie "
		+ "INNER JOIN Utilisateurs u on u.no_utilisateur = a.no_utilisateur " + "WHERE (date_debut_encheres <= GETDATE() "
		+ "AND date_fin_encheres > GETDATE()) ";

	private static final String SELECT_CURRENTLY_ON_SALE_ITEMS_AND_SALES_TO_COME = "SELECT a.no_article, nom_article, description, date_debut_encheres, "
		+ "date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, u.pseudo, a.no_categorie, libelle, r.rue, r.code_postal, r.ville "
		+ "FROM Articles_vendus a " + "INNER JOIN Retraits r ON a.no_article = r.no_article " + "INNER JOIN Categories c on a.no_categorie = c.no_categorie "
		+ "INNER JOIN Utilisateurs u on u.no_utilisateur = a.no_utilisateur " + "WHERE date_fin_encheres > GETDATE() ";

	/* Constructeur */

	public ArticleDAOJdbcImpl() {
	}

	/* ----------- Méthodes C-U-D ------------ */

	/**
	 * ajoute une ligne (un article) dans la table Articles_vendus ajoute une ligne
	 * (l'adresse de retrait de l'article) dans la table Retraits
	 */
	@Override
	public void insert(Article article) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			cnx.setAutoCommit(false);

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(INSERT_ITEM, Statement.RETURN_GENERATED_KEYS);

				/* valorisation des paramètres */
				query.setString(1, article.getNomArticle());
				query.setString(2, article.getDescription());
				query.setTimestamp(3, Timestamp.valueOf(article.getDateDebutEncheres().atTime(article.getHeureDebutEncheres())));
				query.setTimestamp(4, Timestamp.valueOf(article.getDateFinEncheres().atTime(article.getHeureFinEncheres())));
				query.setInt(5, article.getPrixInitial());
				query.setInt(6, article.getVendeur().getNoUtilisateur());
				query.setInt(7, article.getCategorie().getNoCategorie());

				/* exécution de la requête */
				query.executeUpdate();

				/* récupération de la clé primaire/identity générée par la BDD */
				ResultSet rs = query.getGeneratedKeys();

				if (rs.next()) {
					/* valorisation de l'attribut Id de l'objet article avec cette clé */
					article.setNoArticle(rs.getInt(1));
				}

				/* préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(INSERT_PICKUP_ADDRESS, Statement.RETURN_GENERATED_KEYS);

				/* valorisation des paramètres */
				query2.setInt(1, article.getNoArticle());
				query2.setString(2, article.getAdresseRetrait().getRue());
				query2.setInt(3, article.getAdresseRetrait().getCodePostal());
				query2.setString(4, article.getAdresseRetrait().getVille());

				/* exécution de la requête */
				query2.executeUpdate();

				/* on commit les deux requêtes */
				cnx.commit();

			} catch (Exception e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête INSERT_ARTICLE a échoué.");
				System.out.println("L'exécution de la requête INSERT_ARTICLE a échoué !");
				throw modelDalException;

			} finally {
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

	/**
	 * supprime un article identifié par sa primary key
	 */
	@Override
	public void delete(Integer noArticle) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			cnx.setAutoCommit(false);

			try {

				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(DELETE_PICKUP_ADDRESS);

				/* valorisation du paramètre */
				query.setInt(1, noArticle);

				/* exécution de la requête */
				query.executeUpdate();

				/* préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(DELETE_ITEM);

				/* valorisation du paramètre */
				query2.setInt(1, noArticle);

				/* exécution de la seconde requête */
				query2.executeUpdate();

				/* on commit les deux requêtes */
				cnx.commit();

			} catch (Exception e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL,
					"L'exécution de la requête DELETE_PICKUP_ADDRESS ou de la requête DELETE_ITEM a échoué.");
				// System.out.println("L'exécution de la requête DELETE_PICKUP_ADDRESS ou de la requête DELETE_ITEM a échoué.");

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

	public void delete(Integer noArticle, Connection cnx) throws ModelException {

			try {

				try {

					/* préparation de la première requête */
					PreparedStatement query = cnx.prepareStatement(DELETE_PICKUP_ADDRESS);

					/* valorisation du paramètre */
					query.setInt(1, noArticle);

					/* exécution de la requête */
					query.executeUpdate();

					/* préparation de la seconde requête */
					PreparedStatement query2 = cnx.prepareStatement(DELETE_ITEM);

					/* valorisation du paramètre */
					query2.setInt(1, noArticle);

					/* exécution de la seconde requête */
					query2.executeUpdate();

					/* on commit les deux requêtes */
					cnx.commit();

				} catch (Exception e) {
					e.printStackTrace();

					cnx.rollback();
					modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL,
						"L'exécution de la requête DELETE_PICKUP_ADDRESS ou de la requête DELETE_ITEM a échoué.");
					// System.out.println("L'exécution de la requête DELETE_PICKUP_ADDRESS ou de la requête DELETE_ITEM a échoué.");

					throw modelDalException;

				} finally {
					// ConnectionProvider.closeConnection(cnx);
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
	 * met à jour une ligne (un article) dans la table Articles_vendus met à jour
	 * une ligne (l'adresse de retrait de l'article) dans la table Retraits
	 */
	@Override
	public void update(Article article) throws ModelException {

		// System.out.println("\nTEST DAO ARTICLE update() // article = " + article);

		try {
			/* obtention d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();
			cnx.setAutoCommit(false);

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(UPDATE_ITEM);

				/* valorisation des paramètres */
				query.setString(1, article.getNomArticle());
				query.setString(2, article.getDescription());
				query.setTimestamp(3, Timestamp.valueOf(article.getDateDebutEncheres().atTime(article.getHeureDebutEncheres())));
				query.setTimestamp(4, Timestamp.valueOf(article.getDateFinEncheres().atTime(article.getHeureFinEncheres())));
				query.setInt(5, article.getPrixInitial());
				query.setInt(6, article.getCategorie().getNoCategorie());
				query.setInt(7, article.getNoArticle());

				/* exécution de la requête */
				query.executeUpdate();

				/* préparation de la seconde requête pour modifier l'adresse de retrait */
				PreparedStatement query2 = cnx.prepareStatement(UPDATE_PICKUP_ADDRESS);

				/* valorisation du paramètre */
				query2.setString(1, article.getAdresseRetrait().getRue());
				query2.setInt(2, article.getAdresseRetrait().getCodePostal());
				query2.setString(3, article.getAdresseRetrait().getVille());
				query2.setInt(4, article.getNoArticle());

				/* exécution de la requête */
				query2.executeUpdate();

				/* on commit les deux requêtes */
				cnx.commit();

			} catch (Exception e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_ITEM, "L'exécution de la requête UPDATE_ITEM a échoué.");
				// System.out.println("L'exécution de la requête UPDATE_ITEM a échoué !");

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

	/* passe le statut "paye" d'un article à true */
	@Override
	public void updateItemPaymentStatus(Integer noArticle) throws ModelException {

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(UPDATE_ITEM_PAYMENT_STATUS);

				/* valorisation des paramètres */
				query.setBoolean(1, true);
				query.setInt(2, noArticle);

				/* exécution de la requête */
				query.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_UPDATE_ITEM_PAYMENT_STATUS,
					"L'exécution de la requête UPDATE_ITEM_PAYMENT_STATUS a échoué.");
				// System.out.println("L'exécution de la requête UPDATE_ITEM_PAYMENT_STATUS a échoué !");

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

	/* ------------------------------------------------------------------- */
	/* ------ Méthodes de tri qui retournent une liste d'articles -------- */
	/* ------------------------------------------------------------------- */

	/**
	 * récupère et retourne les enchères gagnantes d'un utilisateur
	 */
	@Override
	public List<Article> retrieveUserPurchasedItems(Trieur trieur) throws ModelException {

		List<Article> listeEncheresRemportees = new ArrayList<Article>();

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_USER_PURCHASED_ITEMS);

				/* valorisation du paramètre */
				query.setInt(1, trieur.getUtilisateur().getNoUtilisateur());

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeEncheresRemportees.add(article);
				}

				// System.out.println("\nTEST DAO Article retrieveUserPurchasedItems // Liste des articles remportés : " + listeEncheresRemportees);

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête SELECT_USER_PURCHASED_ITEMS a échoué.");
				// System.out.println("L'exécution d'une des requêtes SELECT_USER_PURCHASED_ITEMS a échoué !");

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

		return listeEncheresRemportees;

	}

	/**
	 * récupère et retourne les articles pour lesquels les enchères sont ouvertes
	 */
	@Override
	public List<Article> retrieveCurrentlyForSaleItemsGet() throws ModelException {

		List<Article> listeEncheresEnCours = new ArrayList<Article>();

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_CURRENTLY_ON_SALE_ITEMS);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeEncheresEnCours.add(article);
				}

				// System.out.println("\nTEST DAO // Liste des enchères en cours : " + listeEncheresEnCours);

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête SELECT_CURRENTLY_ON_SALE_ITEMS a échoué.");
				// System.out.println("L'exécution de la requête SELECT_CURRENTLY_ON_SALE_ITEMS a échoué !");

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

		return listeEncheresEnCours;

	}

	/**
	 * récupère et retourne certains des articles pour lesquels les enchères sont
	 * ouvertes
	 */
	@Override
	public List<Article> retrieveCurrentlyForSaleItemsWithFilter(Trieur trieur) throws ModelException {

		List<Article> listeEncheresEnCours = new ArrayList<Article>();
		String select_articles_filtres = SELECT_CURRENTLY_ON_SALE_ITEMS;

		if (trieur.getCategorie() != 0) {
			select_articles_filtres += " AND (a.no_categorie = ?)";
			// System.out.println("\nTEST DAO // Requête SQL avec catégorie : " + select_articles_filtres);
		}

		if (!trieur.getKeyword().isEmpty()) {
			select_articles_filtres += " AND (a.nom_article LIKE ?)";
			// System.out.println("\nTEST DAO // Requête SQL avec catégorie : " + select_articles_filtres);
		}

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation des paramètres */

				if (trieur.getCategorie() != 0 && !trieur.getKeyword().isEmpty()) {
					// System.out.println("tic"); // debug
					query.setInt(1, trieur.getCategorie());
					query.setString(2, "%" + trieur.getKeyword() + "%");
				}

				if (trieur.getCategorie() != 0 && trieur.getKeyword().isEmpty()) {
					// System.out.println("tac"); // debug
					query.setInt(1, trieur.getCategorie());
				}

				if (trieur.getCategorie() == 0 && !trieur.getKeyword().isEmpty()) {
					// System.out.println("toc"); // debug
					query.setString(1, "%" + trieur.getKeyword() + "%");
				}

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeEncheresEnCours.add(article);
				}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CURRENTLY_ON_SALE_ITEMS,
					"L'exécution de la requête SELECT_CURRENTLY_ON_SALE_ITEMS a échoué.");
				// System.out.println("L'exécution de la requête SELECT_CURRENTLY_ON_SALE_ITEMS a échoué !");

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

		// System.out.println("\nTEST DAO // Liste des enchères en cours : " + listeEncheresEnCours);
		return listeEncheresEnCours;

	}

	/**
	 * récupère et retourne certains des articles pour lequels les enchères sont
	 * ouvertes ou en attente
	 */
	@Override
	public List<Article> retrieveCurrentlyForSaleItemsAndSalesToComeWithFilter(Trieur trieur) throws ModelException {

		List<Article> listeEncheresEnCours = new ArrayList<Article>();
		String select_articles_filtres = SELECT_CURRENTLY_ON_SALE_ITEMS_AND_SALES_TO_COME;

		if (trieur.getCategorie() != 0) {
			select_articles_filtres += " AND (a.no_categorie = ?)";
			// System.out.println("\nTEST DAO // Requête SQL avec catégorie : " + select_articles_filtres);
		}

		if (!trieur.getKeyword().isEmpty()) {
			select_articles_filtres += " AND (a.nom_article LIKE ?)";
			// System.out.println("\nTEST DAO // Requête SQL avec catégorie : " + select_articles_filtres);
		}

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation des paramètres */

				if (trieur.getCategorie() != 0 && !trieur.getKeyword().isEmpty()) {
					// System.out.println("tic"); // debug
					query.setInt(1, trieur.getCategorie());
					query.setString(2, "%" + trieur.getKeyword() + "%");
				}

				if (trieur.getCategorie() != 0 && trieur.getKeyword().isEmpty()) {
					// System.out.println("tac"); // debug
					query.setInt(1, trieur.getCategorie());
				}

				if (trieur.getCategorie() == 0 && !trieur.getKeyword().isEmpty()) {
					// System.out.println("toc"); // debug
					query.setString(1, "%" + trieur.getKeyword() + "%");
				}

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeEncheresEnCours.add(article);
				}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_CURRENTLY_ON_SALE_ITEMS,
					"L'exécution de la requête SELECT_CURRENTLY_ON_SALE_ITEMS_AND_SALES_TO_COME a échoué.");
				// System.out.println("L'exécution de la requête SELECT_CURRENTLY_ON_SALE_ITEMS_AND_SALES_TO_COME a échoué !");

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

		// System.out.println("\nTEST DAO // Liste des enchères en cours : " + listeEncheresEnCours);
		return listeEncheresEnCours;

	}

	/**
	 * récupère et retourne certains des articles mis en vente par l'utilisateur
	 */
	@Override
	public List<Article> retrieveUserSellsWithFilter(Trieur trieur) throws ModelException {

		List<Article> listeArticlesFiltres = new ArrayList<Article>();

		String select_articles_filtres = SELECT_USER_SELLS;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {

				if (trieur.getVentesUtilisateurAttente() != null) {
					select_articles_filtres += " AND date_debut_encheres > GETDATE()";
				}

				if (trieur.getVentesUtilisateurEC() != null) {
					select_articles_filtres += " AND date_debut_encheres <= GETDATE() AND date_fin_encheres > GETDATE()";
				}

				if (trieur.getVentesUtilisateurT() != null) {
					select_articles_filtres += " AND date_fin_encheres <= GETDATE()";
				}

				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation du paramètre */
				query.setInt(1, trieur.getUtilisateur().getNoUtilisateur());

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeArticlesFiltres.add(article);
				}

				// System.out.println("\nTEST DAO ARTICLE ArticleBuilder // listeArticlesFiltres : " + listeArticlesFiltres);

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL, "L'exécution de la requête SELECT_USER_SELLS a échoué.");
				// System.out.println("L'exécution d'une des requêtes SELECT_USER_SELLS de retrieveUserSellsWithFilter a échoué !");

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

		return listeArticlesFiltres;
	}

	/*
	 * récupère et retourne les articles du vendeur pour lesquels les enchères sont
	 * terminées et pour lesquels le vendeur n'a pas été payé
	 */
	@Override
	public List<Article> retrieveSellerYetNotPaidItems(Integer noVendeur) throws ModelException {

		List<Article> listeArticlesEncheresCloturees = new ArrayList<Article>();

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_SELLER_NOT_YET_PAID_ITEMS);

				/* valorisation du paramètre */
				query.setInt(1, noVendeur);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeArticlesEncheresCloturees.add(article);
				}

				// System.out.println("\nTEST DAO ARTICLE ArticleBuilder // listeArticlesFiltres : " + listeArticlesFiltres);

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL, "L'exécution de la requête SELECT_USER_SELLS a échoué.");
				// System.out.println("L'exécution d'une des requêtes SELECT_USER_SELLS a échoué !");

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

		return listeArticlesEncheresCloturees;
	}

	/**
	 * récupère et retourne les articles sur lequels l'utilisateur a effectué au
	 * moins une enchère
	 */
	@Override
	public List<Article> retrieveUserWishlistWithFilter(Trieur trieur) throws ModelException {

		List<Article> listeArticlesFiltres = new ArrayList<Article>();

		String select_articles_filtres = SELECT_ITEMS_USER_HAVE_BIDS_ON;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {

				if (trieur.getEncheresUtilisateurEC() != null) {
					select_articles_filtres += " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres > GETDATE())";
				}

				if (trieur.getEncheresUtilisateurG() != null) {
					select_articles_filtres += " AND a.date_fin_encheres < getdate()";
				}

				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation du paramètre */
				query.setInt(1, trieur.getUtilisateur().getNoUtilisateur());

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeArticlesFiltres.add(article);
				}

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL, "L'exécution de la requête SELECT_ITEMS_USER_HAVE_BIDS_ON a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ITEMS_USER_HAVE_BIDS_ON a échoué !");

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

		// System.out.println("\nTEST DAO // Liste des enchères en cours : " + listeEncheresEnCours);

		return listeArticlesFiltres;
	}

	/**
	 * récupère et retourne les articles répondant à certains critères
	 */
	@Override
	public List<Article> retrieveItemsByCriteria(Trieur trieur) throws ModelException {
		List<Article> listeArticlesFiltres = new ArrayList<Article>();

		String select_articles_filtres = SELECT_CURRENTLY_ON_SALE_ITEMS;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {

				if (trieur.getEncheresEC() != null) {
					select_articles_filtres += " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres > GETDATE())";
				}

				if (trieur.getCategorie() != 0) {
					select_articles_filtres += " AND (no_categorie = ?)";
					System.out.println("\nTEST DAO // Requête SQL avec catégorie : " + select_articles_filtres);
				}

				if (!trieur.getKeyword().isEmpty()) {
					select_articles_filtres += " AND (nom_article LIKE ?)";
					System.out.println("\nTEST DAO // Requête SQL avec mot clé : " + select_articles_filtres);
				}

				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation des paramètres */

				if (trieur.getCategorie() != 0 && !trieur.getKeyword().isEmpty()) {
					System.out.println("tic"); // debug
					query.setInt(1, trieur.getCategorie());
					query.setString(2, "%" + trieur.getKeyword() + "%");
				}

				if (trieur.getCategorie() != 0 && trieur.getKeyword().isEmpty()) {
					System.out.println("tac"); // debug
					query.setInt(1, trieur.getCategorie());
				}

				if (trieur.getCategorie() == 0 && !trieur.getKeyword().isEmpty()) {
					System.out.println("toc"); // debug
					query.setString(1, "%" + trieur.getKeyword() + "%");
				}

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				Integer noArticle;
				Enchere enchere;

				while (rs.next()) {

					Article article = articleBuilder(rs);
					noArticle = (rs.getInt(1));

					/* on récupère la dernière enchére effectuée sur l'article */
					if (noArticle != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(noArticle);

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}

					listeArticlesFiltres.add(article);
				}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL, "L'exécution de la requête SELECT_CURRENTLY_ON_SALE_ITEMS a échoué.");
				// System.out.println("L'exécution d'une des requêtes SELECT_CURRENTLY_ON_SALE_ITEMS a échoué !");

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

		// System.out.println("\nTEST DAO // Liste des enchères en cours : " + listeEncheresEnCours);

		return listeArticlesFiltres;
	}

	/**
	 * récupère et retourne les numéros des articles que l'utilisateur a actuellement en vente
	 */
	@Override
	public List<Integer> retrieveAllUserCurrentlyOnSaleItems(Integer noUtilisateur) throws ModelException {
		
		List<Integer> listeNoArticles = new ArrayList<Integer>();
		String select_articles_filtres = SELECT_USER_SELLS;
		select_articles_filtres += " AND date_debut_encheres <= GETDATE() AND date_fin_encheres > GETDATE()";

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation du paramètre */
				query.setInt(1, noUtilisateur);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
						listeNoArticles.add(rs.getInt(1));
					}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL, "L'exécution de la requête SELECT_USER_SELLS a échoué.");
				// System.out.println("L'exécution de la requête SELECT_USER_SELLS de retrieveAllUserCurrentlyOnSaleItems a échoué !");

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

		return listeNoArticles;

	}

	public void retrieveAndDeleteAllUserSells(Integer noUtilisateur, Connection cnx) throws ModelException {

		try {

			try {

				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_USER_SELLS);

				/* valorisation du paramètre */
				query.setInt(1, noUtilisateur);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					delete(rs.getInt(1), cnx);
				}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL, "L'exécution de la requête SELECT_USER_SELLS a échoué.");
				// System.out.println("L'exécution d'une des requêtes SELECT_USER_SELLS de retrieveAndDeleteAllUserSells a échoué !");

				throw modelDalException;

			} finally {
				// ConnectionProvider.closeConnection(cnx);
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
	
	/* -------------------------------------------------------------------- */
	/* -------- Méthodes de sélection qui retournent une donnée --------- */
	/* -------------------------------------------------------------------- */

	/**
	 * récupère et retourne un article grâce à sa primary key
	 */
	@Override
	public Article selectById(Integer primaryKey) throws ModelException {

		Article article = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ARTICLE_BY_ID);

				/* valorisation du paramètre */
				query.setInt(1, primaryKey);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					article = articleBuilder(rs);

					/* on récupère la dernière enchére effectuée sur l'article */
					if (article.getNoArticle() != null) {
						DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
						Enchere enchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(article.getNoArticle());

						/* s'il y avait bien eu une enchère */
						if (enchere != null) {
							article.setDernierEncherisseur(enchere.getEncherisseur());
						}
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_ARTICLE_BY_ID, "L'exécution de la requête SELECT_ARTICLE_BY_ID a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ARTICLE_BY_ID a échoué !");

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

		return article;
	}

	/**
	 * récupère et retourne le datetime d'ouverture des enchères d'un article
	 */
	@Override
	public LocalDateTime retrieveBidStartingDateTime(Article article) throws ModelException {

		LocalDateTime dateTime = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_BID_STARTING_DATETIME);

				/* valorisation du paramètre */
				query.setInt(1, article.getNoArticle());

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					dateTime = rs.getTimestamp(1).toLocalDateTime();
				}

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_BID_STARTING_DATETIME,
					"L'exécution de la requête SELECT_BID_STARTING_DATETIME a échoué.");
				// System.out.println("L'exécution de la requête SELECT_BID_STARTING_DATETIME a échoué !");

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

		return dateTime;

	}

	/**
	 * récupère et retourne le datetime d'ouverture des enchères d'un article
	 */
	public LocalDateTime retrieveBidClosingDateTime(Article article) throws ModelException {

		LocalDateTime dateTime = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_BID_CLOSING_DATETIME);

				/* valorisation du paramètre */
				query.setInt(1, article.getNoArticle());

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					dateTime = rs.getTimestamp(1).toLocalDateTime();
				}

			} catch (SQLException e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_BID_STARTING_DATETIME,
					"L'exécution de la requête SELECT_BID_STARTING_DATETIME a échoué.");
				// System.out.println("L'exécution de la requête SELECT_BID_STARTING_DATETIME a échoué !");

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

		return dateTime;

	}

	/**
	 * récupère et retourne le prix de vente initial d'un article identifié par son
	 * numéro
	 * 
	 * @throws ModelException
	 */
	@Override
	public Integer retrieveItemStartingPrice(Integer noArticle) throws ModelException {

		Integer prixInitial = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ITEM_STARTING_PRICE);

				/* valorisation du paramètre */
				query.setInt(1, noArticle);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					prixInitial = rs.getInt(1);
				}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_INITIAL_PRICE, "L'exécution de la requête SELECT_ITEM_STARTING_PRICE a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ITEM_STARTING_PRICE a échoué !");

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

		return prixInitial;
	}

	/**
	 * récupère et retourne le prix de vente actuel d'un article identifié par son
	 * numéro
	 */
	@Override
	public Integer retrieveItemCurrentPrice(Integer noArticle) throws ModelException {

		Integer prixActuel = null;

		try {
			/* ouverture d'une connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {

				/* préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ITEM_CURRENT_PRICE);

				/* valorisation du paramètre */
				query.setInt(1, noArticle);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					prixActuel = rs.getInt(1);
				}

			} catch (Exception e) {
				e.printStackTrace();

				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_CURRENT_PRICE, "L'exécution de la requête SELECT_ITEM_CURRENT_PRICE a échoué.");
				// System.out.println("L'exécution de la requête SELECT_ITEM_CURRENT_PRICE a échoué !");

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

		return prixActuel;
	}

	/* ----------- BUILDER ------------ */

	/**
	 * construit un objet de type Article à partir du jeu d'enregistrements remonté
	 * par la requête SQL pour utiliser cette méthode, il faut impérativement que la
	 * requête SQL projette les noms de colonnes suivants dans cet ordre précis :
	 * no_article, nom_article, description, date_debut_encheres, date_fin_encheres,
	 * prix_initial, prix_vente, no_utilisateur, pseudo, no_categorie, libelle, rue,
	 * code_postal et ville
	 * 
	 * @param rs
	 * @return article
	 * @throws ModelException
	 */
	private Article articleBuilder(ResultSet rs) throws ModelException {

		Article article = new Article();

		try {
			article.setNoArticle(rs.getInt(1));
			article.setNomArticle(rs.getString(2));
			article.setDescription(rs.getString(3));
			// article.setDateDebutEncheres(rs.getDate(4).toLocalDate());
			// article.setDateFinEncheres(rs.getDate(5).toLocalDate());

			article.setDateDebutEncheres(rs.getTimestamp(4).toLocalDateTime().toLocalDate());
			article.setHeureDebutEncheres(rs.getTimestamp(4).toLocalDateTime().toLocalTime());
			article.setDateFinEncheres(rs.getTimestamp(5).toLocalDateTime().toLocalDate());
			article.setHeureFinEncheres(rs.getTimestamp(5).toLocalDateTime().toLocalTime());

			article.setPrixInitial(rs.getInt(6));
			article.setPrixVente(rs.getInt(7));

			Utilisateur vendeur = new Utilisateur();
			vendeur.setNoUtilisateur(rs.getInt(8));
			vendeur.setPseudo(rs.getString(9));

			article.setVendeur(vendeur);

			Categorie categorie = new Categorie();
			categorie.setNoCategorie(rs.getInt(10));
			categorie.setLibelle(rs.getString(11));

			article.setCategorie(categorie);

			Adresse adresseRetrait = new Adresse();
			adresseRetrait.setRue(rs.getString(12));
			adresseRetrait.setCodePostal(rs.getInt(13));
			adresseRetrait.setVille(rs.getString(14));

			article.setAdresseRetrait(adresseRetrait);

			// System.out.println("\nTEST DAO ARTICLE ArticleBuilder // Article : " + article);

		} catch (Exception e) {
			e.printStackTrace();

			modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "Une erreur s'est produite dans la méthode articleBuilder.");
			// System.out.println("Une erreur s'est produite dans la méthode articleBuilder");

			throw modelDalException;
		}

		return article;
	}

}
