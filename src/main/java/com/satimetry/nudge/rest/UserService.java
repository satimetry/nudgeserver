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

import com.satimetry.nudge.data.UserRepository;
import com.satimetry.nudge.model.User;
import com.satimetry.nudge.service.UserCRUDService;

@Path("/user")
@RequestScoped
public class UserService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private UserRepository repository;

	@Inject
	private UserCRUDService crud;

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(
		@FormParam("userid") Integer userid) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("userid---->" + userid);
			crud.delete(userid);

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
		// return User;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> listAllUsers() {
		return repository.findAllOrderedByUsername();
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public User lookupUserUsersByProgramidUserid(
		@QueryParam("userid") Integer userid) {

		return repository.findByUserid(userid);
	}
	
	@GET
	@Path("/{userid:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public User lookupUserByUserByUserid(@PathParam("userid") Integer userid) {
		User User = repository.findByUserid(userid);
		if (User == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return User;
	}

	/**
	 * Creates a new User from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUserJSON(User user) {
		
		LOGGER.info("-->User " + user.getUserid() + ":"+ user.getUsername() );

		Response.ResponseBuilder builder = null;

		try {
			// Validates User using bean validation
			validateUser(user);

			crud.create(user);

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
		// return User;
	}
	
	private void validateUser(User User) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<User>> violations = validator.validate(User);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

	}


}
