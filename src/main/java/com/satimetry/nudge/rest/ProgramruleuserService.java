package com.satimetry.nudge.rest;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import java.util.logging.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.satimetry.nudge.data.ProgramruleuserRepository;
import com.satimetry.nudge.model.Programruleuser;
import com.satimetry.nudge.service.ProgramruleuserCRUDService;

@Path("/programruleuser")
@RequestScoped
public class ProgramruleuserService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramruleuserService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private ProgramruleuserRepository repository;

	@Inject
	private ProgramruleuserCRUDService crud;

	@GET
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProgramruleuserByProgramid(
		@QueryParam("programid") Integer programid) {

		Response.ResponseBuilder builder = null;
		Programruleuser Programruleuser = new Programruleuser();
		
		try {
			List<Programruleuser> Programruleusers = repository.findAllByProgramid(programid);
			Iterator it = Programruleusers.iterator();		
			while ( it.hasNext() ) {
				Programruleuser = (Programruleuser) it.next();
				System.out.println("id---->" + Programruleuser.getProgramruleuserid());
				crud.delete(Programruleuser.getProgramruleuserid());
			}
			//Create an "ok" response
			builder = Response.ok();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			System.out.println(e);
		}

		return builder.build();
		// return Programruleuser;		
	}

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProgramruleuser(
		@FormParam("programruleuserid") Integer programruleuserid) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("programruleuserid---->" + programruleuserid);
			crud.delete(programruleuserid);

			//Create an "ok" response
			builder = Response.ok();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			System.out.println(e);
		}

		return builder.build();
		// return Programruleuser;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Programruleuser> listAllProgramruleusers() {
		return repository.findAllOrderedByProgramid();
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Programruleuser> lookupUserProgramruleusersByProgramid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid
		) {

		return repository.findAllByProgramidUserid(programid, userid);
	}
	
	@GET
	@Path("/{programruleuserid:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Programruleuser lookupProgramruleuserByProgramruleuserByProgramruleuserid(@PathParam("programruleuserid") Integer programruleuserid) {
		Programruleuser Programruleuser = repository.findByProgramruleuserid(programruleuserid);
		if (Programruleuser == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return Programruleuser;
	}

	/**
	 * Creates a new Programruleuser from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createProgramruleuserJSON(Programruleuser programruleuser) {
		
		LOGGER.info("-->Programruleuser " + programruleuser.getProgramid() + ":" + programruleuser.getUserid() );

		Response.ResponseBuilder builder = null;

		try {
			// Validates Programruleuser using bean validation
			validateProgramruleuser(programruleuser);

			crud.create(programruleuser);

			//Create an "ok" response
			builder = Response.ok();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			System.out.println(e);
		}

		return builder.build();
		// return Programruleuser;
	}
	
	private void validateProgramruleuser(Programruleuser Programruleuser) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Programruleuser>> violations = validator.validate(Programruleuser);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

	}


}
