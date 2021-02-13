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
 * Servlet implementation class ServletPaiement
 */
@WebServlet("/membre/paiement")
public class ServletPaiement extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UtilisateurManagerV2 utilisateurManager = new UtilisateurManagerV2();
		
		Utilisateur utilisateurSession = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
		
		try {
			utilisateurManager.crediteVendeur(utilisateurSession.getNoUtilisateur());
			Utilisateur utilisateurMaj = utilisateurManager.recupereUtilisateur(utilisateurSession.getNoUtilisateur());
			request.getSession().setAttribute("profilUtilisateur", utilisateurMaj);
			
		} catch (ModelException e) {
			e.printStackTrace();
		}
		
		response.sendRedirect(request.getContextPath() + "/accueil");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
