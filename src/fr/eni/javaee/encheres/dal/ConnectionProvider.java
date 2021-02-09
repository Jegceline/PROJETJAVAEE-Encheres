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

    ConnectionProvider() {
    }

    public static Connection getConnection() throws SQLException {
    	dataSource.getConnection().setAutoCommit(false);
        return dataSource.getConnection();
    }

    
}
