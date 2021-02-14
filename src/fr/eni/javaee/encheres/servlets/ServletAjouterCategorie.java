package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bo.Categorie;

/**
 * Servlet implementation class ServletAjouterCategorie
 */
@WebServlet("/admin/ajouter-categorie")
public class ServletAjouterCategorie extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private CategorieManager categorieManager = new CategorieManager();
       

    public ServletAjouterCategorie() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* rediriger vers la page ajouter-categorie.jsp */
		request.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/ajouter-categorie.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String libelle = request.getParameter("libelle_categorie");
		Categorie categorie = new Categorie(libelle);
		
		try {
			categorieManager.ajouteCategorie(categorie);
			request.setAttribute("succesAjoutCategorie", "La nouvelle catégorie a bien été ajoutée.");
			
		} catch (ModelException e) {
			e.printStackTrace();
			request.setAttribute("mapErreurs", e.getMapErreurs());
		}
		
		doGet(request, response);
	}

}
