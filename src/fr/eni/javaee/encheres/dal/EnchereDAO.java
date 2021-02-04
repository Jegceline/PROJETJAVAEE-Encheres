package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;

public interface EnchereDAO extends DAO<Enchere>{
	
	public Enchere retrieveItemLastBid(Integer noArticle) throws ModelException;

}
