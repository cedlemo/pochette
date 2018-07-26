package fr.pochette.bll;
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
