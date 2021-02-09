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
		String pseudo = request.getParameter("pseudo").trim();
		String nom = request.getParameter("nom").trim();
		String prenom = request.getParameter("prenom").trim();
		String email = request.getParameter("email").trim();
		String telephone = request.getParameter("telephone").trim();
		String rue = request.getParameter("rue").trim();
		String codepostal = request.getParameter("codePostal").trim();
		String ville = request.getParameter("ville").trim();
		String motdepasse = request.getParameter("motDePasse").trim();
		String confirmation = request.getParameter("motDePasseBis").trim();

		/* Création de l'objet Utilisateur */
		Utilisateur utilisateur = new Utilisateur(pseudo, nom, prenom, email, telephone, rue, codepostal, ville, motdepasse);
//		System.out.println("\nTEST SERVLET INSCRIPTION // Utilisateur créé dans la servlet  : " + utilisateur);

		// appeler le manager pour qu'il vérifie l'intégrité des données
		UtilisateurManager inscriptionManager = new UtilisateurManager();

		try {
			inscriptionManager.ajouteUtilisateur(utilisateur, confirmation);
//			System.out.println("\nTEST  SERVLET INSCRIPTION // Utilisateur retourné par le manager : " + utilisateur);

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs()); // on créé un attribut dans lequel on place la map d'erreurs
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/inscription.jsp").forward(request, response);

		}
			
			/* On place l'objet utilisateur dans la session (la session est conservée avec le sendRedirect) */
			request.getSession().setAttribute("profilUtilisateur", utilisateur);
			
//			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
			
//			Rq : le RequestDispatcher étant appelé depuis la méthode doPost, il va appeler la méthode doPost() de la servlet Accueil et non sa méthode doGet()
//			Par conséquent, les articles pour lesquels les enchères sont ouvertes ne seront pas automatiquement affichés sur la page d'accueil
//			(en effet, leur chargement se fait dans la méthode doGet() de la servlet Accueil)
//			C'est pourquoi on utilise une redirection (car sendRedirect appelle par défaut les méthodes doGet)
//			Pour transmettre des attributs à la jsp si le sendRedirect est utilisé, il faut les mettre non pas dans l'objet request mais dans l'objet session
			
			response.sendRedirect(request.getContextPath() + "/accueil?succesInscription=Bienvenue " + utilisateur.getPrenom() + ", vous avez %C3%A9t%C3%A9 cr%C3%A9dit%C3%A9 de 100 points !");
//		}

	}

}
