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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");
		
		/* Récupérer le paramètre et le mettre en session */
		Integer noArticle = Integer.parseInt(request.getParameter("noArticle"));
		request.getSession().setAttribute("noArticle", noArticle);
		// System.out.println("\nTEST SERVLET // Numéro de l'objet = " + noArticle);
		
		/* Appeler le manager pour récupérer les infos de l'objet */
		ArticleManager articleManager = new ArticleManager();
		Article article = articleManager.recupereArticle(noArticle);
		
		/* Mettre l'article en mémoire dans la session */
		request.getSession().setAttribute("articleSelectionne", article);
		
		/* Rediriger vers la page detail-article.jsp si l'utilisateur est connecté */
		if(request.getSession().getAttribute("profilUtilisateur") != null){
			request.setAttribute("articleSelectionne", article);
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail-article.jsp").forward(request, response);		
			
		} else {
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Enchere precedenteEnchere;
		
		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");
		
		/* Récupérer le numéro de l'utilisateur en session */
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
		Integer noUtilisateur = utilisateur.getNoUtilisateur();
		Integer noArticle = (Integer) request.getSession().getAttribute("noArticle");
		
		/* si l'utilisateur a cliqué sur le bouton enchérir */
		if(request.getParameter("encherir") != null) {
			
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
				precedenteEnchere = articleManager.recoitEtAjouteEnchere(enchere);
				
				/* actualisation du crédit de l'enchérisseur et du précédent enchérisseur s'il y en avait un */
				utilisateurManager.actualiseCredit(enchere, precedenteEnchere);
				
				/* récupération des infos mises à jour de l'utilisateur et mise à jour de ses infos dans la session */
				Utilisateur utilisateurMaj = utilisateurManager.recupereUtilisateur(noUtilisateur);
				request.getSession().setAttribute("profilUtilisateur", utilisateurMaj);
				System.out.println("\nTEST SERVLET // Crédit en session : " + utilisateurMaj.getCredit());
				
				request.setAttribute("succesEnchere", "Votre enchère a bien été prise en compte.");
				System.out.println("\nDEBUG SERVLET // Un attribut succesEnchere a été créé.");
				
				// response.sendRedirect(request.getContextPath() + "/accueil");
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
				
			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail-article.jsp").forward(request, response);
				
			}
		
		/* si l'utilisateur a cliqué sur le bouton modifier */
		} else {
			
			request.getServletContext().getRequestDispatcher("/vente").forward(request, response);
		}

	}

}
