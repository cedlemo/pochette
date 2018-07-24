package fr.pochette.dal;

import java.util.List;

import fr.pochette.bo.Link;
import fr.pochette.exception.GenericException;

public interface LinkDAO {
	public List<Link> listAll() throws GenericException ;
	
	public Link getLink(int id) throws GenericException;
}
