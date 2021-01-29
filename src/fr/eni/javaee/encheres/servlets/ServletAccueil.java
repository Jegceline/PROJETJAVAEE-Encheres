package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.ArticleManager;
import fr.eni.javaee.encheres.bo.Article;

/**
 * Servlet implementation class ServletAccueil
 */
@WebServlet("/accueil")
public class ServletAccueil extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Appeler le manager pour récupérer la liste des articles en cours de vente */
		ArticleManager articleManager = new ArticleManager();
		List<Article> listesEncheresEC = null;
		try {
			listesEncheresEC = articleManager.recupereEncheresEnCours();
			request.setAttribute("encheresEC", listesEncheresEC);
			System.out.println("\n TEST SERVLET // Un attribut encheresEC a été créé.");

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
		}

		/* Rediriger vers la page accueil.jsp */
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer les paramètres de tri */
		// Integer noCategorie = Integer.parseInt(request.getParameter("no_categorie"));
		// String keyword = request.getParameter("keyword");

		/* Appeler le manager */
		
		// ArticleManager articleManager = new ArticleManager();
		// articleManager.trieEtRecupereArticles(noCategorie, keyword);

//		if (noCategorie != null && keyword == null) {
//			articleManager.trieEtRecupereArticlesParCategorie(noCategorie);
//		}
//		
//		if(keyword != null && noCategorie == null) {
//			articleManager.trieEtRecupereArticlesParMotCle(keyword);
//		}
//		
//		if(noCategorie != null && keyword != null) {
//			articleManager.trieEtRecupereArticlesParMotCleEtCategorie(keyword, noCategorie);
//		}

		/* Rediriger vers la page accueil.jsp */
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);

	}

}
