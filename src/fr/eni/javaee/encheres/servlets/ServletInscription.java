package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletConnexion
 */
@WebServlet("/inscription")
public class ServletInscription extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// rediriger vers la page inscription.jsp
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/inscription.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// spécifier l'encodage
		request.setCharacterEncoding("UTF-8");

		// récupérer les paramètres du formulaire
		String pseudo = request.getParameter("pseudo");
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String email = request.getParameter("email");
		String telephone = request.getParameter("telephone");
		String rue = request.getParameter("rue");
		String codepostal = request.getParameter("codePostal");
		String ville = request.getParameter("ville");
		String motdepasse = request.getParameter("motDePasse");
		String confirmation = request.getParameter("motDePasseBis");

		// Création de l'objet Utilisateur
		Utilisateur utilisateur = new Utilisateur(pseudo, nom, prenom, email, telephone, rue, codepostal, ville, motdepasse);
		System.out.println("\nTEST // Utilisateur créé dans la servlet  : " + utilisateur);

		// appeler le manager pour qu'il vérifie l'intégrité des données
		UtilisateurManager inscriptionManager = new UtilisateurManager();

		try {
			utilisateur = inscriptionManager.ajouteUtilisateur(utilisateur, confirmation);
			System.out.println("\nTEST // Utilisateur retourné par le manager : " + utilisateur);

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs()); // on créé un attribut dans lequel on place la map d'erreurs
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/inscription.jsp").forward(request, response);

		}

		if (utilisateur == null) {
			request.setAttribute("erreurData", "L'inscription a échoué.");
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/inscription.jsp").forward(request, response);

		} else {
			request.getSession().setAttribute("profilUtilisateur", utilisateur);
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
		}


	}

}
