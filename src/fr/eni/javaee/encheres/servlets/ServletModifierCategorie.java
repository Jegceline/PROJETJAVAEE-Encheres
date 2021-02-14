package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletModifierCategorie
 */
@WebServlet("/admin/modifier-categorie")
public class ServletModifierCategorie extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/modifier-categorie.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("modifier") != null) {
			Integer noCategorie = Integer.parseInt(request.getParameter("no_categorie"));
			response.sendRedirect(request.getContextPath() + "/admin/modifier-categorie?noCategorie=" + noCategorie);
		}
		
		if (request.getParameter("enregistrer") != null) {
			
		}
	}

}
