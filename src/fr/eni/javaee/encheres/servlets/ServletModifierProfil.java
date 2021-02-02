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
 * Servlet implementation class ServletModifierProfil
 */
@WebServlet("/modifier-profil")
public class ServletModifierProfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/modifierProfil.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* On spécifie l'encodage */
		request.setCharacterEncoding("UTF-8");
		
		/* On récupère l'objet utilisateur se trouvant dans la session */
		Utilisateur utilisateurSession = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
		System.out.println("\n DEBUG SERVLET // Utilisateur actuellement en mémoire dans la session : " + utilisateurSession);
		
		Integer noUtilisateur = utilisateurSession.getNoUtilisateur();
		
		/* Si l'utilisateur a cliqué sur le bouton enregistrer */
		if(request.getParameter("enregistrer")!=null) {
			
			// on doit récupérer tous les paramères du formulaire
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
			// System.out.println("\nTEST // Utilisateur créé dans la servlet avec les modif : " + utilisateurAvecModif);

			/* Appeler le manager pour qu'il vérifie l'intégrité des données */
			UtilisateurManager inscriptionManager = new UtilisateurManager();
			Utilisateur utilisateurMisAJour=null;

			try {
				utilisateurMisAJour = inscriptionManager.metAJourUtilisateur(utilisateurSession, utilisateurAvecModif, motDePasse2);
				
				/* mise à jour de l'objet utilisateur dans la session */
				request.getSession().setAttribute("profilUtilisateur", utilisateurMisAJour);
				
				/* attribut pour affichage */
				request.setAttribute("succesModif", "Les modifications ont été enregistrées avec succès.");
				
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/profil.jsp").forward(request, response);

			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("mapErreurs", e.getMapErreurs()); // on créé un attribut dans lequel on place la map d'erreurs
				request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/modifierProfil.jsp").forward(request, response);
			}
			
			
		} else {
			// si l'utilisateur a cliqué sur le bouton supprimer
			UtilisateurManager utilisateurManager = new UtilisateurManager();
			
			try {
				utilisateurManager.supprimeUtilisateur(noUtilisateur);
				request.getSession().invalidate();
				
			} catch (ModelException e) {
				e.printStackTrace();
				request.setAttribute("echecSuppression", "Il n'a pas été possible de supprimer ce compte.");
			}
			
			request.setAttribute("succesSupression", "Votre compte a bien été supprimé.");
			request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/accueil.jsp").forward(request, response);
		}
		
	}

}
