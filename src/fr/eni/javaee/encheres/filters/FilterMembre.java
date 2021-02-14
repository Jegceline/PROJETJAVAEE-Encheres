package fr.eni.javaee.encheres.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class FiltreUtilisateur
 */
@WebFilter("/membre/*")
public class FilterMembre implements Filter {
	
    public FilterMembre() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		 /* cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if (request.getSession().getAttribute("profilUtilisateur") == null ) {
        	
            /* redirection vers la page de connexion */
            response.sendRedirect( request.getContextPath()+"/connexion");
            
        } else {
            /* affichage de la page restreinte */
            chain.doFilter( request, response );
        }
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
