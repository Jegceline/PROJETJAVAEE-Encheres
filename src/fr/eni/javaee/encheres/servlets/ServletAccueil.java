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
import fr.eni.javaee.encheres.bo.Trieur;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletAccueil
 */
@WebServlet("/accueil")
public class ServletAccueil extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** 
	 * retourne les articles pour lequels les enchères sont ouvertes
	 * cette méthode est appelée par l'url /accueil de la servlet
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Appeler le manager pour récupérer la liste des articles en cours de vente */
		ArticleManager articleManager = new ArticleManager();
		List<Article> listesEncheresEC = null;

		try {
			listesEncheresEC = articleManager.recupereArticlesEncheresOuvertesGet();
			request.setAttribute("encheresOuvertes", listesEncheresEC);
//			System.out.println("\nTEST SERVLET ACCUEIL doGet // Un attribut encheresOuvertes a été créé.");

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
		}

		/* Rediriger vers la page accueil.jsp */
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
	}

	/**
	 * retourne les articles que l'utilisateur souhaite afficher
	 * cette méthode est appelée par le bouton Rechercher de la page accueil.jsp
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer les paramètres de tri */
		String keyword = request.getParameter("keyword");
		Integer noCategorie = Integer.parseInt(request.getParameter("no_categorie"));
		String encheresEC = request.getParameter("encheres_ec");
		String encheresUtilisateurEC = request.getParameter("mes_encheres");
		String encheresUtilisateurGagnantes = request.getParameter("mes_encheres_remportees");
		String ventesUtilisateurAttente = request.getParameter("ventes_attente");
		String ventesUtilisateurEC = request.getParameter("ventes_ouvertes");
		String ventesUtilisateurTerminees = request.getParameter("ventes_terminees");

//		System.out.println("\nTEST SERVLET ACCUEIL doPost // Valeur du paramètre ventes_terminees = " + ventesUtilisateurT);
//		System.out.println("\nTEST SERVLET ACCUEIL doPost // Valeur du paramètre mes_encheres = " + encheresUtilisateur);

		Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");

		/* Créer un objet Trieur */
		Trieur trieur = new Trieur(utilisateur, keyword, noCategorie, encheresEC, encheresUtilisateurEC, encheresUtilisateurGagnantes,
				ventesUtilisateurAttente, ventesUtilisateurEC, ventesUtilisateurTerminees);

//		System.out.println("\nTEST SERVLET ACCUEIL doPost // Valeur de keyword = " + keyword);

		/* Appeler le manager */

		ArticleManager articleManager = new ArticleManager();
		List<Article> listeArticlesFiltres = new ArrayList<Article>();

		try {

			/* si un utilisateur est en session */
			if (utilisateur != null) {
				listeArticlesFiltres = articleManager.trieEtRecupereArticles(trieur);
//				System.out.println("\nTEST SERVLET ACCUEIL doPost // Un attribut articlesFiltres a été créé.");

			/* si pas d'utilisateur en session */
			} else {

				if (trieur.getCategorie() != null || !trieur.getKeyword().isEmpty()) {
					listeArticlesFiltres = articleManager.recupereArticlesEncheresOuvertesPost(trieur);
				}
			}
			request.setAttribute("selectionArticles", listeArticlesFiltres);

		} catch (ModelException e) {
			e.printStackTrace();
		}
		
		if (listeArticlesFiltres.isEmpty()) {
			request.setAttribute("noResult", "Votre recherche n'a retourné aucun résultat.");
//			System.out.println("\nTEST SERVLET ACCUEIL doPost // Un attribut noResult a été créé.");
		}

		/* Rediriger vers la page accueil.jsp */
		request.setAttribute("trieur", trieur);
//		System.out.println("\nTEST SERVLET ACCUEIL doPost // Un attribut trieur a été créé.");
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);

	}

}
