package fr.eni.javaee.encheres.bll;

import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.dal.CategorieDAO;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;

public class CategorieManager {
	
	/* Attributs */
	private DAO<Categorie> categorieDAO = DAOFactory.getCategorieDAO();
	
	
	/* Constructeur */
	public CategorieManager() {
	}

	/**
	 * récupère les catégories de la base de données
	 * @return
	 * @throws ModelException
	 */
	public List<Categorie> recupereCategories() throws ModelException {
		
		List<Categorie> listeCategories = new ArrayList<Categorie>();
		
		try {
			listeCategories = ((CategorieDAO) categorieDAO).retrieveAllCategories();
			
		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		
		return listeCategories;
	}

	/* ajoute une catégorie */
	public void ajouteCategorie(Categorie categorie) throws ModelException {
		try {
			categorieDAO.insert(categorie);
			
		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}
