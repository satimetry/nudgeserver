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

import com.satimetry.nudge.model.Optinruleview;

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
public class OptinruleviewRepository {

    @Inject
    private EntityManager em;

    public Optinruleview findByProgramruleuserid(Integer Programruleuserid) {
        return em.find(Optinruleview.class, Programruleuserid);
    }

    public List<Optinruleview> findAllByProgramidUserid(Integer programid, Integer userid) {
    	
    	System.out.println("optinruleview--->" + programid + "-" + userid);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Optinruleview> criteria = cb.createQuery(Optinruleview.class);
        Root<Optinruleview> Optinruleview = criteria.from(Optinruleview.class);
        
        Predicate condition1 = cb.equal(Optinruleview.get("programid"), programid);
        Predicate condition2 = cb.equal(Optinruleview.get("userid"), userid);
        Predicate condition  = cb.and(condition1, condition2);
        
        criteria.select(Optinruleview).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
    
}
