# Pochette

Little java web application to test Java EE + Mariadb + REST api + Ajax

* [Install Mariadb driver and test connection](#install-mariadb-driver-and-test-connection)
* [Create the DAL part](#create-the-dal-part)
* [Handle DAL exceptions](#handle-dal-exceptions)
* [Create a simple BLL](#create-a-simple-bll)
* [Configure the REST interface](#configure-the-rest-interface)
  * [Install the dependencies](#install-the-dependencies)
  * [Configure the REST path](#configure-the-rest-path)
  * [Create a resource class](#create-a-resource-class)
  * [Return xml answer](#return-xml-answer)
    * [Support the JAXB specification](#support-the-jaxb-specification)
    * [Link class serializable in xml](#link-class-serializable-in-xml)
    * [LinksManagement methods return Response objects](#linksmanagemenent-methods-return-response-objects)

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
				Statement stmt = conn.createStatement(
                //execute query
                ResultSet rs = stmt.executeQuery("SELECT 'Hello World!'");
                //position result to first
                rs.first();
                d>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-core</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
   <groupId>com.sun.xml.bind</groupId>
   <artifactId>jaxb-impl</artifactId>
   <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
</dependency>assertTrue("Hello World!".equals(rs.getString(1))){));
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

The database configuration is saved in the file *WebContent/META-INF/context.xml*.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context>
	<Resource
		name="jdbc/pool_cnx"

		driverClassName="org.mariadb.jdbc.Driver"
		type="javax.sql.DataSource"

		url="jdbc:mariadb://localhost/POCHETTE_DB"
		username="pochette_user"
		password="pochette_password"

		maxTotal="100"
		maxIdle="30"
		maxWaitMillis="10000"
	/>

</Context>
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

## Create a simple BLL

In a package *fr.pochette.bll* the following file is added:

* LinkManager.java

```java
import java.util.List;

import fr.pochette.bo.Link;
import fr.pochette.dal.DAOFactory;
import fr.pochette.dal.LinkDAO;
import fr.pochette.exception.BusinessException;

public class LinkManager {
	private LinkDAO linkDAO;

	public LinkManager() {
		this.linkDAO = DAOFactory.getLinkDAO();
	}

	public List<Link> listAll() throws BusinessException{
		return this.linkDAO.listAll();
	}

	public Link getLink(int id) throws BusinessException {
		return this.linkDAO.getLink(id);
	}
}
```

## Configure the REST interface

### Install the dependencies

Now the project will be converted to a Maven project and in the *pom.xml* file
the implementation of the JAX-RS 2.0 specification will be specified so that
Maven will be able to download the needed jars.

* Right click on the Project > Configure > convert to Maven Project.
    * Group Id : *Pochette*
    * Artifact Id : *Pochette*
    * Version : *0.0.1-SNAPSHOT*
    * Packaging : *war*
    * Pochette : *Pochette*

This will generate a pom.xml file in the root directory of the project. Now here
is the part to add in order to use the Apache CXF implementation.

```diff
diff --git a/pom.xml b/pom.xml
index 9b3e830..0eb5cc7 100644
--- a/pom.xml
+++ b/pom.xml
@@ -5,6 +5,18 @@
   <version>0.0.1-SNAPSHOT</version>
   <packaging>war</packaging>
   <name>Pochette</name>
+  <dependencies>
+       <dependency>
+       <groupId>org.apache.cxf</groupId>
+       <artifactId>cxf-rt-frontend-jaxrs</artifactId>
+       <version>3.1.6</version>
+       </dependency>
+        <dependency>
+       <groupId>org.apache.cxf</groupId>
+       <artifactId>cxf-rt-rs-http-sci</artifactId>
+       <version>3.1.6</version>
+       </dependency>
+  </dependencies>
   <build>
     <sourceDirectory>src</sourceDirectory>
     <resources>
```
If the JDK version used is >= 9 then it is necessary to add the following
dependencies ([source](https://stackoverflow.com/questions/46920461/java-lang-noclassdeffounderror-javax-activation-datasource-on-wsimport-intellij )):

```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-core</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
   <groupId>com.sun.xml.bind</groupId>
   <artifactId>jaxb-impl</artifactId>
   <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
</dependency>
```

All the dependencies will be downloaded after the following actions:
* right click on the project
* select Maven
* select Update Project

### Configure the REST path

In the package *fr.pochette.rest*, a new class is created:

```java
package fr.pochette.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class ConfigurationApplicationREST extends Application {

}
```

This class will be detected by Tomcat and will be used as a configuration for
the REST application.

### Create a resource class

Now that there is a class that defines the root path of the REST application,
I need to create a resource class that will manage the requests like GET, POST and so on
for the `Link` objects.

*LinksManagement*

```java
package fr.pochette.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import fr.pochette.bll.LinkManager;
import fr.pochette.bo.Link;
import fr.pochette.exception.BusinessException;

@Path("/links")
public class LinksManagement {

	@GET
	public String getLinks() {
		LinkManager linkManager = new LinkManager();
		String linkTitles = "";
		try {
			List<Link> links = linkManager.listAll();
			for(Link l : links) {
				linkTitles += l.getTitle() + "; " ;
			}
		} catch (BusinessException e) {
			linkTitles = "An error occured " + e.getLocalizedMessage();
		}
		return linkTitles;
	}
}
```

Now the connection with a browser to *http://localhost:8080/Pochette/rest/links*
returns :

```
Detecting use case of GADT with OCaml; Documentation OCaml; OCaml Mooc; Shahmen Poison; JJC De Mondoville Dominus Regnavit Mov. 4&5/6;
```

### Return xml answer

In order to be able to return xml anwsers, it is necessary to add support for the
JAXB specification, to make the `Link` class easily serializable in xml and to
make the `LinksManagement` methods return some usable Http responses.

#### Support for the JAXB specification

The JAXB APIs are considered to be Java EE APIs, and therefore are no longer contained on the default class path in Java SE 9. In Java 11 they are completely removed from the JDK.

ressources : [1](https://stackoverflow.com/questions/51564844/java-ee-rest-xml-support-javax-xml-bind-jaxbcontext-missing), [2](https://stackoverflow.com/questions/43574426/how-to-resolve-java-lang-noclassdeffounderror-javax-xml-bind-jaxbexception-in-j)

So if you have Java 6/7 or 8, nothing has to be done to have access to the JAXB api. For Java 9, you can modify the pom.xml file with the following maven dependencies.

```xml
<dependencies>
    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
        <version>2.3.0</version>
    </dependency>
    <dependency>
        <groupId>com.sun.xml.bind</groupId>
        <artifactId>jaxb-impl</artifactId>
        <version>2.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.glassfish.jaxb</groupId>
        <artifactId>jaxb-runtime</artifactId>
        <version>2.3.0</version>
    </dependency>
    <dependency>
        <groupId>javax.activation</groupId>
        <artifactId>activation</artifactId>
        <version>1.1.1</version>
    </dependency>
</dependencies>
```

If the Java version is equal to 10, the quick and dirty way is to add the following
command-line option : `--add-modules java.xml.bind`.

In eclipse this can be done via:

* Right click on the project
* Run as
* Run Configurations
* Go on the Arguments panel
* In the Vm arguments entry add with a space char ` --add-modules java.xml.bind`

#### Link class serializable in xml

The two important things to note are:

* the `@XmlRootElement` annotation: it defines the xml element that describes any *Link* object.
* the presence of an empty constructor that is needed.

##### *src/fr/pochette/bo/Link.java*

```diff
diff --git a/src/fr/pochette/bo/Link.java b/src/fr/pochette/bo/Link.java
index 6fc94d5..b8293ef 100644
--- a/src/fr/pochette/bo/Link.java
+++ b/src/fr/pochette/bo/Link.java
@@ -1,8 +1,14 @@
 package fr.pochette.bo;

 import java.time.LocalDate;

-public class Link {
+import javax.xml.bind.annotation.XmlRootElement;
+
+@XmlRootElement(name="link")
+public class Link {
                int idLink;
                String title;
                String url;
@@ -82,6 +88,12 @@ public class Link {
                public void setLinkType(LinkType linkType) {
                        this.linkType = linkType;
                }
+               /**
+                *
+                */
               public Link() {
+                       super();
+               }
                /**
                 * @param idLink
                 * @param title
@@ -99,7 +111,4 @@ public class Link {
                        this.setConsumed(consumed);
                        this.setLinkType(linkType);
                }
-
-
-
 }
```

#### LinksManagement methods return Response objects

Previously, what was returned was basic java String objects. Now the objects returned
 are Response objects form *javax.rw.rs.core.Response*. This kind of object allows
 to specify the status of the Http response, and the object to use to fill the
 response body.

 ```java
package fr.pochette.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericEntity;

import fr.pochette.bll.LinkManager;
import fr.pochette.bo.Link;
import fr.pochette.exception.BusinessException;

@Path("/links")
public class LinksManagement {

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getLinks() {
		LinkManager linkManager = new LinkManager();
		List<Link> links = null;
		try {
			links = linkManager.listAll();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		GenericEntity<List<Link>> resultat = new GenericEntity<List<Link>>(links) {};
		return Response
				.ok()
				.entity(resultat)
				.build();
	}

	@GET
	@Path("/{id : \\d+}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getLink(@PathParam("id") int id) {
		LinkManager linkManager = new LinkManager();
		Link link = null;
		try {
			link = linkManager.getLink(id);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return Response
				.ok()
				.entity(link)
				.build();
	}
}
```

Now every access to *http://localhost:8080/Pochette/rest/links* or *http://localhost:8080/Pochette/rest/links/1* will return an xml response:

```xml
<link>
  <consumed>false</consumed>
  <creationDate/>
  <idLink>1</idLink>
  <linkType>
    <idType>2</idType>
    <label>DOCUMENTATION</label>
  </linkType>
  <title>Documentation OCaml</title>
  <url>http://caml.inria.fr/pub/docs/manual-ocaml/</url>
</link>
```
