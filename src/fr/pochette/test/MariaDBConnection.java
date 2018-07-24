package fr.pochette.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pochette.bo.Link;
import fr.pochette.dal.LinkDaoMariaDBJdbcImpl;

class MariaDBConnection {
	
	@Test
	void test_connection() {
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
	
	@Test
	void test_link_list_all_request() {
		String url ="jdbc:mariadb://localhost/POCHETTE_DB";
		String user = "pochette_user";
		String password = "pochette_password";
		try (Connection cnx = DriverManager.getConnection(url, user, password);){
				LinkDaoMariaDBJdbcImpl dao = new LinkDaoMariaDBJdbcImpl();
				List<Link> links = dao._listAll(cnx);
				assertEquals(5, links.size());
				for(Link l : links) {
					if(l.getIdLink() == 1)
						assertEquals("Documentation OCaml", l.getTitle());
				}
		}
		catch(SQLException e) {
			fail("La connection a échouée : " + e.getLocalizedMessage());
		}
	}
	
	@Test
	void test_link_select_by_id() {
		String url ="jdbc:mariadb://localhost/POCHETTE_DB";
		String user = "pochette_user";
		String password = "pochette_password";
		try (Connection cnx = DriverManager.getConnection(url, user, password);){
				LinkDaoMariaDBJdbcImpl dao = new LinkDaoMariaDBJdbcImpl();
				Link link = dao._getLink(cnx, 1);
				assertEquals("Documentation OCaml", link.getTitle());
		} catch(SQLException e) {
			fail("La connection a échouée : " + e.getLocalizedMessage());
		}
	}
}
