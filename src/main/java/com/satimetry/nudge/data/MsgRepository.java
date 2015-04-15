/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.satimetry.nudge.data;

import com.satimetry.nudge.model.Msg;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import java.util.List;

@ApplicationScoped
public class MsgRepository {

    @Inject
    private EntityManager em;

    public Msg findByMsgid(Integer Msgid) {
        return em.find(Msg.class, Msgid);
    }

    public List<Msg> findAllByProgramidUseridIsnotsent(Integer programid, Integer userid) {
    	
    	System.out.println("TNM-inputs--->" + programid + "-" + userid);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Msg> criteria = cb.createQuery(Msg.class);
        Root<Msg> Msg = criteria.from(Msg.class);
        
        Predicate condition1 = cb.equal(Msg.get("programid"), programid);
        Predicate condition2 = cb.equal(Msg.get("userid"), userid);
        Predicate condition3 = cb.equal(Msg.get("issent"), 0);

        Predicate condition4 = cb.and(condition1, condition2);
        Predicate condition = cb.and(condition3, condition4);
       		
        criteria.select(Msg).where(condition);        
        return em.createQuery(criteria).getResultList();
    }

    public List<Msg> findAllByProgramidUserid(Integer programid, Integer userid) {
    	
    	System.out.println("TNM-inputs--->" + programid + "-" + userid);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Msg> criteria = cb.createQuery(Msg.class);
        Root<Msg> Msg = criteria.from(Msg.class);
        
        Predicate condition1 = cb.equal(Msg.get("programid"), programid);
        Predicate condition2 = cb.equal(Msg.get("userid"), userid);
        Predicate condition = cb.and(condition1, condition2);
       		
        criteria.select(Msg).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
        
    public List<Msg> findAllByProgramIdUseridRulename(Integer programid, Integer userid, String rulename) {
    	
    	System.out.println("TNM-inputs--->" + programid + "-" + userid + "-" + rulename);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Msg> criteria = cb.createQuery(Msg.class);
        Root<Msg> Msg = criteria.from(Msg.class);

        Predicate condition1 = cb.equal(Msg.get("programid"), programid);
        Predicate condition2 = cb.equal(Msg.get("userid"), userid);
        Predicate condition3 = cb.equal(Msg.get("rulename"), rulename);
        Predicate condition4 = cb.and(condition1, condition2);
        Predicate condition  = cb.and(condition3, condition4);

        criteria.select(Msg).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
            
    public List<Msg> findAllOrderedByProgramid() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Msg> criteria = cb.createQuery(Msg.class);
        Root<Msg> Msg = criteria.from(Msg.class);

        criteria.select(Msg).orderBy(cb.asc(Msg.get("programid")));
        return em.createQuery(criteria).getResultList();
    }
    
}
