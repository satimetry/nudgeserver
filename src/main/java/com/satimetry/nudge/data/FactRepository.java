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

import com.satimetry.nudge.model.Fact;

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
public class FactRepository {

    @Inject
    private EntityManager em;

    public Fact findByFactid(Integer id) {
        return em.find(Fact.class, id);
    }

    public List<Fact> findAllSystemFactsByProgramIdGroupidFactname(Integer programid, Integer groupid, String factname) {
    	
    	System.out.println("TNM-fact--->" + programid + "-" + groupid);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fact> criteria = cb.createQuery(Fact.class);
        Root<Fact> Fact = criteria.from(Fact.class);
        
        Predicate condition1 = cb.equal(Fact.get("programid"), programid);
        Predicate condition2 = cb.equal(Fact.get("groupid"), groupid);
        Predicate condition3 = cb.equal(Fact.get("factname"), factname);
        Predicate condition4 = cb.equal(Fact.get("facttype"), 0);

        Predicate condition5 = cb.and(condition1, condition2);
        Predicate condition6 = cb.and(condition5, condition3);

        Predicate condition  = cb.and(condition6, condition4);       		
        criteria.select(Fact).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Fact> findAllUserFactsByProgramIdGroupidFactname(Integer programid, Integer groupid, String factname) {
    	
    	System.out.println("TNM-fact--->" + programid + "-" + groupid);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fact> criteria = cb.createQuery(Fact.class);
        Root<Fact> Fact = criteria.from(Fact.class);

        Predicate condition1 = cb.equal(Fact.get("programid"), programid);
        Predicate condition2 = cb.equal(Fact.get("groupid"), groupid);
        Predicate condition3 = cb.equal(Fact.get("factname"), factname);
        Predicate condition4 = cb.equal(Fact.get("facttype"), 1);
        // condition3 to be replaced by gt once syntax is figured out       
        // Predicate condition3 = cb.gt(Fact.get(Fact_.facttype)), 0);
        Predicate condition5 = cb.and(condition1, condition2);
        Predicate condition6 = cb.and(condition5, condition3);
        Predicate condition  = cb.and(condition6, condition4);
       		
        criteria.select(Fact).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Fact> findAllByProgramIdgroupidFacttype(Integer programid, Integer groupid, Integer facttype) {
    	
    	System.out.println("TNM-fact--->" + programid + "-" + groupid + "-" + facttype);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fact> criteria = cb.createQuery(Fact.class);
        Root<Fact> Fact = criteria.from(Fact.class);

        Predicate condition1 = cb.equal(Fact.get("programid"), programid);
        Predicate condition2 = cb.equal(Fact.get("groupid"), groupid);
        Predicate condition3 = cb.equal(Fact.get("facttype"), facttype);
        Predicate condition4 = cb.and(condition1, condition2);
        Predicate condition  = cb.and(condition3, condition4);

        criteria.select(Fact).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Fact> findAllByProgramIdGroupid(Integer programid, Integer groupid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fact> criteria = cb.createQuery(Fact.class);
        Root<Fact> Fact = criteria.from(Fact.class);

        Predicate condition1 = cb.equal(Fact.get("programid"), programid);
        Predicate condition2 = cb.equal(Fact.get("groupid"), groupid);
        Predicate condition  = cb.and(condition1, condition2);

        criteria.select(Fact).where(condition);
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Fact> findAllOrderedByGroupid() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fact> criteria = cb.createQuery(Fact.class);
        Root<Fact> Fact = criteria.from(Fact.class);

        criteria.select(Fact).orderBy(cb.asc(Fact.get("groupid")));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Fact> findAllOrderedByProgramid() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fact> criteria = cb.createQuery(Fact.class);
        Root<Fact> Fact = criteria.from(Fact.class);

        criteria.select(Fact).orderBy(cb.asc(Fact.get("programid")));
        return em.createQuery(criteria).getResultList();
    }
    
    
}
