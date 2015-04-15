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

import com.satimetry.nudge.data.ProgramuserRepository;
import com.satimetry.nudge.model.Programuser;
import com.satimetry.nudge.service.ProgramuserCRUDService;

@Path("/programuser")
@RequestScoped
public class ProgramuserService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RulefileService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private ProgramuserRepository repository;

	@Inject
	private ProgramuserCRUDService crud;

	@GET
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProgramuserByProgramid(
		@QueryParam("programid") Integer programid) {

		Response.ResponseBuilder builder = null;
		Programuser Programuser = new Programuser();
		
		try {
			List<Programuser> Programusers = repository.findAllByProgramid(programid);
			Iterator it = Programusers.iterator();		
			while ( it.hasNext() ) {
				Programuser = (Programuser) it.next();
				System.out.println("id---->" + Programuser.getProgramuserid());
				crud.delete(Programuser.getProgramuserid());
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
		// return Programuser;		
	}

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProgramuser(
		@FormParam("programuserid") Integer programuserid) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("programuserid---->" + programuserid);
			crud.delete(programuserid);

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
		// return Programuser;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Programuser> listAllProgramusers() {
		return repository.findAllOrderedByProgramid();
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Programuser> lookupUserProgramusersByProgramid(
		@QueryParam("programid") Integer programid
		) {

		return repository.findAllByProgramid(programid);
	}
	
	@GET
	@Path("/{programuserid:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Programuser lookupProgramuserByProgramuserByProgramuserid(@PathParam("programuserid") Integer programuserid) {
		Programuser Programuser = repository.findByProgramuserid(programuserid);
		if (Programuser == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return Programuser;
	}

	/**
	 * Creates a new Programuser from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createProgramuserJSON(Programuser programuser) {
		
		LOGGER.info("-->Programuser " + programuser.getProgramid() + ":" + programuser.getUserid() + ":"+ programuser.getRoletype() );

		Response.ResponseBuilder builder = null;

		try {
			// Validates Programuser using bean validation
			validateProgramuser(programuser);

			crud.create(programuser);

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
		// return Programuser;
	}
	
	private void validateProgramuser(Programuser Programuser) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Programuser>> violations = validator.validate(Programuser);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

	}


}
