package com.arcblaze.archire.rest.admin;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.arcblaze.archire.db.DaoFactory;
import com.arcblaze.archire.db.DatabaseException;
import com.arcblaze.archire.model.Enrichment;
import com.arcblaze.archire.model.User;
import com.arcblaze.archire.rest.BaseResource;
import com.codahale.metrics.Timer;

/**
 * The REST end-point for managing users.
 */
@Path("/admin/user")
public class UserResource extends BaseResource {
	@Context
	private ServletContext servletContext;

	/**
	 * @param security
	 *            the security information associated with the request
	 * @param userId
	 *            the unique id of the user to retrieve
	 * @param enrichments
	 *            indicates the additional data to be included in the returned
	 *            user
	 * 
	 * @return the requested user (if in the same company as the current user)
	 * 
	 * @throws DatabaseException
	 *             if there is an error communicating with the back-end
	 */
	@GET
	@Path("{userId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User one(@Context SecurityContext security,
			@PathParam("userId") Integer userId,
			@QueryParam("enrichments") Set<Enrichment> enrichments)
			throws DatabaseException {
		try (Timer.Context timer = getTimer(this.servletContext,
				"/admin/user/{userId}")) {
			return DaoFactory.getUserDao().get(userId, enrichments);
		}
	}

	/**
	 * @param security
	 *            the security information associated with the request
	 * @param enrichments
	 *            indicates the additional data to be included in the returned
	 *            users
	 * 
	 * @return all of the available users in the same company as the current
	 *         user
	 * 
	 * @throws DatabaseException
	 *             if there is an error communicating with the back-end
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Set<User> all(@Context SecurityContext security,
			@QueryParam("enrichments") Set<Enrichment> enrichments)
			throws DatabaseException {
		try (Timer.Context timer = getTimer(this.servletContext, "/admin/user")) {
			return DaoFactory.getUserDao().getAll(enrichments);
		}
	}
}
