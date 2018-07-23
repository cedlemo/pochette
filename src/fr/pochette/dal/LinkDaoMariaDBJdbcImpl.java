package fr.pochette.dal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.pochette.bo.Link;
import fr.pochette.bo.LinkType;

public class LinkDaoMariaDBJdbcImpl implements LinkDAO {

	public static final String SELECT_ALL_LINKS =
			"SELECT idLink, title, url, creationDate, consumed, l.idType as l_idType, label FROM LINKS l"
			+ " JOIN TYPES t ON l.idType = t.idType;";
	
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
			LinkType linkType = new LinkType(rs.getInt("l_idType"), rs.getString("label"));
			Link link = new Link(rs.getInt("idLink"),
													 rs.getString("title"),
													 rs.getString("url"),
													 rs.getDate("creationDate").toLocalDate(),
													 rs.getBoolean("consumed"),
													 linkType);
			links.add(link);
		}
		return links;
	}
	
	@Override
	public Link getLink(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
