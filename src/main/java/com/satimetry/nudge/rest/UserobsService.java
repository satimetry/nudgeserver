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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.satimetry.nudge.data.UserobsRepository;
import com.satimetry.nudge.model.Userobs;
import com.satimetry.nudge.service.UserobsCRUDService;

@Path("/userobs")
@RequestScoped
public class UserobsService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserobsService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private UserobsRepository repository;

	@Inject
	private UserobsCRUDService crud;
	
	@GET
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUserobsByProgramidUserid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid,
		@QueryParam("obsname") String obsname) {

		Response.ResponseBuilder builder = null;
		Userobs Userobs = new Userobs();
		
		try {
			List<Userobs> Userobss = repository.findAllByProgramidUseridObsname(programid, userid, obsname);
			Iterator it = Userobss.iterator();		
			while ( it.hasNext() ) {
				Userobs = (Userobs) it.next();
				System.out.println("id---->" + Userobs.getUserobsid());
				crud.delete(Userobs.getUserobsid());
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
		// return Userobs;		
	}

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUserobs(
		@FormParam("userobsid") Integer userobsid) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("userobsid---->" + userobsid);
			crud.delete(userobsid);

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
		// return Userobs;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Userobs> listAllUserobss() {
		return repository.findAllOrderedByProgramid();
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Userobs> lookupUserUserobssByProgramidGroupid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid,		
		@QueryParam("obsname") String obsname) {

		return repository.findAllByProgramidUseridObsname(programid, userid, obsname);
	}
	
	@GET
	@Path("/{userobsid:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Userobs lookupUserobsByUserobsByUserobsid(@PathParam("userobsid") Integer userobsid) {
		Userobs Userobs = repository.findByUserobsid(userobsid);
		if (Userobs == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return Userobs;
	}

	/**
	 * Creates a new Userobs from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUserobsJSON(Userobs userobs) {
		
		LOGGER.info("-->POSTbyJSON Userobs " + userobs);		
		LOGGER.info("-->Userobs JSON " + userobs.getProgramid() + ":" + userobs.getUserid() + ":"+ userobs.getObsname() + ":" + userobs.getObsvalue() + ":" + userobs.getObsdate());

		Response.ResponseBuilder builder = null;
	    
		try {
			// Validates Userobs using bean validation
			validateUserobs(userobs);

			crud.create(userobs);

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
		// return Userobs;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	public Response createUserobs(
			@FormParam("programid") Integer programid,
			@FormParam("userid") Integer userid,
			@FormParam("obsname") String obsname,
			@FormParam("obsvalue") String obsvalue,
			@FormParam("obsdesc") String obsdesc,
			@FormParam("obsdate") String obsdatestr,
			@FormParam("obstype") String obstype
			) {

		java.util.Date date = new Date(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = (java.util.Date) sdf.parse(obsdatestr);  
		} catch (Exception e) {}
		Timestamp obsdate = new Timestamp(date.getTime());
		
		System.out.println("-->POSTbyFORM Userobs " + programid);
		LOGGER.info("-->Userobs FORM " + programid + ":" + userid + ":"+ obsname + ":" + obsvalue + ":" + obsdate);
		
		Userobs Userobs = new Userobs();
		Userobs.setUserid(userid);
		Userobs.setProgramid(programid);
		Userobs.setObsname(obsname);
		Userobs.setObsvalue(obsvalue);
		Userobs.setObsdesc(obsdesc);
		Userobs.setObsdate(obsdate);
		Userobs.setObstype(obstype);

		Response.ResponseBuilder builder = null;

		try {
			// Validates Userobs using bean validation
			validateUserobs(Userobs);

			crud.create(Userobs);

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
		// return Userobs;

	}

	private void validateUserobs(Userobs Userobs) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Userobs>> violations = validator.validate(Userobs);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

	}


}
