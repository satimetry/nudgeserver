package com.satimetry.nudge.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.satimetry.nudge.data.RulefileRepository;
import com.satimetry.nudge.model.Fact;
import com.satimetry.nudge.model.Rule;
import com.satimetry.nudge.model.Rulefile;
import com.satimetry.nudge.service.RulefileCRUDService;

@Path("/rulefile")
@RequestScoped
public class RulefileService {

	@Inject
	private Validator validator;

	@Inject
	private RulefileRepository repository;

	@Inject
	private RulefileCRUDService crud;

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteObservation(
			@FormParam("id") Integer id) {

		Response.ResponseBuilder builder = null;

		try {
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
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rulefile> listAllRuless() {
		return repository.findAllOrderedByProgramid();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Rulefile lookupRuleByRuleid(@PathParam("id") Integer id) {
		Rulefile rule = repository.findByRuleid(id);
		if (rule == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return rule;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rulefile> lookupRulefileByRulename(
		@QueryParam("programid") Integer programid,
		@QueryParam("groupid") Integer groupid,
		@QueryParam("rulename") String rulename) {
		return repository.findByRulename(programid, groupid, rulename);	
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createObservation(
		@FormParam("programid") Integer programid,
		@FormParam("groupid") Integer groupid,
		@FormParam("rulename") String rulename,
		@FormParam("ruletxt") String ruletxt
		) {

		rulename = rulename.replace("\"", "");

		System.out.println(ruletxt);
		
		Rulefile rule = new Rulefile();
		rule.setProgramid(programid);
		rule.setGroupid(groupid);
		rule.setRulename(rulename);
		rule.setRuletxt(ruletxt);
		
		Response.ResponseBuilder builder = null;

		Rulefile delrule = new Rulefile();
		try {
			
			// Remove same rule
			List<Rulefile> rules = repository.findByRulename(programid, groupid, rulename);
			Iterator it = rules.iterator();		
			while ( it.hasNext() ) {
				delrule = (Rulefile) it.next();
				System.out.println("del old id---->" + delrule.getId());
				crud.delete(delrule.getId());
			}
			
			// Validates observation using bean validation
			validateRule(rule);

			crud.create(rule);

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

	}

	private void validateRule(Rulefile rule) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Rulefile>> violations = validator.validate(rule);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

}

