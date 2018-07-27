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