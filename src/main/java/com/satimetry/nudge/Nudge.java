package com.satimetry.nudge;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.drools.core.reteoo.ObjectTypeNode.Id;
import org.json.*;

import com.satimetry.nudge.data.FactRepository;
import com.satimetry.nudge.data.RuleRepository;
import com.satimetry.nudge.model.Fact;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/nudge")
public class Nudge {

	@Inject
	private FactRepository factRepo;

	@Inject
	private RuleRepository ruleRepo;

	private static String RULES_FILE_NAME = "com/satimetry/nudge/data/rules.drl";	

	@Inject
	NudgeService nudgeService;

	@GET
	@Produces({ "application/json" })	
	public String doNudge(	
		@QueryParam("programid") Integer programid,
		@QueryParam("groupid") Integer groupid,
		@QueryParam("factname") String factname,
		@QueryParam("rulename") String rulename) {						
		rulename = rulename.replace("\"", "");
		System.out.println("TNM-rulename--->" + rulename);				
		return nudgeService.doNudge(programid, groupid, factname, rulename);
	}

	@GET
	@Path("/getnudge")
	@Produces({ "application/json" })	
	public String getNudge(	
		@QueryParam("id") String id, 
		@QueryParam("obs") String jsonInputString ) {
		
		return nudgeService.createNudges(id, jsonInputString);
	}

	@GET
	@Path("/getsession")
	@Produces({ "application/json" })	
	public String getSession( @DefaultValue("com/satimetry/nudge/data/rules.drl") @QueryParam("rules") String rulesString ) {

		try {
			InputStream finput = Thread.currentThread().getContextClassLoader().getResourceAsStream(RULES_FILE_NAME);
			rulesString = readFileAsString(finput);			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nudgeService.createSession(rulesString);
	}

	@GET
	@Path("/putrules")
	@Produces({ "application/json" })		
	public String putRules( @QueryParam("id") String id, @QueryParam("rules") String rulesString ) {

		try {
			String rulesFile = "com/satimetry/nudge/data/rules.drl";
			InputStream finput = Thread.currentThread().getContextClassLoader().getResourceAsStream(rulesFile);
			rulesString = readFileAsString(finput);			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nudgeService.putRules(id, rulesString);
	}
	
	public String readFileAsString(InputStream inputStream) throws java.io.IOException {

		InputStreamReader inRead = new InputStreamReader(inputStream);
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(inRead);
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		return fileData.toString();
	}	
	
	
}
