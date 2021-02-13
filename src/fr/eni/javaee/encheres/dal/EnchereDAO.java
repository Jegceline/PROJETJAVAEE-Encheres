package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;

public interface EnchereDAO extends DAO<Enchere>{
	
	public Enchere retrieveItemLastBid(Integer noArticle) throws ModelException;
	
	public List<Integer> retrieveAllBidsUserHavePutOnCurrentlyOnSaleItems(Integer noUtilisateur) throws ModelException;
	
	public void retrieveAndDeleteAllUserBids(Integer noUtilisateur, Connection cnx) throws ModelException;
	
	public List<Enchere> retrieveAllItemBids(Integer noArticle) throws ModelException;

}
