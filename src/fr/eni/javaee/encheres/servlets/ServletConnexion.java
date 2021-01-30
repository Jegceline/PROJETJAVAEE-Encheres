package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.CodesErreurs;
import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletAccueil
 */
@WebServlet("/connexion")
public class ServletConnexion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Map<Integer, String> mapErreurs = new HashMap<Integer, String>();

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer les paramètres du formulaire */
		String identifiant = request.getParameter("identifiant");
		System.out.println("\nSERVLET // Identifiant récupéré qui sera envoyé au Manager : " + identifiant);
		String motDePasse = request.getParameter("motdepasse");

		/* Appeler le manager */
		UtilisateurManager utilisateurManager = new UtilisateurManager();
		Utilisateur utilisateur = null;
		
		try {
				utilisateurManager.verifieExistenceUtilisateur(identifiant, motDePasse);
				utilisateur = utilisateurManager.rechercheUtilisateur(identifiant);	
				System.out.println("\n SERVLET // Utilisateur retourné par le Manager : " + utilisateur);
			
		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
		}

		if (mapErreurs.containsKey(CodesErreurs.ERREUR_MOTDEPASSE_INCORRECT)) {
			request.setAttribute("erreurMotDePasse", "Mot de passe incorrect.");
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
			
		} else {
			
			/* On place l'objet utilisateur dans la session pour y avoir accès partout dans l'application */
			request.getSession().setAttribute("profilUtilisateur", utilisateur);
			
			request.getSession().setAttribute("succesConnexion", "Connexion réussie !");
			response.sendRedirect(request.getContextPath() + "/accueil");
			// remarque : on veut appeler la méthode doGet() de la servlet Accueil et non sa méthode doPost() donc on ne peut pas utiliser de RequestDispatcher

		}

	}

}
