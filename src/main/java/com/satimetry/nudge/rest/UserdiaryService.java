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

import com.satimetry.nudge.data.UserdiaryRepository;
import com.satimetry.nudge.model.Userdiary;
import com.satimetry.nudge.service.UserdiaryCRUDService;

@Path("/userdiary")
@RequestScoped
public class UserdiaryService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserdiaryService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private UserdiaryRepository repository;

	@Inject
	private UserdiaryCRUDService crud;

	@GET
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUserdiaryByProgramidUserid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid) {

		Response.ResponseBuilder builder = null;
		Userdiary Userdiary = new Userdiary();
		
		try {
			List<Userdiary> Userdiarys = repository.findAllByProgramidUserid(programid, userid);
			Iterator it = Userdiarys.iterator();		
			while ( it.hasNext() ) {
				Userdiary = (Userdiary) it.next();
				System.out.println("id---->" + Userdiary.getUserdiaryid());
				crud.delete(Userdiary.getUserdiaryid());
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
		// return Userdiary;		
	}

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUserdiary(
		@FormParam("userdiaryid") Integer userdiaryid) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("userdiaryid---->" + userdiaryid);
			crud.delete(userdiaryid);

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
		// return Userdiary;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Userdiary> listAllUserdiarys() {
		return repository.findAllOrderedByProgramid();
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Userdiary> lookupUserUserdiarysByProgramidGroupid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid) {

		return repository.findAllByProgramidUserid(programid, userid);
	}
	
	@GET
	@Path("/{userdiaryid:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Userdiary lookupUserdiaryByUserdiaryByUserdiaryid(@PathParam("userdiaryid") Integer userdiaryid) {
		Userdiary Userdiary = repository.findByUserdiaryid(userdiaryid);
		if (Userdiary == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return Userdiary;
	}

	/**
	 * Creates a new Userdiary from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUserdiaryJSON(Userdiary userdiary) {
		
		LOGGER.info("-->Userdiary " + userdiary.getProgramid() + ":" + userdiary.getUserid() + ":"+ userdiary.getDiarylabel() + ":"+ userdiary.getDiarytxt() );

		Response.ResponseBuilder builder = null;

		try {
			// Validates Userdiary using bean validation
			validateUserdiary(userdiary);

			crud.create(userdiary);

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
		// return Userdiary;
	}
	
	private void validateUserdiary(Userdiary Userdiary) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Userdiary>> violations = validator.validate(Userdiary);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

	}


}
