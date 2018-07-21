package fr.pochette.dal;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DBConnexionProvider {
	
	private static DataSource dataSource;
	
	static
	{
		try {
			Context context = new InitialContext();
			DBConnexionProvider.dataSource=(DataSource) context.lookup("java:comp/env/jdbc/pool_cnx");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to access to the database.");
		}
	}

	public static Connection getConnection() throws SQLException {
		return DBConnexionProvider.dataSource.getConnection();
	}
}
