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