# Pochette

Little java web application to test Java EE + Mariadb + REST api + Ajax

* [Install Mariadb driver and test connection](#install-mariadb-driver-and-test-connection)

## Install Mariadb driver and test the connection.

see https://mariadb.com/kb/en/library/getting-started-with-the-java-connector/#using-the-jar-file-directly for reference.

```bash
mkdir -p ./WebContent/WEB-INF/lib
cd WebContent/WEB-INF/lib
wget https://downloads.mariadb.com/Connectors/java/connector-java-2.2.6/mariadb-java-client-2.2.6.jar
```

Add this library in the build path and add the JUnit 5 library.

the tests

```java
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
```
