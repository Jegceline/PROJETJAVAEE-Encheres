package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

public abstract class DAOFactory {

	/* Constructeur */
	
	DAOFactory() {
	}

	/* Méthodes */
	
	public static DAO<Utilisateur> getUtilisateurDAO() {
		return new UtilisateurDAOJdbcImpl();
	}

	public static DAO<Article> getArticleDAO() {
		return new ArticleDAOJdbcImpl();
	}
	
	public static DAO<Enchere> getEnchereDAO(){
		return new EnchereDAOJdbcImpl();
	}
	
	public static DAO<Categorie> getCategorieDAO(){
		return new CategorieDAOJdbcImpl();
	}

}
