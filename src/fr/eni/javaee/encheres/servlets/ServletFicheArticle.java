package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.ArticleManagerV2;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletDetailArticle
 */
@WebServlet("/membre/fiche-article")
public class ServletFicheArticle extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// si on déclare un manager comme attribut de la servlet, on fera toujours appel à la même instance de ce manager
	// étant donné que Tomcat n'instancie qu'une seule fois les servlet et n'utilise plus que cette unique instance jusqu'à ce qu'il soit redémarré.
	// si on veut déclarer un manager comme attribut de la servlet, il ne faudra donc pas déclarer l'objet modelBllException
	// en tant qu'attribut du manager car il ne sera jamais ré-initialisé et sa map d'erreurs ne sera donc jamais vidée ! 
	// voire la version 2 du manager des articles
	
	private ArticleManagerV2 articleManager = new ArticleManagerV2();
	

	/**
	 * demande au manager si les enchères sont ouvertes ou clôturées (l'état des
	 * enchères va conditionner l'affichage des boutons Modifier et Enchérir sur la
	 * page fiche-article.jsp)
	 * 
	 * Si les enchères sont clôturées et si l'utilisateur est le vendeur de
	 * l'article, vérifie s'il y a eu une enchère et, si c'est le cas, le crédite du
	 * montant de la dernière enchère
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer le paramètre et le mettre en session pour qu'il soit dispo pour la
		 * méthode doPost */
		Integer noArticle = Integer.parseInt(request.getParameter("noArticle"));
		request.getSession().setAttribute("noArticle", noArticle);
		// System.out.println("\nTEST SERVLET DETAIL ARTICLE // Numéro de l'objet = " + noArticle);
		
		Article article = null;

		/* Appeler le manager pour récupérer les infos de l'objet */
		try {
			article = articleManager.recupereArticle(noArticle);
			
			/* Mettre l'article en mémoire dans la session pour le rendre disponible pour la
			 * méthode doPost */
			request.getSession().setAttribute("articleSelectionne", article);
			request.setAttribute("articleSelectionne", article);
			
			List<Enchere> listeEncheres = articleManager.recupereEncheres(noArticle);
			request.setAttribute("listeDesEncheres", listeEncheres);
			
		} catch (ModelException e1) {
			e1.printStackTrace();
			request.setAttribute("mapErreurs", e1.getMapErreurs());
		}

		boolean encheresOuvertes;
		boolean encheresCloturees;

		/* regarder si les enchères sont ouvertes OU si elles sont clôturées 
		 * pour conditionner l'affichage des boutons Modifier et Enchérir */

		try {
			encheresOuvertes = articleManager.controleDateDebutOuvertureEncheres(article);
			encheresCloturees = articleManager.controleDateFinClotureEncheres(article);

			if (encheresOuvertes) {
				request.setAttribute("encheresOuvertes", "La JSP n'affichera pas le bouton Modifier");
			}

			if (encheresCloturees) {
				request.setAttribute("encheresCloturees", "La JSP n'affichera pas le bouton Enchérir");
				verifieExistenceEnchere(article.getVendeur().getNoUtilisateur(), article, request);
			}

			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/fiche-article.jsp").forward(request, response);

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/fiche-article.jsp").forward(request, response);
		}

	}

	/* Cette méthode est appelée si les enchères sont clôturées. Elle permet de
	 * vérifier qu'au moins une enchère a été émise sur l'article affiché. Si c'est
	 * le cas, elle récupère le nom du dernier enchérisseur. */
	private void verifieExistenceEnchere(Integer noVendeur, Article article, HttpServletRequest request) throws ModelException {

		List<Object> valeursRetournees = new ArrayList<>();

		UtilisateurManager utilisateurManager = new UtilisateurManager();
		try {

			valeursRetournees = utilisateurManager.verifieExistenceEnchere(article);

			if ((boolean) valeursRetournees.get(1)) {

				request.setAttribute("auMoinsUneEnchere", "L'existence de cet attribut conditionnera l'affichage d'un message");

				Enchere derniereEnchere = (Enchere) valeursRetournees.get(0);
				request.setAttribute("nomDernierEncherisseur", derniereEnchere.getEncherisseur().getPseudo());
				// System.out.println("\nTEST SERVLET FICHE ARTICLE // Les attributs aucuneEnchere et auMoinsUneEnchere ont été créés.");

			} else {
				request.setAttribute("aucuneEnchere", "L'existence de cet attribut conditionnera l'affichage d'un message");
				// System.out.println("\nTEST SERVLET FICHE ARTICLE // Un attribut aucuneEnchere a été créé.");
			}

		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * cette méthode peut être appelée par les boutons enchérir et modifier de la
	 * page detail-article.jsp
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Enchere precedenteEnchere;

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer le numéro de l'utilisateur en session */
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
		Integer noUtilisateur = utilisateur.getNoUtilisateur();

		/* Récupérer le numéro de l'article mis en session par la méthode doGet */
		Integer noArticle = (Integer) request.getSession().getAttribute("noArticle");

		/* --------------------------------------------------------- */
		/* Si la méthode doPost a été appelée par le bouton enchérir */
		/* --------------------------------------------------------- */

		if (request.getParameter("encherir") != null) {
		
//			Integer montantEnchere = null;
//
//			/* Récupérer le montant de l'enchère et gérer le cas où l'utilisateur n'a renseigné aucune valeur */
			
//			try {
//				montantEnchere = Integer.parseInt(request.getParameter("enchere_prix"));
//
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//				request.setAttribute("montantNonRenseigne", "Vous n'avez pas renseigné de montant !");
//				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/fiche-article.jsp").forward(request, response);
//			}
			
			/* Récupérer le montant de l'enchère */
			Integer montantEnchere = Integer.parseInt(request.getParameter("enchere_prix"));

			/* Créer un objet Article pour le donner à l'objet Enchere */
			Article article = new Article();
			article.setNoArticle(noArticle);

			/* Créer un objet Utilisateur pour le donner à l'objet Enchere */
			Utilisateur encherisseur = new Utilisateur();
			encherisseur.setNoUtilisateur(noUtilisateur);

			/* Créer un objet Enchere */
			Enchere enchere = new Enchere(montantEnchere, encherisseur, article);

			/* Appeler les managers */
			// ArticleManager articleManager = new ArticleManager();
			UtilisateurManager utilisateurManager = new UtilisateurManager();

			try {
				/* récupération de la précédente enchère s'il y en avait une */
				precedenteEnchere = articleManager.controleEtAjouteEnchereSurArticle(enchere);
				
				// System.out.println("\nTEST SERVLET FICHE ARTICLE doPost() // Précédente enchère = "  + precedenteEnchere);

				/* actualisation du crédit de l'enchérisseur et du précédent enchérisseur s'il y en avait un */
				utilisateurManager.actualiseCredit(enchere, precedenteEnchere);

				/* récupération des infos mises à jour de l'utilisateur et mise à jour de ses infos dans la session */
				Utilisateur utilisateurMaj = utilisateurManager.recupereUtilisateur(noUtilisateur);
				request.getSession().setAttribute("profilUtilisateur", utilisateurMaj);

				response.sendRedirect(request.getContextPath() + "/accueil?succesEnchere=Votre ench%C3%A8re a bien %C3%A9t%C3%A9 prise en compte.");

			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/fiche-article.jsp").forward(request, response);

			}

			/* ------------------------------------------------------------ */
			/* Si la méthode doPost a été appelée par le bouton modifier */
			/* ------------------------------------------------------------ */

		} else {

			response.sendRedirect(request.getContextPath() + "/membre/modifier-article?noArticle=" + noArticle);
		}

	}

}
