package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.UtilisateurManagerV2;
import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletModifierProfil
 */
@WebServlet("/membre/modifier-profil")
public class ServletModifierProfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private UtilisateurManagerV2 utilisateurManager = new UtilisateurManagerV2();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/modifier-profil.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* On spécifie l'encodage */
		request.setCharacterEncoding("UTF-8");
		
		/* On récupère l'objet utilisateur se trouvant dans la session */
		Utilisateur utilisateurSession = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
//		System.out.println("\nTEST SERVLET MODIFIER PROFIL // Utilisateur actuellement en mémoire dans la session : " + utilisateurSession);
		
		/* on récupère la primary key de l'utilisateur */
		Integer noUtilisateur = utilisateurSession.getNoUtilisateur();
		
		/* ------------------------------------------------------------ */
		/* Si la méthode doPost a été appelée par le bouton enregistrer */
		/* ------------------------------------------------------------ */
		
		if(request.getParameter("enregistrer")!=null) {
			
			/* on recupère tous les paramères du formulaire */
			String pseudo = request.getParameter("pseudo");
			String nom = request.getParameter("nom");
			String prenom = request.getParameter("prenom");
			String email = request.getParameter("email");
			String telephone = request.getParameter("telephone");
			String rue = request.getParameter("rue");
			String codepostal = request.getParameter("codePostal");
			String ville = request.getParameter("ville");
			String motDePasse = request.getParameter("motDePasse");
			String motDePasse2 = request.getParameter("motDePasseBis");

			
			Utilisateur utilisateurAvecModif = new Utilisateur(noUtilisateur, pseudo, nom, prenom, email, telephone, rue, codepostal, ville, motDePasse);
//			System.out.println("\nTEST SERVLET MODIFIER PROFIL // Utilisateur créé dans la servlet avec les modif : " + utilisateurAvecModif);

			Utilisateur utilisateurMisAJour = null;
			
			/* Appeler le manager pour qu'il vérifie l'intégrité des données */
			// UtilisateurManager utilisateurManager = new UtilisateurManager();

			try {
				utilisateurMisAJour = utilisateurManager.metAJourUtilisateur(utilisateurSession, utilisateurAvecModif, motDePasse2);
				
				/* mise à jour de l'objet utilisateur dans la session (ce nouvel attribut écrasera l'ancien) */
				request.getSession().setAttribute("profilUtilisateur", utilisateurMisAJour);
				
//				/* attribut à envoyer à la page profil.jsp pour affichage */
//				request.setAttribute("succesModifProfil", "Les modifications ont été enregistrées avec succès.");
//				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/profil.jsp").forward(request, response);
				
	 			response.sendRedirect(request.getContextPath() + "/membre/profil?succesModifProfil=Les modifications ont %C3%A9t%C3%A9 enregistr%C3%A9es avec succ%C3%A8s.");

			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs()); // on créé un attribut dans lequel on place la map d'erreurs
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/modifier-profil.jsp").forward(request, response);
			}
			
			/* ---------------------------------------------------------- */
			/* Si la méthode doPost a été appelée par le bouton supprimer */
			/* ---------------------------------------------------------- */
			
		} else {

			// UtilisateurManager utilisateurManager = new UtilisateurManager();
			
			try {
				utilisateurManager.supprimeUtilisateur(noUtilisateur);
				request.getSession().invalidate();
				
//				/* attribut à envoyer à la page accueil.jsp pour affichage */
//				request.setAttribute("succesSupressionUtilisateur", "Votre compte a bien été supprimé.");
//				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp?").forward(request, response);
				
				response.sendRedirect(request.getContextPath() + "/accueil?succesSupressionUtilisateur=Votre compte a bien %C3%A9t%C3%A9 supprim%C3%A9.");
				
			} catch (ModelException e) {
				e.printStackTrace();
				
				/* attribut à envoyer à la page accueil.jsp pour affichage */
				request.setAttribute("mapErreurs", e.getMapErreurs());
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/membre/modifier-profil.jsp").forward(request, response);
			}
			
		}
		
	}

}
