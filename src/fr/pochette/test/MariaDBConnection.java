package fr.pochette.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

class MariaDBConnection {
	
	@Test
	void test() {
		String url ="jdbc:mariadb://localhost/";
		String user = "pochette_user";
		String password = "pochette_password";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement()){
                //execute query
                ResultSet rs = stmt.executeQuery("SELECT 'Hello World!'");
                //position result to first
                rs.first();
                assertTrue("Hello World!".equals(rs.getString(1)));
		}
		catch(SQLException e) {
			fail("La connection a échouée : " + e.getLocalizedMessage());
		}
	}

}
