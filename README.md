# Pochette

Little java web application to test Java EE + Mariadb + REST api + Ajax

* [Install Mariadb driver and test connection](#install-mariadb-driver-and-test-connection)
* [Create the DAL part](#create-the-dal-part)
* [Handle DAL exceptions](#handle-dal-exceptions)

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

## Create the DAL part

I only need for now to list all the links and see one link based on its id.

In the DAL package :
* DAOFactory.java

```java
package fr.pochette.dal;

public class DAOFactory {
	public static LinkDAO getLinkDAO() {
		return new LinkDaoMariaDBJdbcImpl();
	}
}
```

* LinkDAO.java

```java
package fr.pochette.dal;

import java.util.List;

import fr.pochette.bo.Link;

public interface LinkDAO {
	public List<Link> listAll() ;

	public Link getLink(int id);
}
```

* LinkDaoMariaDBJdbcImpl.java

```java
package fr.pochette.dal;

import java.util.List;

import fr.pochette.bo.Link;

public class LinkDaoMariaDBJdbcImpl implements LinkDAO {

	@Override
	public List<Link> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Link getLink(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
```

Add the code to generate the requests:

```java
public class LinkDaoMariaDBJdbcImpl implements LinkDAO {

    public static final String SELECT_ALL_LINKS =
        "SELECT idLink, title, url, creationDate, consumed, l.idType as l_idType, label FROM LINKS l"
        + " JOIN TYPES t ON l.idType = t.idType;";

    public static final String SELECT_LINK_BY_ID=
        "SELECT idLink, title, url, creationDate, consumed, l.idType as l_idType, label FROM LINKS l"
        + " JOIN TYPES t ON l.idType = t.idType WHERE idLink=?;";

    @Override
    public List<Link> listAll() {
        List<Link> links = null;
	try(Connection cnx = DBConnexionProvider.getConnection()){
            links = _listAll(cnx);
	} catch (SQLException e) {
            e.printStackTrace();
	}
	return links;
    }

    public List<Link> _listAll(Connection cnx) throws SQLException {
        List<Link> links = new ArrayList<Link>();
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(SELECT_ALL_LINKS);
        while(rs.next()) {
            Link link = _buildLink(rs);
            links.add(link);
        }
	st.close();
            return links;
    }

    @Override
    public Link getLink(int id) {
        Link link = null;
        try(Connection cnx = DBConnexionProvider.getConnection()) {
            link = _getLink(cnx, id);
        } catch(SQLException e) {
            e.printStackTrace();
        }
            return link;
	}

     public Link _getLink(Connection cnx, int id) throws SQLException {
        Link link = null;
        PreparedStatement pstmt = cnx.prepareStatement(SELECT_LINK_BY_ID);
        pstmt.setInt(1,  id);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            link = _buildLink(rs);
        }
        pstmt.close();
        return link;
    }

    private Link _buildLink(ResultSet rs) throws SQLException {
        LinkType linkType = new LinkType(rs.getInt("l_idType"), rs.getString("label"));
        Link link = new Link(rs.getInt("idLink"),
	                     rs.getString("title"),
			     rs.getString("url"),
                             rs.getDate("creationDate").toLocalDate(),
                             rs.getBoolean("consumed"),
                             linkType);
        return link;
    }
}
```

## Handle DAL exceptions:

In order to handle all the exceptions, there will be one generic exception that
is used to catch all the dal exceptions via user defined error codes. Those
error codes will be later translated in readable message thanks to the
`ErrorMessageReader` class coupled with the *error_messages.properties* file.

* GenericException.java

```java
package fr.pochette.exception;

import java.util.ArrayList;
import java.util.List;

public class GenericException extends Exception{

	private static final long serialVersionUID = 1L;

	private List<Integer> errorCodes;

	public GenericException(){
		this.errorCodes = new ArrayList<Integer>();
	}

	public void addError(int code) {
		this.errorCodes.add(code);
	}

	public List<Integer> getErrorCodes(){
		return this.errorCodes;
	}

	public boolean hasError() {
		return this.errorCodes.size() > 0;
	}
}

```

* ErrorMessageReader.java

```java
package fr.pochette.exception;

import java.util.ResourceBundle;

public class ErrorMessageReader {
    private static ResourceBundle rb;

    static {
        try {
            rb = ResourceBundle.getBundle("fr.pochette.exception.error_messages");
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static String getMessage(int code)
    {
        String message="";
        try
        {
            if(rb!=null)
            {
                message=rb.getString(String.valueOf(code));
    	} else {
                message="Something went wrong with the file that contains the error messages";
    	}
        } catch(Exception e) {
            e.printStackTrace();
            message="Unknown error";
        }

        return message;
    }
}
```


