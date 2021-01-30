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
		
		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");
		
		/* Récupérer l'enchère, l'identifiant de l'utilisateur et le numéro de l'article */
		Integer montantEnchere = Integer.parseInt(request.getParameter("enchere_prix"));
		// System.out.println("\nTEST SERVLET // Montant de l'enchère = " + enchere_prix);
		
		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
		Integer noUtilisateur = utilisateur.getNoUtilisateur();
		Integer noArticle = (Integer) request.getSession().getAttribute("noArticle");
		System.out.println("\nTEST SERVLET // id de l'article : " + noArticle);
		System.out.println("\nTEST SERVLET // id de l'utilisateur : " + noUtilisateur);
		
		/* Créer un objet Enchere */
		Enchere enchere = new Enchere(montantEnchere, noArticle, noUtilisateur);
		
		/* Appeler les managers */
		ArticleManager articleManager = new ArticleManager();
		UtilisateurManager utilisateurManager = new UtilisateurManager();

		try {
			articleManager.encherit(enchere);
			utilisateurManager.actualiseCredit(enchere);
			Utilisateur utilisateurMaj = utilisateurManager.recupereUtilisateur(noUtilisateur);
			
			request.setAttribute("succesEnchere", "Votre enchère a bien été prise en compte.");
			System.out.println("\nDEBUG SERVLET // Un attribut succesEnchere a été créé.");
			
			request.getSession().setAttribute("profilUtilisateur", utilisateurMaj);
			
			System.out.println("\nTEST SERVLET // Crédit en session : " + utilisateurMaj.getCredit());
			
			response.sendRedirect(request.getContextPath() + "/accueil");
			
		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/detail-article.jsp").forward(request, response);
		}

	}

}
