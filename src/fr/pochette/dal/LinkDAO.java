package fr.pochette.dal;

import java.util.List;

import fr.pochette.bo.Link;

public interface LinkDAO {
	public List<Link> listAll() ;
	
	public Link getLink(int id);
}
