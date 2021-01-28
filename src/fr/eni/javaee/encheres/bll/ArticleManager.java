package fr.eni.javaee.encheres.bll;

import java.time.LocalDate;
import java.util.List;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.dal.ArticleDAO;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;

public class ArticleManager {

	private ModelException modelBllException = new ModelException();
	private DAO<Article> articleDAO = DAOFactory.getArticleDAO();

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
			listesEncheresEC = ((ArticleDAO)articleDAO).selectByStatus();
			
		} catch (ModelException e) {
			modelBllException.ajouterErreur(CodesErreurs.ERREUR_CHARGEMENT_ENCHERES_EC, "Une erreur est survenue lors du chargement des enchères en cours.");
			e.printStackTrace();
			throw e;
		}
		
		return listesEncheresEC;
		
	}

}
