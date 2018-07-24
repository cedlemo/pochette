package fr.pochette.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.pochette.bo.Link;
import fr.pochette.bo.LinkType;
import fr.pochette.exception.GenericException;

public class LinkDaoMariaDBJdbcImpl implements LinkDAO {

	public static final String SELECT_ALL_LINKS =
			"SELECT idLink, title, url, creationDate, consumed, l.idType as l_idType, label FROM LINKS l"
			+ " JOIN TYPES t ON l.idType = t.idType;";
	
	public static final String SELECT_LINK_BY_ID=
			"SELECT idLink, title, url, creationDate, consumed, l.idType as l_idType, label FROM LINKS l"
					+ " JOIN TYPES t ON l.idType = t.idType WHERE idLink=?;";
	
	@Override
	public List<Link> listAll() throws GenericException {
		List<Link> links = null;
		try(Connection cnx = DBConnexionProvider.getConnection()){
			links = _listAll(cnx);
		} catch (SQLException e) {
			e.printStackTrace();
			GenericException ge = new GenericException();
			ge.addError(ErrorCodesDal.LIST_ALL_LINKS);
			throw ge;
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
	public Link getLink(int id) throws GenericException {
		Link link = null;
		try(Connection cnx = DBConnexionProvider.getConnection()) {
			link = _getLink(cnx, id);
		} catch(SQLException e) {
			e.printStackTrace();
			GenericException ge = new GenericException();
			ge.addError(ErrorCodesDal.SELECT_LINK_BY_ID);
			throw ge;
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
