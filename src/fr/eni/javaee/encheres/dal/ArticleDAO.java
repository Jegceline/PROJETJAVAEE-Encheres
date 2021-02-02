package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Trieur;

public interface ArticleDAO extends DAO<Article>{
	
	public List<Article> retrieveCurrentlyForSaleArticlesWithFilter(Trieur trieur) throws ModelException;

	public List<Article> selectByCriteria(Trieur trieur) throws ModelException;

	public Integer selectInitialPrice(Integer noArticle) throws ModelException;

	public Integer selectCurrentPrice(Integer noArticle) throws ModelException;

	public List<Article> retrieveUserWishlistWithFilter(Trieur trieur) throws ModelException;

	public List<Article> retrieveUserSellsWithFilter(Trieur trieur) throws ModelException;

	public List<Article> retrieveCurrentlyForSaleArticles() throws ModelException;
}
