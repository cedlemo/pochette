package fr.pochette.dal;

public class DAOFactory {
	public static LinkDAO getLinkDAO() {
		return new LinkDaoMariaDBJdbcImpl();
	}
}
