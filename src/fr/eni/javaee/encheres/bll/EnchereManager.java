package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.EnchereDAO;

public class EnchereManager {
	
	private DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();
	
	/**
	 * ajoute une enchère
	 * si ce n'est pas la première enchère sur l'article, récupère et retourne l'enchère précédente
	 * @param enchere
	 * @param precedenteEnchere
	 * @param premiereEnchere
	 * @return precedenteEnchere
	 * @throws ModelException
	 */
	public Enchere ajouteEnchere(Enchere enchere, Enchere precedenteEnchere, boolean premiereEnchere) throws ModelException {

		/* Si l'objet n'avait jamais fait l'objet d'une enchère */
		if (premiereEnchere) {
//			System.out.println("\nTEST Manager Enchere // Il s'agit d'une première enchère sur cet article.");

			try {
				/* créer l'enchère dans la base de données */
				enchereDAO.insert(enchere);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}

			/* s'il y a déjà eu des enchères sur l'article */
		} else {

			try {
				/* récupérer le numéro du dernier enchérisseur */
				precedenteEnchere = ((EnchereDAO) enchereDAO).retrieveItemLastBid(enchere.getArticle().getNoArticle());

				/* créer l'enchère dans la base de données */
				enchereDAO.insert(enchere);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}

		}
		return precedenteEnchere;
	}

}
