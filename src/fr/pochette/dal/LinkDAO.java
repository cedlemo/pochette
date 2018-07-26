package fr.pochette.dal;

import java.util.List;

import fr.pochette.bo.Link;
import fr.pochette.exception.BusinessException;

public interface LinkDAO {
	public List<Link> listAll() throws BusinessException ;
	
	public Link getLink(int id) throws BusinessException;
}
