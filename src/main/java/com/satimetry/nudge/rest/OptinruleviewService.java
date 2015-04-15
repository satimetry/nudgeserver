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

import com.satimetry.nudge.data.OptinruleviewRepository;
import com.satimetry.nudge.model.Optinruleview;

@Path("/optinruleview")
@RequestScoped
public class OptinruleviewService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RulefileService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private OptinruleviewRepository repository;

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Optinruleview> lookupUserOptinruleviewByProgramid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid
		) {

		return repository.findAllByProgramidUserid(programid, userid);
	}
	
	@GET
	@Path("/{programruleuserid:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optinruleview lookupOptinruleviewByProgramruleuserByProgramruleuserid(@PathParam("programruleuserid") Integer programruleuserid) {
		Optinruleview Programruleuser = repository.findByProgramruleuserid(programruleuserid);
		if (Programruleuser == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return Programruleuser;
	}

	/**
	 * Creates a new Programruleuser from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */


}
