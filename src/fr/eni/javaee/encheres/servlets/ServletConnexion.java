package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletAccueil
 */
@WebServlet("/connexion")
public class ServletConnexion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String COOKIE_UTILISATEUR_IDENTIFIANT = "cookieUserId";
	public static final String COOKIE_UTILISATEUR_MDP = "cookieUserPassword";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
	}

	/**
	 * méthode appelée depuis le bouton Connexion de la pahe connexion.jsp
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer les paramètres du formulaire */
		String identifiant = request.getParameter("identifiant");
		String motDePasse = request.getParameter("motdepasse");
//		System.out.println("\nTEST SERVLET CONNEXION // Identifiant récupéré qui sera envoyé au Manager : " + identifiant);

		/* Appeler le manager */
		UtilisateurManager utilisateurManager = new UtilisateurManager();
		Utilisateur utilisateur = null;

		try {
			utilisateur = utilisateurManager.rechercheUtilisateur(identifiant);
//			System.out.println("\nTEST SERVLET CONNEXION // Utilisateur retourné par le Manager : " + utilisateur);

			/* si l'utilisateur existe */
			if (utilisateur != null) {
				utilisateurManager.recupereEtControleMdp(identifiant, motDePasse);

			} else {
				request.setAttribute("utilisateurInconnu", "Utilisateur inexistant.");
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
			}

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
		}

			/* On place l'objet utilisateur dans la session */
			request.getSession().setAttribute("profilUtilisateur", utilisateur);

			/* si l'utilisateur a coché la case "se souvenir de moi", on enregistre son id et son mdp dans deux cookies qu'on envoie au navigateur */
			if (request.getParameter("memoriserUtilisateur") != null) {
				
				Cookie cookieIdentifiant = new Cookie(COOKIE_UTILISATEUR_IDENTIFIANT, identifiant);
				cookieIdentifiant.setMaxAge(60 * 60 * 24 * 365 * 10); // bricolage pour spécifier une durée de vie très longue
				
				Cookie cookieMdp = new Cookie(COOKIE_UTILISATEUR_MDP, motDePasse);
				cookieMdp.setMaxAge(60 * 60 * 24 * 365 * 10);
				
				response.addCookie(cookieIdentifiant);
				response.addCookie(cookieMdp);
			}

//			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
			
//			Rq : le RequestDispatcher étant appelé depuis la méthode doPost, il va appeler la méthode doPost() de la servlet Accueil et non sa méthode doGet()
//			Par conséquent, les articles pour lesquels les enchères sont onvertes ne seront pas automatiquement affichés sur la page d'accueil 
//			(en effet, leur chargement se fait dans la méthode doGet() de la servlet Accueil)
//			C'est pourquoi on utilise une redirection (car sendRedirect appelle par défaut les méthodes doGet)
//			Pour transmettre des attributs à la jsp si le sendRedirect est utilisé, il faut les mettre non pas dans l'objet request mais dans l'objet session

 			response.sendRedirect(request.getContextPath() + "/accueil");

	}
	
	

}
