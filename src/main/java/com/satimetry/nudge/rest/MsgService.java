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

import com.satimetry.nudge.data.MsgRepository;
import com.satimetry.nudge.model.Msg;
import com.satimetry.nudge.service.MsgCRUDService;

@Path("/msg")
@RequestScoped
public class MsgService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MsgService.class);
	
	@Inject
	private Validator validator;

	@Inject
	private MsgRepository repository;

	@Inject
	private MsgCRUDService crud;

	@GET
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletemsgByProgramidUserid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid) {

		Response.ResponseBuilder builder = null;
		Msg msg = new Msg();
		
		try {
			List<Msg> msgs = repository.findAllByProgramidUserid(programid, userid);
			Iterator it = msgs.iterator();		
			while ( it.hasNext() ) {
				msg = (Msg) it.next();
				System.out.println("id---->" + msg.getMsgid());
				crud.delete(msg.getMsgid());
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
		// return msg;		
	}

	@GET
	@Path("/issent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatemsg(
		@QueryParam("msgid") Integer msgid) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("msgid---->" + msgid);
			crud.update(msgid);

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
		// return msg;
	}

	@POST
	@Path("/del")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletemsg(
		@FormParam("msgid") Integer msgid) {

		Response.ResponseBuilder builder = null;

		try {
			System.out.println("msgid---->" + msgid);
			crud.delete(msgid);

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
		// return msg;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Msg> listAllmsgs() {
		return repository.findAllOrderedByProgramid();
	}

	@GET
	@Path("/isnotsent")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Msg> lookupUsermsgsByProgramidUseridIsnotsent(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid) {

		return repository.findAllByProgramidUseridIsnotsent(programid, userid);
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Msg> lookupUsermsgsByProgramidGroupid(
		@QueryParam("programid") Integer programid,
		@QueryParam("userid") Integer userid) {

		return repository.findAllByProgramidUserid(programid, userid);
	}
	
	@GET
	@Path("/{msgid:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Msg lookupmsgBymsgBymsgid(@PathParam("msgid") Integer msgid) {
		Msg msg = repository.findByMsgid(msgid);
		if (msg == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return msg;
	}

	/**
	 * Creates a new msg from the values provided.  Performs validation, and will return a JAX-RS response with either
	 * 200 ok, or with a map of fields, and related errors.
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createmsgJSON(Msg msg) {
		
		LOGGER.info("-->msg " + msg.getProgramid() + ":" + msg.getUserid() + ":"+ msg.getRulename() + ":" + msg.getRuledate() + ":" + msg.getMsgtxt());

		Response.ResponseBuilder builder = null;

		try {
			// Validates msg using bean validation
			validateMsg(msg);

			crud.create(msg);

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
		// return msg;
	}
	
	private void validateMsg(Msg msg) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Msg>> violations = validator.validate(msg);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

	}


}
