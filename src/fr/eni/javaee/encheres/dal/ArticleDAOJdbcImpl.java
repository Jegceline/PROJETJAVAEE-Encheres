package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Adresse;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Trieur;

public class ArticleDAOJdbcImpl implements ArticleDAO {

	private ModelException modelDalException = new ModelException();

	/* Constantes */

	private static final String INSERT_ARTICLE = "INSERT INTO Articles_Vendus(nom_article, description, date_debut_encheres, "
			+ "date_fin_encheres, prix_initial, no_utilisateur, no_categorie) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_PICKUP_ADDRESS = "INSERT INTO Retraits (no_article, rue, code_postal, ville) VALUES (?, ?, ?, ?)";

	private static final String SELECT_CURRENTLY_ON_SALE_ARTICLES = "SELECT * FROM Articles_vendus a "
			+ "INNER JOIN Retraits r ON a.no_article = r.no_article "
			+ "WHERE (date_debut_encheres <= GETDATE() AND date_fin_encheres > GETDATE())";

	private static final String SELECT_USER = "SELECT pseudo FROM Utilisateurs WHERE no_utilisateur = ?";
	private static final String SELECT_ARTICLE_BY_ID = "SELECT * FROM Articles_vendus a INNER JOIN RETRAITS r ON a.no_article = r.no_article WHERE a.no_article = ?";
	private static final String SELECT_ARTICLE_INITIAL_PRICE = "SELECT prix_initial FROM Articles_vendus WHERE no_article = ?";
	private static final String SELECT_ARTICLE_CURRENT_PRICE = "SELECT prix_vente FROM Articles_vendus WHERE no_article = ?";
	private static final String SELECT_ARTICLE_PICKUP_ADDRESS = "SELECT rue, code_postal, ville FROM Retraits WHERE no_article = ?";

	private static final String SELECT_USER_SELLS = "SELECT * FROM ARTICLES_VENDUS a "
			+ "INNER JOIN Retraits r ON a.no_article = r.no_article " + "WHERE no_utilisateur = ?";
	private static final String SELECT_ARTICLES_USER_HAVE_BIDS_ON = "SELECT DISTINCT a.no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, a.no_article, r.rue, r.code_postal, r.ville "
			+ "FROM ENCHERES e " + "INNER JOIN ARTICLES_VENDUS a on a.no_article = e.no_article "
			+ "INNER JOIN Retraits r ON a.no_article = r.no_article " + "WHERE e.no_utilisateur = ?";

	/* Constructeur */

	public ArticleDAOJdbcImpl() {
	}

	/* ----------- Méthodes CRUD ------------ */

	@Override
	public void insert(Article article) throws ModelException {

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS);

				// valorisation des paramètres
				query.setString(1, article.getNomArticle());
				query.setString(2, article.getDescription());
				query.setDate(3, Date.valueOf(article.getDateDebutEncheres()));
				query.setDate(4, Date.valueOf(article.getDateFinEncheres()));
				query.setInt(5, article.getPrixInitial());
				query.setInt(6, article.getNoUtilisateur());
				query.setInt(7, article.getNoCategorie());

				/* exécution de la requête */
				query.executeUpdate();

				/* récupération de la clé primaire/identity générée par la BDD */
				ResultSet rs = query.getGeneratedKeys();

				if (rs.next()) {
					/* valorisation de l'attribut Id de l'objet article avec cette clé */
					article.setNoArticle(rs.getInt(1));
				}

				/* Préparation de la seconde requête */
				PreparedStatement query2 = cnx.prepareStatement(INSERT_PICKUP_ADDRESS, Statement.RETURN_GENERATED_KEYS);

				/* valorisation des paramètres */
				query2.setInt(1, article.getNoArticle());
				query2.setString(2, article.getAdresseRetrait().getRue());
				query2.setInt(3, article.getAdresseRetrait().getCodePostal());
				query2.setString(4, article.getAdresseRetrait().getVille());

				/* exécution de la requête */
				query2.executeUpdate();

				cnx.commit();

			} catch (SQLException e) {
				e.printStackTrace();

				cnx.rollback();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête INSERT_ARTICLE a échoué.");
				System.out.println("L'exécution de la requête INSERT_ARTICLE a échoué !");
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
	public void delete(Integer chiffre) throws ModelException {

	}

	@Override
	public void update(Article objet) throws ModelException {

	}

	/* --------------- SELECT --------------- */
	

	/**
	 * récupère et retourne les articles dont la vente est ouverte
	 */
	@Override
	public List<Article> retrieveCurrentlyForSaleArticles() throws ModelException {
		
		List<Article> listeEncheresEnCours = new ArrayList<Article>();

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_CURRENTLY_ON_SALE_ARTICLES);
				
				/* Exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					
					Article article = articleBuilder(rs);

					ajoutePseudoVendeur(cnx, article);
					ajouteAdresseRetrait(cnx, article);

					listeEncheresEnCours.add(article);
				}
				
				 // System.out.println("\nTEST DAO // Liste des enchères en cours : " + listeEncheresEnCours);

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL,
						"L'exécution d'une des requêtes SELECT_CURRENTLY_ON_SALE_ARTICLES a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_CURRENTLY_ON_SALE_ARTICLES a échoué !");
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

		return listeEncheresEnCours;

	}
	
	/**
	 * récupère et retourne les articles dont la vente est ouverte avec filtre
	 */
	@Override
	public List<Article> retrieveCurrentlyForSaleArticlesWithFilter(Trieur trieur) throws ModelException {

		List<Article> listeEncheresEnCours = new ArrayList<Article>();
		String select_articles_filtres = SELECT_CURRENTLY_ON_SALE_ARTICLES;

		if (trieur.getCategorie() != 0) {
			select_articles_filtres += " AND (no_categorie = ?)";
			// System.out.println("\nTEST DAO // Requête SQL avec catégorie : " + select_articles_filtres);
		}

		if (!trieur.getKeyword().isEmpty()) {
			select_articles_filtres += " AND (no_categorie = ?)";
			// System.out.println("\nTEST DAO // Requête SQL avec catégorie : " + select_articles_filtres);
		}

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de la première requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* Valorisation des paramètres */

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
				
				/* Exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {

					Article article = articleBuilder(rs);

					ajoutePseudoVendeur(cnx, article);
					ajouteAdresseRetrait(cnx, article);

					listeEncheresEnCours.add(article);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL,
						"L'exécution d'une des requêtes SELECT_CURRENTLY_ON_SALE_ARTICLES a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_CURRENTLY_ON_SALE_ARTICLES a échoué !");
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

		// System.out.println("\nTEST DAO // Liste des enchères en cours : " +
		// listeEncheresEnCours);
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
			/* Obtention d'un connexion */
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

				/* Préparation de requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation du paramètre */
				query.setInt(1, trieur.getUtilisateur().getNoUtilisateur());

				/* Exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {

					Article article = articleBuilder(rs);

					ajoutePseudoVendeur(cnx, article);
					ajouteAdresseRetrait(cnx, article);

					listeArticlesFiltres.add(article);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL,
						"L'exécution de la requête SELECT_ARTICLES_CRITERIA_USER_FILTER a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_ARTICLES_CRITERIA_USER_FILTER a échoué !");
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

		return listeArticlesFiltres;
	}

	/**
	 * récupère et retourne les articles sur lequels l'utilisateur a effectué au
	 * moins une enchère
	 */
	@Override
	public List<Article> retrieveUserWishlistWithFilter(Trieur trieur) throws ModelException {

		List<Article> listeArticlesFiltres = new ArrayList<Article>();

		String select_articles_filtres = SELECT_ARTICLES_USER_HAVE_BIDS_ON;

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {

				if (trieur.getEncheresUtilisateurEC() != null) {
					select_articles_filtres += " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres > GETDATE())";
				}

				if (trieur.getEncheresUtilisateurG() != null) {
					select_articles_filtres += " AND a.date_fin_encheres < getdate()";
				}

				/* Préparation de requête */
				PreparedStatement query = cnx.prepareStatement(select_articles_filtres);

				/* valorisation du paramètre */
				query.setInt(1, trieur.getUtilisateur().getNoUtilisateur());

				/* Exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {

					Article article = articleBuilder(rs);

					ajoutePseudoVendeur(cnx, article);
					ajouteAdresseRetrait(cnx, article);

					listeArticlesFiltres.add(article);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL,
						"L'exécution de la requête SELECT_ARTICLES_CRITERIA_USER_FILTER a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_ARTICLES_CRITERIA_USER_FILTER a échoué !");
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

		// System.out.println("\nTEST DAO // Liste des enchères en cours : " +
		// listeEncheresEnCours);

		return listeArticlesFiltres;
	}

	/**
	 * récupère et retourne tous les articles répondant à certains critères
	 */
	@Override
	public List<Article> selectByCriteria(Trieur trieur) throws ModelException {
		List<Article> listeArticlesFiltres = new ArrayList<Article>();

		String select_articles_filtres = SELECT_CURRENTLY_ON_SALE_ARTICLES;

		try {
			/* Obtention d'un connexion */
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

				/* Préparation de requête */
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

				/* Exécution de la requête */
				ResultSet rs = query.executeQuery();

				while (rs.next()) {

					Article article = articleBuilder(rs);

					ajoutePseudoVendeur(cnx, article);
					ajouteAdresseRetrait(cnx, article);

					listeArticlesFiltres.add(article);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECTION_SQL,
						"L'exécution de la requête SELECT_ARTICLE_ENCHERES_EC a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_ARTICLE_ENCHERES_EC a échoué !");
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

		// System.out.println("\nTEST DAO // Liste des enchères en cours : " +
		// listeEncheresEnCours);

		return listeArticlesFiltres;
	}

	/**
	 * récupère et retourne un article
	 */
	@Override
	public Article selectById(Integer number) throws ModelException {

		Article article = null;

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ARTICLE_BY_ID);

				/* Valorisation du paramètre */
				query.setInt(1, number);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					article = articleBuilder(rs);
				}

				ajoutePseudoVendeur(cnx, article);
				ajouteAdresseRetrait(cnx, article);

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL,
						"L'exécution d'une des requêtes SELECT_ARTICLE_BYID a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_ARTICLE_BYID a échoué !");
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

		return article;
	}

	/**
	 * récupère et retourne le prix de vente initial d'un article
	 * 
	 * @throws ModelException
	 */
	@Override
	public Integer selectInitialPrice(Integer noArticle) throws ModelException {

		Integer prixInitial = null;

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {
				/* Préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ARTICLE_INITIAL_PRICE);

				/* Valorisation du paramètre */
				query.setInt(1, noArticle);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					prixInitial = rs.getInt(1);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_INITIAL_PRICE,
						"L'exécution d'une de la requête SELECT_INITIAL_PRICE a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_INITIAL_PRICE a échoué !");
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

		return prixInitial;
	}

	/**
	 * récupère et retourne la dernière enchère émise d'un article
	 */
	@Override
	public Integer selectCurrentPrice(Integer noArticle) throws ModelException {

		Integer prixActuel = null;

		try {
			/* Obtention d'un connexion */
			Connection cnx = ConnectionProvider.getConnection();

			try {

				/* Préparation de requête */
				PreparedStatement query = cnx.prepareStatement(SELECT_ARTICLE_CURRENT_PRICE);

				/* Valorisation du paramètre */
				query.setInt(1, noArticle);

				/* exécution de la requête */
				ResultSet rs = query.executeQuery();

				if (rs.next()) {
					prixActuel = rs.getInt(1);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_SELECT_CURRENT_PRICE,
						"L'exécution d'une de la requête SELECT_CURRENT_PRICE a échoué.");
				System.out.println("L'exécution d'une des requêtes SELECT_CURRENT_PRICE a échoué !");
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

		return prixActuel;
	}

	
	
	/* -------------- BUILDERS -------------- */

	/**
	 * construit un objet de type Article et le retourne à la méthode appelante
	 * 
	 * @param rs
	 * @return article
	 */
	private Article articleBuilder(ResultSet rs) {
		Article article = new Article();

		try {
			article.setNoArticle(rs.getInt(1));
			article.setNomArticle(rs.getString(2));
			article.setDescription(rs.getString(3));
			article.setDateDebutEncheres(rs.getDate(4).toLocalDate());
			article.setDateFinEncheres(rs.getDate(5).toLocalDate());
			article.setPrixInitial(rs.getInt(6));
			article.setPrixVente(rs.getInt(7));
			article.setNoUtilisateur(rs.getInt(8));
			article.setNoCategorie(rs.getInt(9));

			Adresse adresseRetrait = new Adresse();
			adresseRetrait.setRue(rs.getString(11));
			adresseRetrait.setCodePostal(rs.getInt(12));
			adresseRetrait.setVille(rs.getString(13));

			article.setAdresseRetrait(adresseRetrait);

			// System.out.println("\nTEST DAO : Article actuellement en vente : " + article);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return article;
	}

	/**
	 * récupère le pseudo du vendeur et le place dans l'objet article
	 * 
	 * @param cnx
	 * @param article
	 * @throws SQLException
	 */
	private void ajoutePseudoVendeur(Connection cnx, Article article) throws SQLException {

		/* Préparation de la requête */
		PreparedStatement query = cnx.prepareStatement(SELECT_USER);

		/* Valorisation du paramètre */
		query.setInt(1, article.getNoUtilisateur());

		/* Exécution de la requête */
		ResultSet rs = query.executeQuery();

		if (rs.next()) {
			article.setPseudoVendeur(rs.getString(1));
			// System.out.println("\n DEBUG DAO // Pseudo du vendeur = " +
			// article.getPseudoVendeur());
		}
	}

	/**
	 * récupère l'adresse de retrait d'un article et le place dans l'objet article
	 * 
	 * @param cnx
	 * @param article
	 * @throws SQLException
	 */
	private void ajouteAdresseRetrait(Connection cnx, Article article) throws SQLException {

		Adresse adresseRetrait = new Adresse();

		/* Préparation de la requête */
		PreparedStatement query = cnx.prepareStatement(SELECT_ARTICLE_PICKUP_ADDRESS);

		/* Valorisation du paramètre */
		query.setInt(1, article.getNoArticle());

		/* Exécution de la requête */
		ResultSet rs = query.executeQuery();

		if (rs.next()) {
			adresseRetrait.setRue(rs.getString(1));
			adresseRetrait.setCodePostal(rs.getInt(2));
			adresseRetrait.setVille(rs.getString(3));
		}

		// System.out.println("\nTEST DAO // Adresse de retrait = " + adresseRetrait);
		article.setAdresseRetrait(adresseRetrait);
	}

}
