package fr.eni.javaee.encheres.bll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Trieur;
import fr.eni.javaee.encheres.dal.ArticleDAO;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.EnchereDAO;

public class ArticleManagerV2 {

	/* Variables */

	private DAO<Article> articleDAO = DAOFactory.getArticleDAO();
	private DAO<Enchere> enchereDAO = DAOFactory.getEnchereDAO();

	/* Constructeur */

	public ArticleManagerV2() {
	}

	/* ----------------------------------------- */
	/* ------------- Méthodes CRUD ------------- */
	/* ----------------------------------------- */

	/**
	 * contrôle le respect des contraintes imposées par la BDD sur les champs de
	 * colonne, appelle le DAO si toutes les contraintes sont respectées, returne null
	 * si au moins une contrainte n'a pas été respectée
	 * 
	 * @param article
	 * @return article
	 * @throws ModelException
	 */
	public void ajouteArticle(Article article) throws ModelException {

		ModelException modelBllException = new ModelException();
		
		/* vérifier que les contraintes imposées par la database sont respectées */
		
		modelBllException = valideNomArticle(article.getNomArticle(), modelBllException);
		modelBllException = valideDescriptionArticle(article.getDescription(), modelBllException);
		modelBllException = valideDatesEncheres(article.getDateDebutEncheres(), article.getDateFinEncheres(), article.getHeureDebutEncheres(),
				article.getHeureFinEncheres(), modelBllException);
		modelBllException = valideCategorie(article.getCategorie().getNoCategorie(), modelBllException);
		modelBllException = validePrixInitial(article.getPrixInitial(), modelBllException);

		/* si tous les paramètres sont ok, appeler le DAO */

		if (!modelBllException.contientErreurs()) {
			try {
				articleDAO.insert(article);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}

		} else { /* si un des paramètres n'est pas ok */

			throw modelBllException;
		}

	}

	public void metAJourArticle(Article article) throws ModelException {

		ModelException modelBllException = new ModelException();
		
		/* vérifier que les contraintes imposées par la database sont respectées */
		
		modelBllException = valideNomArticle(article.getNomArticle(), modelBllException);
		modelBllException = valideDescriptionArticle(article.getDescription(), modelBllException);
		modelBllException = valideDatesEncheres(article.getDateDebutEncheres(), article.getDateFinEncheres(), article.getHeureDebutEncheres(),
				article.getHeureFinEncheres(), modelBllException);
		modelBllException = valideCategorie(article.getCategorie().getNoCategorie(), modelBllException);
		modelBllException = validePrixInitial(article.getPrixInitial(), modelBllException);

		/* si tous les paramètres sont ok */
		if (!modelBllException.contientErreurs()) {
			try {
				articleDAO.update(article);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}
		} else { /* si est des paramètres n'est pas ok */

			// article = null;
			throw modelBllException;
		}

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

	public void supprimerArticle(Integer noArticle) throws ModelException {

		try {
			articleDAO.delete(noArticle);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

	}

	/* ----------------------------------------- */
	/* ---------- Méthodes de contrôle --------- */
	/* ----------------------------------------- */

	/**
	 * vérifie que le prix est positif (inutile en indiquant un minimum requis avec
	 * du html)
	 * 
	 * @param prixInitial
	 */
	private ModelException validePrixInitial(Integer prixInitial, ModelException modelBllException) {
		if (prixInitial < 0) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_PRIX_INVALIDE, "Le prix ne peut pas être négatif.");
		}
		
		return modelBllException;
	}

	/*
	 * vérifie qu'une catégorie a bien été sélectionnée (valeur différente de zéro)
	 */
	private ModelException valideCategorie(Integer noCategorie, ModelException modelBllException) {
		if (noCategorie == 0) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_CATEGORIE_INEXISTANTE, "Veuillez sélectionner une catégorie.");
		}
		
		return modelBllException;
	}

	/**
	 * vérifie que les dates d'ouverture et de fin des enchères sont cohérentes
	 * 
	 * @param dateDebutEncheres
	 * @param dateFinEncheres
	 * @param heureDebutEncheres
	 * @param heureFinEncheres
	 */
	private ModelException valideDatesEncheres(LocalDate dateDebutEncheres, LocalDate dateFinEncheres, LocalTime heureDebutEncheres,
			LocalTime heureFinEncheres, ModelException modelBllException) {

		if (dateDebutEncheres.isBefore(LocalDate.now())) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_DATE_DEBUT_ENCHERES_INVALIDE,
					"La date d'ouverture des enchères ne peut être antérieure à la date du jour.");
		}

		if (dateFinEncheres.isBefore(dateDebutEncheres)) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_DATE_FIN_ENCHERES_INVALIDE,
					"La date de fin des enchères ne peut être antérieure à la date d'ouverture des enchères.");
		}

		if (dateFinEncheres.isBefore(LocalDate.now())) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_DATE_FIN_ENCHERES_INVALIDE,
					"La date de fin des enchères ne peut être antérieure à la date du jour.");
		}

		if (dateDebutEncheres.isEqual(LocalDate.now()) && heureDebutEncheres.isBefore(LocalTime.now())) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_HEURE_DEBUT_ENCHERES_INVALIDE,
					"L'heure de début des enchères ne peut pas être antérieure à l'heure actuelle.");
		}
		
		if (dateFinEncheres.isEqual(LocalDate.now()) && heureFinEncheres.isBefore(LocalTime.now())) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_HEURE_DEBUT_ENCHERES_INVALIDE,
					"L'heure de fin des enchères ne peut pas être antérieure à l'heure actuelle.");
		}
		
		return modelBllException;

	}

	/**
	 * vérifie que le nom de l'article ne comporte pas plus de 30 caractères
	 * (contrainte de la BDD)
	 * 
	 * @param nomArticle
	 */
	private ModelException valideNomArticle(String nomArticle, ModelException modelBllException) {

		if (nomArticle.length() > 30) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_NOM_LONGUEUR, "Le nom de l'article est trop long.");
		}
		return modelBllException;
	}

	/**
	 * vérifie que la description ne comporte pas plus de 300 caractères (contrainte
	 * de la BDD) (inutile en spécifiant un nombre max de caractères avec du html)
	 * 
	 * @param description
	 */
	private ModelException valideDescriptionArticle(String description, ModelException modelBllException) {
		if (description.trim().length() > 300) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_ARTICLE_DESCRIPTION_LONGUEUR, "La description est trop longue.");
		}
		return modelBllException;
	}

	/*
	 * vérifie que l'enchère est supérieure au prix de vente actuel de l'article
	 * détermine si c'est la première enchère effectuée sur l'objet
	 * 
	 * @param noArticle
	 * 
	 * @throws ModelException
	 */
	private boolean controleMontantEnchere(Integer montantEnchere, Integer noArticle) throws ModelException {

		Integer prixInitial = null;
		Integer prixActuel = null;
		Boolean premiereEnchere = false;
		ModelException modelBllException = new ModelException();

		try {

			prixActuel = ((ArticleDAO) articleDAO).retrieveItemCurrentPrice(noArticle);
			// // System.out.println("\nTEST MANAGER ARTICLE // Prix actuel de l'objet : " + prixActuel);

			if (prixActuel == null || prixActuel == 0) {
				premiereEnchere = true;
			}

			prixInitial = ((ArticleDAO) articleDAO).retrieveItemStartingPrice(noArticle);
			// System.out.println("\nTEST MANAGER ARTICLE // Prix initial de l'objet : " + prixInitial);

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

	/**
	 * vérifie que les enchères ne sont pas ouvertes sur un article passé en paramètre
	 * 
	 * @param article
	 * @return encheresOuvertes
	 * @throws ModelException
	 */
	public boolean controleDateDebutOuvertureEncheres(Article article) throws ModelException {

		LocalDateTime dateHeureDebutEncheres = null;
		boolean encheresOuvertes = false;

		try {
			dateHeureDebutEncheres = ((ArticleDAO) articleDAO).retrieveBidStartingDateTime(article);


		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

		if (dateHeureDebutEncheres.isBefore(LocalDateTime.now()) || dateHeureDebutEncheres.isEqual(LocalDateTime.now())) {
			encheresOuvertes = true;
		}

		return encheresOuvertes;
	}
	
	/**
	 * vérifie que les enchères ne sont pas clôturées sur un article passé en paramètre
	 * 
	 * @param article
	 * @return encheresCloturees
	 * @throws ModelException
	 */
	public boolean controleDateFinClotureEncheres(Article article) throws ModelException {

		LocalDateTime dateHeureFinEncheres = null;
		boolean encheresCloturees = false;

		try {
			dateHeureFinEncheres = ((ArticleDAO) articleDAO).retrieveBidClosingDateTime(article);

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		
		if (dateHeureFinEncheres.isBefore(LocalDateTime.now()) || dateHeureFinEncheres.isEqual(LocalDateTime.now())) {
			encheresCloturees = true;
		}

		return encheresCloturees;
	}

	
	/* ----------------------------------------- */
	/* -------------- Méthodes DAO ------------- */
	/* ----------------------------------------- */

	/**
	 * récupère et retourne les articles pour lesquels les enchères sont ouvertes
	 * cette méthode sera appelée depuis la méthode doGet() de la servlet Accueil
	 * 
	 * @param trieur
	 * @return listesEncheresEC
	 * @throws ModelException
	 */
	public List<Article> recupereArticlesEncheresOuvertesPost(Trieur trieur) throws ModelException {

		List<Article> listesEncheresEC = null;
		ModelException modelBllException = new ModelException();

		try {
			listesEncheresEC = ((ArticleDAO) articleDAO).retrieveCurrentlyForSaleItemsWithFilter(trieur);

		} catch (ModelException e) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_CHARGEMENT_ENCHERES_EC,
					"Une erreur est survenue lors du chargement des enchères en cours.");
			e.printStackTrace();
			throw e;
		}

		return listesEncheresEC;

	}

	/**
	 * rérécupère et retourne les articles pour lesquels les enchères sont ouvertes
	 * cette méthode sera appelée depuis la méthode doGet() de la servlet Accueil
	 * 
	 * @return listesEncheresEC
	 * @throws ModelException
	 */
	public List<Article> recupereArticlesEncheresOuvertesGet() throws ModelException {

		List<Article> listesEncheresEC = null;
		ModelException modelBllException = new ModelException();

		try {
			listesEncheresEC = ((ArticleDAO) articleDAO).retrieveCurrentlyForSaleItemsGet();

		} catch (ModelException e) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_CHARGEMENT_ENCHERES_EC,
					"Une erreur est survenue lors du chargement des enchères ouvertes.");
			e.printStackTrace();
			throw e;
		}

		return listesEncheresEC;

	}

	/**
	 * tri et récupère les articles selon la case cochée par l'utilisateur
	 * 
	 * @param trieur
	 * @return
	 * @throws ModelException
	 */
	public List<Article> trieEtRecupereArticles(Trieur trieur) throws ModelException {

		List<Article> listeSelectionArticles = new ArrayList<Article>();

		/* Si la case "mes enchères remportées" est cochée */
		if (trieur.getEncheresUtilisateurG() != null) {

			try {
				listeSelectionArticles = ((ArticleDAO) articleDAO).retrieveUserPurchasedItems(trieur);
				// System.out.println("\nTEST MANAGER ARTICLE trieEtRecupereArticles //
				// listeSelectionArticles = " + listeSelectionArticles);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}

		/* Si la case "mes enchères en cours" est cochée */
		if (trieur.getEncheresUtilisateurEC() != null) {

			try {
				listeSelectionArticles = ((ArticleDAO) articleDAO).retrieveUserWishlistWithFilter(trieur);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}

		/* Si la case "mes enchères remportées" est cochée */
		if (trieur.getEncheresUtilisateurG() != null) {
			try {
				listeSelectionArticles = ((ArticleDAO) articleDAO).retrieveUserPurchasedItems(trieur);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}

		/* Si une des cases "ventes" est cochée */
		if (trieur.getVentesUtilisateurAttente() != null || trieur.getVentesUtilisateurEC() != null
				|| trieur.getVentesUtilisateurT() != null) {

			try {
				listeSelectionArticles = ((ArticleDAO) articleDAO).retrieveUserSellsWithFilter(trieur);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}

		/* Si la case "enchères en cours" est cochée */
		if (trieur.getEncheresEC() != null || trieur.getCategorie() != 0 || !trieur.getKeyword().isEmpty()) {

			try {
				listeSelectionArticles = ((ArticleDAO) articleDAO).retrieveCurrentlyForSaleItemsWithFilter(trieur);

			} catch (ModelException e) {
				e.printStackTrace();
				throw e;
			}
		}

		/* Si aucune case n'est cochée */
		// if (trieur.getCategorie() != 0 || !trieur.getKeyword().isEmpty()) {
		//
		// System.out.println("JE SUIS PASSE PAR LA");
		// try {
		// listeSelectionArticles = ((ArticleDAO)
		// articleDAO).retrieveCurrentlyForSaleItemsAndSalesToComeWithFilter(trieur);
		//
		// } catch (ModelException e) {
		// e.printStackTrace();
		// throw e;
		// }
		// }
		//
		return listeSelectionArticles;

	}

	/**
	 * détermine si c'est la première enchère sur l'article passé en paramètre
	 * vérifie que l'enchère est supérieure à la précédente enchère vérfie que
	 * l'utilisateur a un crédit suffisant appelle le DAO pour ajouter l'enchère
	 * dans la BDD
	 * 
	 * @param enchere
	 * @return
	 * @throws ModelException
	 */
	public Enchere controleEtAjouteEnchereSurArticle(Enchere enchere) throws ModelException {

		Boolean premiereEnchere;
		Enchere precedenteEnchere = null;
		UtilisateurManager utilisateurManager = new UtilisateurManager();
		ModelException modelBllException = new ModelException();
		
		try {

			/* déterminer si c'est la première enchère proposée pour l'article */
			/* vérifier que l'enchère est bien supérieure à la précédente enchère */
			premiereEnchere = controleMontantEnchere(enchere.getMontant(), enchere.getArticle().getNoArticle());

		} catch (ModelException e2) {
			e2.printStackTrace();
			throw e2;
		}

		/* si l'enchère est bien supérieure à la précédente enchère */
		if (!modelBllException.contientErreurs()) {

			try {
				/* vérifier que l'utilisateur a bien un crédit suffisant */
				utilisateurManager.recupereEtControleSoldeCredits(enchere.getEncherisseur().getNoUtilisateur(), enchere.getMontant());

			} catch (ModelException e1) {
				e1.printStackTrace();
				throw e1;
			}
		}

		/*
		 * si l'enchère est bien supérieure à l'enchère précédente et si l'utilisateur a
		 * un crédit suffisant, on appelle le manager des enchères
		 */

		if (!modelBllException.contientErreurs()) {

			EnchereManager enchereManager = new EnchereManager();
			try {
				precedenteEnchere = enchereManager.ajouteEnchere(enchere, precedenteEnchere, premiereEnchere);

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

		}

		// if (!modelBllException.contientErreurs()) {
		//
		// /* Si l'objet n'avait jamais fait l'objet d'une enchère */
		// if (premiereEnchere) {
		// System.out.println("\nTEST Manager // Il s'agit d'une première enchère sur
		// cet article.");
		//
		// try {
		// /* créer l'enchère dans la base de données */
		// enchereDAO.insert(enchere);
		//
		// } catch (ModelException e) {
		// e.printStackTrace();
		// throw e;
		// }
		//
		// /* s'il y a déjà eu des enchères sur l'article */
		// } else {
		//
		// try {
		// /* récupérer le numéro du dernier enchérisseur */
		// precedenteEnchere = ((EnchereDAO)
		// enchereDAO).returnLastBid(enchere.getArticle().getNoArticle());
		//
		// /* créer l'enchère dans la base de données */
		// enchereDAO.insert(enchere);
		//
		// } catch (ModelException e) {
		// e.printStackTrace();
		// throw e;
		// }
		//
		// }
		// }

		return precedenteEnchere;
	}

	/* récupère toutes les enchères émises sur un article */
	public List<Enchere> recupereEncheres(Integer noArticle) throws ModelException {
		List<Enchere> listeEncheres;
		try {
			listeEncheres = ((EnchereDAO) enchereDAO).retrieveAllItemBids(noArticle);
		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		
		return listeEncheres;
	}

}
