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
			// System.out.println("\nTEST SERVLET // Un attribut encheresEC a été créé.");

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
		
		Integer noCategorie = Integer.parseInt(request.getParameter("no_categorie"));
		String keyword = request.getParameter("keyword");
		// System.out.println("\nDEBUG SERVLET // Valeur de keyword = " + keyword);

		/* Récupérer les paramètres de tri et les placer dans une map */
		
//		Map<String, String> mapCritères = new HashMap<String, String>();
//		mapCritères.put("noCategorie", request.getParameter("no_categorie"));
//		mapCritères.put("keyword", request.getParameter("keyword"));

		/* Appeler le manager */
		
		ArticleManager articleManager = new ArticleManager();
		List<Article> listeArticlesFiltres = new ArrayList<Article>();
		try {
			listeArticlesFiltres = articleManager.trieEtRecupereArticles(noCategorie, keyword);
			request.setAttribute("articlesFiltres", listeArticlesFiltres);
			System.out.println("\nTEST SERVLET // Un attribut articlesFiltres a été créé.");
			
		} catch (ModelException e) {
			e.printStackTrace();
		}

		/* Rediriger vers la page accueil.jsp */
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);

	}

}
