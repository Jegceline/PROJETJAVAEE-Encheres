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
import fr.eni.javaee.encheres.bo.Article;


public class ArticleDAOJdbcImpl implements ArticleDAO {
	
	private ModelException modelDalException = new ModelException();
	
	
	private static final String INSERT_ARTICLE = "INSERT INTO Articles_Vendus(nom_article, description, date_debut_encheres, " + 
	"date_fin_encheres, prix_initial, no_utilisateur, no_categorie) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_ADRESSE_RETRAIT = "INSERT INTO Retraits (no_article, rue, code_postal, ville) VALUES (?, ?, ?, ?)";
	private static final String SELECT_ARTICLE_ENCHERES_EC = "SELECT * FROM Articles_vendus WHERE (date_debut_encheres <= GETDATE() AND date_fin_encheres > GETDATE())";
	
	/* Constructeur */
	
	public ArticleDAOJdbcImpl() {
	}

	@Override
	public void insert(Article article) throws ModelException {
		
		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la première requête
				PreparedStatement query = cnx.prepareStatement(INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS);

				// valorisation des paramètres
				query.setString(1, article.getNomArticle());
				query.setString(2, article.getDescription());
				query.setDate(3, Date.valueOf(article.getDateDebutEncheres()));
				query.setDate(4, Date.valueOf(article.getDateFinEncheres()));
				query.setInt(5, article.getPrixInitial());
				query.setInt(6, article.getNoUtilisateur());
				query.setInt(7, article.getNoCategorie());

				// exécution de la requête
				query.executeUpdate();

				// récupération de la clé primaire/identity générée par la BDD
				ResultSet rs = query.getGeneratedKeys();
				
				if (rs.next()) {
					// valorisation de l'attribut Id de l'objet article avec cette clé
					article.setNoArticle(rs.getInt(1));
				}
				
				// Préparation de la seconde requête
				PreparedStatement query2 = cnx.prepareStatement(INSERT_ADRESSE_RETRAIT, Statement.RETURN_GENERATED_KEYS);
				
				// valorisation des paramètres
				query2.setInt(1, article.getNoArticle());
				query2.setString(2, article.getAdresseRetrait().getRue());
				query2.setInt(3, article.getAdresseRetrait().getCodePostal());
				query2.setString(4, article.getAdresseRetrait().getVille());
				
				// exécution de la requête
				query2.executeUpdate();
			

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
	public List<Article> selectByStatus() throws ModelException {
		List<Article> listeEncheresEnCours = new ArrayList<Article>();
		
		try {
			// obtention d'un connexion
			Connection cnx = ConnectionProvider.getConnection();

			try {
				// Préparation de la première requête
				PreparedStatement query = cnx.prepareStatement(SELECT_ARTICLE_ENCHERES_EC);
				
				// exécution de la requête
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					
					Article article = articleBuilder(rs);
					
					listeEncheresEnCours.add(article);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				modelDalException.ajouterErreur(CodesErreurs.ERREUR_INSERTION_SQL, "L'exécution de la requête SELECT a échoué.");
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
		
		// System.out.println("\nTEST DAO // Liste des enchères en cours : " + listeEncheresEnCours);
		return listeEncheresEnCours;
		
	}
	
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
			
			System.out.println("\n TEST DAO : Article actuellement en vente : " + article);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return article;
	}

	@Override
	public void delete(Integer chiffre) throws ModelException {
		
	}

	@Override
	public void update(Article objet) throws ModelException {
		
	}



}
