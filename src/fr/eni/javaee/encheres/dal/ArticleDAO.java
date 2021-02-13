package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Trieur;

public interface ArticleDAO extends DAO<Article>{
	
	public List<Article> retrieveCurrentlyForSaleItemsWithFilter(Trieur trieur) throws ModelException;

	public List<Article> retrieveCurrentlyForSaleItemsAndSalesToComeWithFilter(Trieur trieur) throws ModelException;
	
	public List<Article> retrieveItemsByCriteria(Trieur trieur) throws ModelException;

	public Integer retrieveItemStartingPrice(Integer noArticle) throws ModelException;
	
	public LocalDateTime retrieveBidClosingDateTime(Article article) throws ModelException;

	public Integer retrieveItemCurrentPrice(Integer noArticle) throws ModelException;

	public List<Article> retrieveUserWishlistWithFilter(Trieur trieur) throws ModelException;

	public List<Article> retrieveUserSellsWithFilter(Trieur trieur) throws ModelException;
	
	public List<Article> retrieveSellerYetNotPaidItems(Integer noVendeur) throws ModelException;

	public List<Article> retrieveCurrentlyForSaleItemsGet() throws ModelException;
	
	// public List<Article> retrieveCurrentlyForSaleItemsPost(Trieur trieur) throws ModelException;

	public List<Article> retrieveUserPurchasedItems(Trieur trieur) throws ModelException;

	public LocalDateTime retrieveBidStartingDateTime(Article article) throws ModelException;

	public void updateItemPaymentStatus(Integer noArticle) throws ModelException;
	
	public List<Integer> retrieveAllUserCurrentlyOnSaleItems(Integer noUtilisateur) throws ModelException;
	
	public void retrieveAndDeleteAllUserSells(Integer noUtilisateur, Connection cnx) throws ModelException;
}
