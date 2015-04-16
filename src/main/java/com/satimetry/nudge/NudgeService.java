
package com.satimetry.nudge;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.json.*;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.*;
import org.kie.api.time.SessionClock;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.drools.core.time.impl.*;

import com.satimetry.nudge.data.FactRepository;
import com.satimetry.nudge.data.RulefileRepository;
import com.satimetry.nudge.model.Fact;
import com.satimetry.nudge.rest.FactService;
import com.satimetry.nudge.service.FactCRUDService;
import com.satimetry.nudge.util.RandomString;
import com.satimetry.nudge.model.Rulefile;

public class NudgeService {

	/**
	 * SLF4J Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(NudgeService.class);
	
	@Inject
	private FactRepository factRepo;

	@Inject
	private RulefileRepository ruleRepo;

	@Inject
	private FactCRUDService crud;
	
	private static String RULES_FILE_NAME = "com/satimetry/nudge/data/rules.drl";	
	private JSONObject rulesjson = new JSONObject();

	public String doNudge(Integer programid, Integer groupid, String factname, String rulename) {

		java.util.List<JSONObject> systemfactjsonList = new ArrayList<JSONObject>();
		java.util.List<JSONObject> userfactjsonList = new ArrayList<JSONObject>();
		Fact fact = new Fact();		
		Rulefile rule = new Rulefile();
		
		// Fetch the rules to use
		List<Rulefile> ruleList = ruleRepo.findByRulename(programid, groupid, rulename);
		Iterator it = ruleList.iterator();		
		while ( it.hasNext() ) {
			rule = (Rulefile) it.next();
//			System.out.println("********************************");
//			System.out.println(rule.getRuletxt());
//			System.out.println("********************************");
			break;
		}

		// Find all the user input facts
		List<Fact> userfacts = factRepo.findAllUserFactsByProgramIdGroupidFactname(programid, groupid, factname);
		it = userfacts.iterator();		
		while ( it.hasNext() ) {
			fact = (Fact) it.next();
			userfactjsonList.add( new JSONObject(fact.getFactjson()) );
		}

		// Find and remove all the system generated facts
		List<Fact> systemfacts = factRepo.findAllSystemFactsByProgramIdGroupidFactname(programid, groupid, factname);
		it = systemfacts.iterator();		
		try {		
			while ( it.hasNext() ) {
				fact = (Fact) it.next();
				crud.delete(fact.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Reason over the user input facts using the rule
		try {
//			String rulesFile = "com/satimetry/nudge/data/rules.drl";
//			InputStream finput = Thread.currentThread().getContextClassLoader().getResourceAsStream(rulesFile);
//			String rules = readFileAsString(finput);
			String rules = rule.getRuletxt();
			
			KieSession sfSession = createStreamSession(rules);
			systemfactjsonList = runStreamRules(sfSession, userfactjsonList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Save the system generated facts
		try {
			it = systemfactjsonList.iterator();
			while ( it.hasNext() ) {
				fact = new Fact();
				fact.setFactjson( it.next().toString() );
				fact.setProgramid(programid);
				fact.setGroupid(groupid);
				fact.setFactname(factname);
				fact.setFacttype(new Integer(0));
				crud.create(fact);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return systemfactjsonList.toString();
	}
	
	public String createNudges(String id, String jsonInputString) {
		
		java.util.List<JSONObject> jsonInputList = new ArrayList<JSONObject>();
		java.util.List<JSONObject> jsonOutputList = new ArrayList<JSONObject>();
		JSONArray jsonInputArray = new JSONArray(jsonInputString);
		JSONObject json = new JSONObject();

		for (int i = 0; i < jsonInputArray.length(); i++ ) {
			json = jsonInputArray.getJSONObject(i);
			LOGGER.info("==>nudge/create jsonInputs=" + json);			
			jsonInputList.add(json);
		}

		try {
//			InputStream finput = Thread.currentThread().getContextClassLoader().getResourceAsStream(RULES_FILE_NAME);
			String rulesFile = "com/satimetry/nudge/data/rules.drl";
			InputStream finput = Thread.currentThread().getContextClassLoader().getResourceAsStream(rulesFile);
			String rules = readFileAsString(finput);
			
//			LOGGER.info("==>nudge/create rules=" + rulesjson);
//			String rules = rulesjson.get("stefano").toString();
			LOGGER.info("==>nudge/create +++++++++++++++++++++++++++++++++++++++++++");
			LOGGER.info("==>nudge/create rules=" + rules);
			LOGGER.info("==>nudge/create +++++++++++++++++++++++++++++++++++++++++++");
			
			KieSession sfSession = createStreamSession(rules);
			jsonOutputList = runStreamRules(sfSession, jsonInputList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonOutputList.toString();
	}

	public String putRules(String id, String rulesString) {
		
		JSONObject jsonOutput = new JSONObject();
		
		try {	    			
			rulesjson.put(id, rulesString); 
			PrintWriter writer = new PrintWriter(id + ".drl", "UTF-8");
			writer.print(rulesString);
			writer.close();
			// Better to write rules file to database
			// Then get file via id in getNudges
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LOGGER.info("==>nudge putRules=" + rulesjson);
		JSONObject json = new JSONObject();
		json.put(id, rulesjson.get(id));
		return json.toString();
	}
	
	public String createSession(String rulesString) {
		
		JSONObject jsonOutput = new JSONObject();
		
		try {	    			
			KieSession sfSession = createStreamSession(rulesString);
			jsonOutput.put("id", sfSession); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonOutput.toString();
	}
	
	protected KieSession createStreamSession(String rules) throws Exception {

		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();

//		kfs.write("/tmp/Rules.drl", rules);
//		kfs.write("src/main/resources/rules/Rules.drl", rules);
		LOGGER.info("==>nudge kie=" + rules);
		
		RandomString randomString = new RandomString(10);
		String ruleFileName = randomString.nextString();
		LOGGER.info("==>nudge/kie ruleFileName=" + ruleFileName);
		
		kfs.write("src/main/resources/" + ruleFileName + ".drl", rules);
//		kfs.write("src/main/resources/Rules.drl", rules);
//		kfs.write("src/main/java/com/satimetry/nudge/Rules.drl", rules);
		KieBuilder kb = ks.newKieBuilder(kfs);
		kb.buildAll(); 
		// kieModule is automatically deployed to KieRepository if successfully built.

		if (kb.getResults().hasMessages(Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
    	
    	KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		
    	KieBaseConfiguration kBaseConf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
    	kBaseConf.setOption( EventProcessingOption.STREAM );
    	KieBase kbase = kContainer.newKieBase(kBaseConf);

    	KieSessionConfiguration ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();    	
    	ksessionConfig.setOption( ClockTypeOption.get("realtime") );
    	KieSession sfSession = kbase.newKieSession(ksessionConfig, null);
    	
    	LOGGER.info("==>nudge/kie sfSession=" + sfSession);

//		sfSession.addEventListener( new DebugAgendaEventListener() );
//		sfSession.addEventListener( new DebugRuleRuntimeEventListener() );

		return sfSession;
	}

	protected List<JSONObject> runStreamRules(KieSession sfSession, List<JSONObject> jsonInputList ) throws Exception {

		java.util.List<JSONObject> jsonOutputList = new ArrayList<JSONObject>();
		
        SessionClock clock = sfSession.getSessionClock();        
        LOGGER.info("==>nudge/list clockc=" + clock.toString()  );
        LOGGER.info("==>nudge/list clockc=" + sfSession.getSessionClock()  );

        EntryPoint entryPoint = (EntryPoint) sfSession.getEntryPoint( "DEFAULT" );
        
		Iterator it = jsonInputList.iterator();
		JSONObject json = new JSONObject();
		while ( it.hasNext() ) {
			json = (JSONObject) it.next();
	        entryPoint.insert(json);
		}

		sfSession.fireAllRules();
        for ( FactHandle f : sfSession.getFactHandles() ) {
        	Object o = sfSession.getObject( f );
        	if ( o.getClass().toString().toLowerCase().contains("Output".toLowerCase()) ) {
    		    json = new JSONObject(o.toString());
    		    jsonOutputList.add(json);
        	}
        }
		sfSession.dispose();     
		
		return jsonOutputList;
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
