package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.UtilisateurManagerV2;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletAccueil
 */
@WebServlet("/connexion")
public class ServletConnexion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String COOKIE_UTILISATEUR_IDENTIFIANT = "cookieUserId";
	public static final String COOKIE_UTILISATEUR_MDP = "cookieUserPassword";
	
	private UtilisateurManagerV2 utilisateurManager = new UtilisateurManagerV2();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
	}

	/**
	 * méthode appelée depuis le bouton Connexion de la page connexion.jsp
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Spécifier l'encodage */
		request.setCharacterEncoding("UTF-8");

		/* Récupérer les paramètres du formulaire */
		String identifiantConnexion = request.getParameter("identifiant").trim();
		String motDePasse = request.getParameter("motdepasse").trim();
		// System.out.println("\nTEST SERVLET CONNEXION // Identifiant récupéré qui sera envoyé au Manager : " + identifiant);

		Utilisateur utilisateur = null;

		try {
			/* Appeler le manager */
			utilisateur = utilisateurManager.rechercheUtilisateur(identifiantConnexion);
			// System.out.println("\nTEST SERVLET CONNEXION // Utilisateur retourné par le Manager : " + utilisateur);

			/* si l'utilisateur existe */
			if (utilisateur != null) {
				utilisateurManager.recupereEtControleMdp(identifiantConnexion, motDePasse);

				/* si l'utilisateur a coché la case "se souvenir de moi", on enregistre son id
				 * et son mdp dans deux cookies qu'on envoie au navigateur */
				
				if (request.getParameter("memoriserUtilisateur") != null) {

					Cookie cookieIdentifiant = new Cookie(COOKIE_UTILISATEUR_IDENTIFIANT, identifiantConnexion);
					cookieIdentifiant.setMaxAge(60 * 60 * 24 * 365 * 10); // bricolage pour spécifier une durée de vie très longue

					Cookie cookieMdp = new Cookie(COOKIE_UTILISATEUR_MDP, motDePasse);
					cookieMdp.setMaxAge(60 * 60 * 24 * 365 * 10);

					response.addCookie(cookieIdentifiant);
					response.addCookie(cookieMdp);
				}
				
				/* on crédite l'utilisateur de toutes ses ventes qui se sont achevées depuis sa dernière connexion 
				 * ou depuis qu'il a cliqué sur le lien "actualiser mes points" */
				crediteUtilisateur(utilisateur, request);

				response.sendRedirect(request.getContextPath() + "/accueil");

			} else { /* si aucun utilisateur ne correspond à l'identifiant renseigné */
				request.setAttribute("utilisateurInconnu", "Utilisateur inexistant.");
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
			}

		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
		}

	}

	private void crediteUtilisateur(Utilisateur utilisateur, HttpServletRequest request) throws ModelException {

		try {
			utilisateurManager.crediteVendeur(utilisateur.getNoUtilisateur());
			Utilisateur utilisateurMaj = utilisateurManager.recupereUtilisateur(utilisateur.getNoUtilisateur());
			
			/* On place l'objet utilisateur dans la session */
			request.getSession().setAttribute("profilUtilisateur", utilisateurMaj);
			
		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
}
