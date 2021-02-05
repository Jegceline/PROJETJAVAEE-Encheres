package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.ArticleManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletDetailArticle
 */
@WebServlet("/detail-article")
public class ServletDetailArticle extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * demande au manager si les enchères sont ouvertes sur l'article sélectionné
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/*
		 * Récupérer le paramètre et le mettre en session pour qu'il soit dispo pour la
		 * méthode doPost
		 */
		Integer noArticle = Integer.parseInt(request.getParameter("noArticle"));
		request.getSession().setAttribute("noArticle", noArticle);
//		System.out.println("\nTEST SERVLET DETAIL ARTICLE // Numéro de l'objet = " + noArticle);

		/* Appeler le manager pour récupérer les infos de l'objet */
		ArticleManager articleManager = new ArticleManager();
		Article article = articleManager.recupereArticle(noArticle);

		/*
		 * Mettre l'article en mémoire dans la session pour le rendre disponible pour la
		 * méthode doPost
		 */
		request.getSession().setAttribute("articleSelectionne", article);

		/* Rediriger vers la page detail-article.jsp si l'utilisateur est identifié */
		if (request.getSession().getAttribute("profilUtilisateur") != null) {
			
			request.setAttribute("articleSelectionne", article);

			boolean encheresOuvertes = false;

			/* regarder si les enchères sont ouvertes sur l'article */
			try {
				encheresOuvertes = articleManager.controleDateDebutOuvertureEncheres(article);

				/* si elles le sont, envoyer un attribut encheresOuvertes à la page detail-article.jsp 
				 * l'affichage d'un bouton modifier sera conditionné par l'absence de cet attribut */
				
				if (encheresOuvertes) {
					request.setAttribute("encheresOuvertes",
							"Vous ne pouvez pas modifier un article sur lequel les enchères sont ouvertes.");
				}

			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail-article.jsp").forward(request, response);
			}

			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail-article.jsp").forward(request, response);

		} else {
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
		}
	}

	/**
	 * cette méthode peut être appelée par les boutons enchérir et modifier de la page detail-article.jsp
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Enchere precedenteEnchere;

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer le numéro de l'utilisateur en session */
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
		Integer noUtilisateur = utilisateur.getNoUtilisateur();
		Integer noArticle = (Integer) request.getSession().getAttribute("noArticle");

		/* --------------------------------------------------------- */
		/* Si la méthode doPost a été appelée par le bouton enchérir */
		/* --------------------------------------------------------- */
		
		if (request.getParameter("encherir") != null) {

			/* Récupérer l'enchère et le numéro de l'article */
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
			ArticleManager articleManager = new ArticleManager();
			UtilisateurManager utilisateurManager = new UtilisateurManager();

			try {
				/* récupération de la précédente enchère s'il y en avait une */
				precedenteEnchere = articleManager.controleEtAjouteEnchereSurArticle(enchere);

				/*
				 * actualisation du crédit de l'enchérisseur et du précédent enchérisseur s'il y
				 * en avait un
				 */
				utilisateurManager.actualiseCredit(enchere, precedenteEnchere);

				/*
				 * récupération des infos mises à jour de l'utilisateur et mise à jour de ses
				 * infos dans la session
				 */
				Utilisateur utilisateurMaj = utilisateurManager.recupereUtilisateur(noUtilisateur);
				request.getSession().setAttribute("profilUtilisateur", utilisateurMaj);
//				System.out.println("\nTEST SERVLET DETAIL ARTICLE // Crédit en session : " + utilisateurMaj.getCredit());

				request.setAttribute("succesEnchere", "Votre enchère a bien été prise en compte.");
//				System.out.println("\nTEST SERVLET DETAIL ARTICLE // Un attribut succesEnchere a été créé.");

//				response.sendRedirect(request.getContextPath() + "/accueil");
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);

			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail-article.jsp").forward(request, response);

			}

			/* ------------------------------------------------------------ */
			/* Si la méthode doPost a été appelée par le bouton modifier */
			/* ------------------------------------------------------------ */
			
		} else {

			boolean enchereEnCours = false;

			/* Créer un objet Article pour le donner à l'objet Enchere */
			Article article = new Article();
			article.setNoArticle(noArticle);

			ArticleManager articleManager = new ArticleManager();

			try {
				enchereEnCours = articleManager.controleDateDebutOuvertureEncheres(article);

				if (enchereEnCours) {
					request.setAttribute("encheresEnCours",
							"Vous ne pouvez pas modifier un article sur lequel les enchères sont ouvertes.");
				}
				
//			Rq : en théorie, la méthode doPost n'a de toute façon pas pu être appelée par le bouton Modifier si les enchères étaient ouvertes sur l'article
				
			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail-article.jsp").forward(request, response);
			}

			request.getServletContext().getRequestDispatcher("/vente").forward(request, response);
		}

	}

}
