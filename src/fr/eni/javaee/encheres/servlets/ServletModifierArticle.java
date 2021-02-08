package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletModifierArticle
 */
@WebServlet("/membre/modifier-article")
public class ServletModifierArticle extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* récupérer le numéro de l'article et le mettre en session pour le rendre dispo à la méthode doPost */
		Integer noArticle = Integer.parseInt(request.getParameter("noArticle"));
		request.getSession().setAttribute("noArticle", noArticle);
		
		boolean enchereEnCours = false;

		Article article = new Article();
		article.setNoArticle(noArticle);

		ArticleManager articleManager = new ArticleManager();

		try {
			enchereEnCours = articleManager.controleDateDebutOuvertureEncheres(article);
//			System.out.println("\nTEST SERVLET MODIFIER ARTICLE // enchereEnCours = " + enchereEnCours);

			if (enchereEnCours) {
				request.setAttribute("encheresOuvertes", "Vous ne pouvez pas modifier un article sur lequel les enchères sont ouvertes.");
			}

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/modifier-article.jsp").forward(request, response);
		}

		article = articleManager.recupereArticle(noArticle);

		request.setAttribute("article", article);
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/modifier-article.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		/* ------------------------------------------------------------------------------- */
		/* Si la méthode doPost a été appelée par le bouton enregistrer les modifications  */
		/* ------------------------------------------------------------------------------- */
		
		if (request.getParameter("enregistrerModifications") != null) {

			/* Récupérer les paramètres du formulaire */
			String nomArticle = request.getParameter("nom_article").trim();
			String description = request.getParameter("description").trim();
			Integer noCategorie = Integer.parseInt(request.getParameter("no_categorie").trim());
			Integer prixInitial = Integer.parseInt(request.getParameter("prix_initial").trim());
			LocalDate dateDebutEncheres = null;
			LocalDate dateFinEncheres = null;
			LocalTime heureDebutEncheres = null;
			LocalTime heureFinEncheres = null;
			String rue = request.getParameter("rue").trim();
			Integer codePostal = Integer.parseInt(request.getParameter("code_postal").trim());
			String ville = request.getParameter("ville").trim();

			Integer noArticle = (Integer) request.getSession().getAttribute("noArticle");

			/* Récupérer l'objet Uilisateur présent en session */
			Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
			// System.out.println("\nTEST SERVLET VENTE ARTICLE // L'utilisateur qui met en
			// vente l'article = " + utilisateur);
			Integer noUtilisateur = utilisateur.getNoUtilisateur();

			/* Formatage des dates */
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				dateDebutEncheres = LocalDate.parse(request.getParameter("date_debut_encheres"), dtf);
				dateFinEncheres = LocalDate.parse(request.getParameter("date_fin_encheres"), dtf);

				DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
				heureDebutEncheres = LocalTime.parse(request.getParameter("heure_debut_encheres"), dtf2);
				heureFinEncheres = LocalTime.parse(request.getParameter("heure_fin_encheres"), dtf2);

			} catch (Exception e) {
				e.printStackTrace();
			}

			/* Création d'un objet Adresse à donner à l'objet Article */
			Adresse adresseRetrait = new Adresse(rue, codePostal, ville);
			// System.out.println("\nTEST SERVLET VENTE ARTICLE // L'adresse de retrait est
			// : " + adresseRetrait);

			/* Création d'un objet Utilisateur à donner à l'objet Article */
			Utilisateur vendeur = new Utilisateur();
			vendeur.setNoUtilisateur(noUtilisateur);

			/* Création d'un objet Catégorie à donner à l'objet Article */
			Categorie categorie = new Categorie();
			categorie.setNoCategorie(noCategorie);

			/* Création d'un objet Article */

			Article article = new Article(noArticle, nomArticle, description, dateDebutEncheres, heureDebutEncheres, dateFinEncheres,
					heureFinEncheres, prixInitial, categorie, vendeur, adresseRetrait);
			// System.out.println("\nTEST SERVLET ARTICLE // L'article à vendre est : " +
			// article);

			/* Appeler le manager */
			ArticleManager articleManager = new ArticleManager();

			try {
				articleManager.metAJourArticle(article);
				// request.setAttribute("succesModificationsArticle", "Vos modifications ont
				// bien été prises en compte.");
				// request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request,
				// response);
				response.sendRedirect(request.getContextPath()
						+ "/accueil?succesModificationsArticle=Vos modifications ont bien %C3%A9t%C3%A9 prises en compte.");

			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/modifier-article.jsp").forward(request, response);
			}
		}
		
		/* ----------------------------------------------------------- */
		/* Si la méthode doPost a été appelée par le bouton supprimer  */
		/* ----------------------------------------------------------- */

		if (request.getParameter("supprimer") != null) {
			Integer noArticle = (Integer) request.getSession().getAttribute("noArticle");

			/* Appeler le manager */
			ArticleManager articleManager = new ArticleManager();

			try {
				articleManager.supprimerArticle(noArticle);
				response.sendRedirect(
						request.getContextPath() + "/accueil?succesSuppressionArticle=L'article a bien %C3%A9t%C3%A9 supprim%C3%A9.");

			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/modifier-article.jsp").forward(request, response);
			}
		}

	}

}
