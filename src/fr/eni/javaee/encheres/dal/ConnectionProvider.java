package fr.eni.javaee.encheres.dal;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class ConnectionProvider {
	
    private static DataSource dataSource;

    static {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource)context.lookup("java:comp/env/jdbc/pool_cnx");
        
        } catch (NamingException var2) {
            var2.printStackTrace();
            throw new RuntimeException("Impossible d'accéder à la base de données.");
        }
    }

    /* Constructeur */
    ConnectionProvider() {
    }

    /**
     * ouvre une connexion
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
    	// dataSource.getConnection().setAutoCommit(false); : mettre l'auto-commit à false dans le ConnectionProvider pose des problèmes
    	// mieux vaut le faire localement sur la connexion obtenue au sein de chaque méthode effectuant un insert / update / delete 
    	
        return dataSource.getConnection();
    }

    /**
     * ferme la connexion passée en paramètre
     * @param cnx
     * @throws SQLException
     */
	public static void closeConnection(Connection cnx) throws SQLException {
		if (cnx != null)
			cnx.close();
	}
    
}
