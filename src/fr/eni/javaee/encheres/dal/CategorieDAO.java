package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Categorie;

public interface CategorieDAO extends DAO<Categorie>{
	
	public List<Categorie> retrieveAllCategories() throws ModelException;

}
