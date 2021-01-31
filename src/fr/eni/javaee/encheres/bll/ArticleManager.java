package fr.eni.javaee.encheres.bll;

import java.time.LocalDate;
import java.util.List;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.dal.ArticleDAO;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.EnchereDAO;

public class ArticleManager {

	private ModelException modelBllException = new ModelException();
	private DAO<Article> articleDAO = DAOFactory.getArticleDAO();
	private DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();

	public ArticleManager() {
	}

	public Article ajouteArticle(Article article) throws ModelException {

		/* vérifier que les contraintes imposées par la database sont respectées */

		valideNomArticle(article.getNomArticle());
		valideDescriptionArticle(article.getDescription());
		valideDatesEncheres(article.getDateDebutEncheres(), article.getDateFinEncheres());
		valideCategorie(article.getNoCategorie());
		validePrixInitial(article.getPrixInitial());

		/* si les étapes de valisation ont été passées avec succès, appeler le DAO */

		if (!modelBllException.contientErreurs()) {
			try {
				articleDAO.insert(article);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}

		} else { // si une des étapes de validation a échoué

			article = null;
			throw modelBllException;
		}

		return article;

	}

	private void validePrixInitial(Integer prixInitial) {
		if (prixInitial < 0) {
			modelBllException.ajouterErreur(CodesErreurs.ARTICLE_PRIX_INVALIDE, "Le prix ne peut pas être négatif.");
		}
	}

	private void valideCategorie(Integer noCategorie) {
		if (noCategorie == 0) {
			modelBllException.ajouterErreur(CodesErreurs.ARTICLE_CATEGORIE_INEXISTANTE, "Veuillez sélectionner une catégorie.");
		}

	}

	private void valideDatesEncheres(LocalDate dateDebutEncheres, LocalDate dateFinEncheres) {

		if (dateDebutEncheres.isBefore(LocalDate.now())) {
			modelBllException.ajouterErreur(CodesErreurs.ARTICLE_DATE_DEBUT_ENCHERES_INVALIDE,
					"La date d'ouverture des enchères ne peut être antérieure à la date du jour.");
		}

		if (dateFinEncheres.isBefore(dateFinEncheres)) {
			modelBllException.ajouterErreur(CodesErreurs.ARTICLE_DATE_FIN_ENCHERES_INVALIDE,
					"La date de fin des enchères ne peut être antérieure à la date d'ouverture des enchères.");
		}

		if (dateFinEncheres.isBefore(LocalDate.now())) {
			modelBllException.ajouterErreur(CodesErreurs.ARTICLE_DATE_FIN_ENCHERES_INVALIDE,
					"La date de fin des enchères ne peut être antérieure à la date du jour");
		}

	}

	private void valideNomArticle(String nomArticle) {

		if (nomArticle.trim().length() > 30) {
			modelBllException.ajouterErreur(CodesErreurs.ARTICLE_NOM_LONGUEUR, "Le nom de l'article est trop long.");
		}
	}

	private void valideDescriptionArticle(String description) {
		if (description.trim().length() > 300) {
			modelBllException.ajouterErreur(CodesErreurs.ARTICLE_DESCRIPTION_LONGUEUR, "La description est trop longue.");
		}
	}

	public List<Article> recupereEncheresEnCours() throws ModelException {

		List<Article> listesEncheresEC = null;

		try {
			listesEncheresEC = ((ArticleDAO) articleDAO).selectByStatus();

		} catch (ModelException e) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_CHARGEMENT_ENCHERES_EC,
					"Une erreur est survenue lors du chargement des enchères en cours.");
			e.printStackTrace();
			throw e;
		}

		return listesEncheresEC;

	}

	public List<Article> trieEtRecupereArticles(Integer noCategorie, String keyword) throws ModelException {
		List<Article> listeSelectionArticles = null;

		try {
			listeSelectionArticles = ((ArticleDAO) articleDAO).selectByCriteria(noCategorie, keyword);

		} catch (ModelException e) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_CHARGEMENT_TRI_ARTICLES,
					"Une erreur est survenue lors du chargement de la sélection par critères");
			e.printStackTrace();
			throw e;
		}

		return listeSelectionArticles;

	}

	public Article recupereArticle(Integer noArticle) {
		Article article = null;
		try {
			article = articleDAO.selectById(noArticle);
		} catch (ModelException e) {
			e.printStackTrace();

		}
		return article;

	}

	public Enchere recoitEnchere(Enchere enchere) throws ModelException {

		Boolean premiereEnchere;
		Enchere precedenteEnchere = null;
		UtilisateurManager utilisateurManager = new UtilisateurManager();

		try {

			/* vérifier que l'enchère est bien supérieure à la précédente enchère */
			/* déterminer si c'est la première enchère proposée pour l'article */
			premiereEnchere = verifieMontantEnchere(enchere.getMontant(), enchere.getNoArticle());

		} catch (ModelException e2) {
			e2.printStackTrace();
			throw e2;
		}

		/* si l'enchère est bien supérieure à la précédente enchère */
		if (!modelBllException.contientErreurs()) {

			try {
				/* vérifier que l'utilisateur a bien un crédit suffisant */
				utilisateurManager.verifieSoldeCredits(enchere.getNoUtilisateur(), enchere.getMontant());

			} catch (ModelException e1) {
				e1.printStackTrace();
				throw e1;
			}
		}
		
		/* si l'enchère est bien supérieure à l'énchère précédente et si l'utilisateur a un crédit suffisant */
		if (!modelBllException.contientErreurs()) {

			/* Si l'objet n'avait jamais fait l'objet d'une enchère */
			if (premiereEnchere) {
				System.out.println("\nTEST Manager // Il s'agit d'une première enchère sur cet article.");

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
					precedenteEnchere = ((EnchereDAO) enchereDAO).returnLastBid(enchere.getNoArticle());
					
					/* créer l'enchère dans la base de données */
					enchereDAO.insert(enchere);

				} catch (ModelException e) {
					e.printStackTrace();
					throw e;
				}

			}
		}
		
		return precedenteEnchere;
	}

	/*
	 * vérifie que l'enchère est supérieure au prix de vente actuel 
	 * détermine si c'est la première enchère effectuée sur l'objet
	 * 
	 * @param noArticle
	 * 
	 * @throws ModelException
	 */
	private boolean verifieMontantEnchere(Integer montantEnchere, Integer noArticle) throws ModelException {

		Integer prixInitial = null;
		Integer prixActuel = null;
		Boolean premiereEnchere = false;

		try {
			prixInitial = ((ArticleDAO) articleDAO).selectInitialPrice(noArticle);
			// System.out.println("\nTEST MANAGER ARTICLE // Prix initial de l'objet : " + prixInitial);

			prixActuel = ((ArticleDAO) articleDAO).selectCurrentPrice(noArticle);
			// System.out.println("\nTEST MANAGER ARTICLE // Prix actuel de l'objet : " + prixActuel);

			if (prixActuel == null || prixActuel == 0) {
				premiereEnchere = true;
			}

			if (premiereEnchere) {

				if (montantEnchere <= prixInitial) {
					modelBllException.ajouterErreur(CodesErreurs.ERREUR_MONTANT_ENCHERE,
							"Votre enchère doit être supérieure au prix de vente initial !");
					System.out.println("\n TEST MANAGER // Le montant de l'enchère est invalide.");
					throw modelBllException;
				}

			} else {

				if (montantEnchere <= prixActuel) {
					modelBllException.ajouterErreur(CodesErreurs.ERREUR_MONTANT_ENCHERE,
							"Votre enchère doit être supérieure à la dernière enchère !");
					System.out.println("\n TEST MANAGER // Le montant de l'enchère est invalide.");
					throw modelBllException;
				}

			}

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

		return premiereEnchere;

	}

}
