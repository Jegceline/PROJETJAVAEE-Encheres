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

import fr.eni.javaee.encheres.bo.Utilisateur;

/**
 * Servlet Filter implementation class FilterAdmin
 */
@WebFilter("/admin/*")
public class FilterAdmin implements Filter {

    public FilterAdmin() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		 /* cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Utilisateur utilisateurSession = (Utilisateur) request.getSession().getAttribute("profilUtilisateur");
        
        /* si l'utilisateur est identifié */
        if (utilisateurSession != null) {
        	
        	/* si ce n'est pas un administrateur */
        	if(!utilisateurSession.isAdministrateur()) {
        		
        		/* redirection vers la page d'accueil */
        		response.sendRedirect( request.getContextPath()+"/accueil");
        		
        	} else { /* si c'est un admin */
        		
        		/* affichage de la page restreinte */
        		chain.doFilter( request, response );
        	}
        	
        } else { /* si l'utilisateur n'est pas identifié */
        	
        	/* redirection vers la page d'accueil */
    		response.sendRedirect( request.getContextPath()+"/accueil");
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
