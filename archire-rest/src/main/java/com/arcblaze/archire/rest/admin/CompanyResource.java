package com.arcblaze.archire.rest.admin;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.arcblaze.archire.db.DaoFactory;
import com.arcblaze.archire.db.DatabaseException;
import com.arcblaze.archire.model.Company;
import com.arcblaze.archire.rest.BaseResource;
import com.codahale.metrics.Timer;

/**
 * The REST end-point for performing administrator actions on companies.
 */
@Path("/admin/company")
public class CompanyResource extends BaseResource {
	@Context
	private ServletContext servletContext;

	/**
	 * @param companyId
	 *            the unique id of the company to retrieve
	 * 
	 * @return the requested company
	 * 
	 * @throws DatabaseException
	 *             if there is an error communicating with the back-end
	 */
	@GET
	@Path("{companyId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Company one(@PathParam("companyId") Integer companyId)
			throws DatabaseException {
		try (Timer.Context timer = getTimer(this.servletContext,
				"/admin/company/{companyId}")) {
			return DaoFactory.getCompanyDao().get(companyId);
		}
	}

	/**
	 * @return all of the available companies
	 * 
	 * @throws DatabaseException
	 *             if there is an error communicating with the back-end
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Set<Company> all() throws DatabaseException {
		try (Timer.Context timer = getTimer(this.servletContext,
				"/admin/company")) {
			return DaoFactory.getCompanyDao().getAll();
		}
	}
}
