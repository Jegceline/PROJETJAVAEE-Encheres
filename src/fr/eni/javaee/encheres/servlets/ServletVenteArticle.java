package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.ArticleManager;
import fr.eni.javaee.encheres.bo.Adresse;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletVenteArticle
 */
@WebServlet("/vente")
public class ServletVenteArticle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/vente.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");
		
		/* Récupérer les paramètres du formulaire */
		
		if(request.getParameter("enregistrer") != null) {
			
			String nomArticle = request.getParameter("nom_article");
			String description = request.getParameter("description");
			Integer noCategorie =  Integer.parseInt(request.getParameter("no_categorie"));
			Integer prixInitial = Integer.parseInt(request.getParameter("prix_initial"));
			LocalDate dateDebutEncheres = null;
			LocalDate dateFinEncheres = null;
			String rue = request.getParameter("rue");
			Integer codePostal = Integer.parseInt(request.getParameter("code_postal"));
			String ville = request.getParameter("ville");
			
			/* Récupérer l'objet Uilisateur présent en session */
			
			Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
			System.out.println("\n TEST SERVLET // L'utilisateur qui met en vente l'article : " + utilisateur);
			Integer noUtilisateur = utilisateur.getNoUtilisateur();
			
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				dateDebutEncheres = LocalDate.parse(request.getParameter("date_debut_encheres"), dtf);
				dateFinEncheres = LocalDate.parse(request.getParameter("date_fin_encheres"), dtf);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/* Création d'un objet Adresse */

			Adresse adresseRetrait = new Adresse(rue, codePostal, ville);
			System.out.println("\n TEST SERVLET // L'adresse de retrait est : " + adresseRetrait);
			
			/* Création d'un objet Article */
			
			Article article = new Article(nomArticle, description, dateDebutEncheres, dateFinEncheres, prixInitial, "EC", noUtilisateur, noCategorie, adresseRetrait);
			System.out.println("\n TEST SERVLET // L'article à vendre est : " + article);
			
			/* Appeler le manager */
			
			ArticleManager articleManager = new ArticleManager();
			
			try {
				article = articleManager.ajouteArticle(article);
				
			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/vente.jsp").forward(request, response);
			}
			
			if(article == null) {
				request.setAttribute("erreurData", "La mise en vente de votre article a échoué.");
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/vente.jsp").forward(request, response);
				
			} else {
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
			}
			
		} 
	}

}
