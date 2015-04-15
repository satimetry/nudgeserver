package com.satimetry.nudge.rest;

//import java.util.logging.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import org.slf4j.LoggerFactory;

import com.satimetry.nudge.data.FactRepository;
import com.satimetry.nudge.model.Fact;
import com.satimetry.nudge.service.FactCRUDService;

@Path("/fact")
@RequestScoped
public class FactService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FactService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private FactRepository repository;

	@Inject
	private FactCRUDService crud;

	@GET
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFactsByProgramidGroupid(
		@QueryParam("programid") Integer programid,
		@QueryParam("groupid") Integer groupid,
		@QueryParam("factname") String factname) {

		Response.ResponseBuilder builder = null;
		Fact fact = new Fact();
		
		try {
			List<Fact> facts = repository.findAllUserFactsByProgramIdGroupidFactname(programid, groupid, factname);
			Iterator it = facts.iterator();		
			while ( it.hasNext() ) {
				fact = (Fact) it.next();
				System.out.println("id---->" + fact.getId());
				crud.delete(fact.getId());
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
		// return fact;		
	}

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFact(
		@FormParam("id") Integer id) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("id---->" + id);
			crud.delete(id);

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
		// return fact;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Fact> listAllFacts() {
		return repository.findAllOrderedByProgramid();
	}

	@GET
	@Path("/system")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Fact> lookupSystemFactsByProgramidGroupid(
			@QueryParam("programid") Integer programid,
			@QueryParam("groupid") Integer groupid,
			@QueryParam("factname") String factname) {
		
		return repository.findAllSystemFactsByProgramIdGroupidFactname(programid, groupid, factname);

	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Fact> lookupUserFactsByProgramidGroupid(
		@QueryParam("programid") Integer programid,
		@QueryParam("groupid") Integer groupid,
		@QueryParam("factname") String factname) {
		
		return repository.findAllUserFactsByProgramIdGroupidFactname(programid, groupid, factname);

	}
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Fact lookupFactByFactByid(@PathParam("id") Integer id) {
		Fact Fact = repository.findByFactid(id);
		if (Fact == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return Fact;
	}

	/**
	 * Creates a new Fact from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFactByJSON(Fact fact) {
		
		Response.ResponseBuilder builder = null;

		System.out.println("-->POSTbyJSON Fact " + fact.getProgramid());
		
		try {
			// Validates fact using bean validation
			validateFact(fact);

			crud.create(fact);

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
		// return fact;

	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFact(
		@FormParam("programid") Integer programid,
		@FormParam("groupid") Integer groupid,
		@FormParam("factname") String factname,
		@FormParam("factjson") String factjson) {
		
		Fact fact = new Fact();
		fact.setGroupid(groupid);
		fact.setProgramid(programid);
		fact.setFactname(factname);
		fact.setFacttype(new Integer(1));
		fact.setFactjson(factjson);

		System.out.println("-->POST Fact " + fact.getProgramid());

		Response.ResponseBuilder builder = null;

		try {
			// Validates fact using bean validation
			validateFact(fact);

			crud.create(fact);

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
		// return fact;

	}

	private void validateFact(Fact fact) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Fact>> violations = validator.validate(fact);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

	}


}
