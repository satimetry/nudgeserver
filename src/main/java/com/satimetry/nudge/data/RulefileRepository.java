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

import com.satimetry.nudge.model.Rulefile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

@ApplicationScoped
public class RulefileRepository {

    @Inject
    private EntityManager em;

    public Rulefile findByRuleid(Integer id) {
        return em.find(Rulefile.class, id);
    }

    public List<Rulefile> findByRulename(Integer programid, Integer groupid, String rulename) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rulefile> criteria = cb.createQuery(Rulefile.class);
        Root<Rulefile> Rule = criteria.from(Rulefile.class);
 
        Predicate condition1 = cb.equal(Rule.get("programid"), programid);
        Predicate condition2 = cb.equal(Rule.get("groupid"), groupid);
        Predicate condition3 = cb.equal(Rule.get("rulename"), rulename);
        Predicate condition4 = cb.and(condition1, condition2);
        Predicate condition  = cb.and(condition3, condition4);
       		
        criteria.select(Rule).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Rulefile> findAllByProgramIdGroupidRulename(Integer programid, Integer groupid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rulefile> criteria = cb.createQuery(Rulefile.class);
        Root<Rulefile> Rule = criteria.from(Rulefile.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Rule).where(cb.equal(Rule.get(Rule_.name), email));
        criteria.select(Rule).where(cb.equal(Rule.get("programid"), programid));
        criteria.select(Rule).where(cb.equal(Rule.get("groupid"), groupid));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Rulefile> findAllOrderedByGroupid() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rulefile> criteria = cb.createQuery(Rulefile.class);
        Root<Rulefile> Rule = criteria.from(Rulefile.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Rule).orderBy(cb.asc(Rule.get(Rule_.name)));
        criteria.select(Rule).orderBy(cb.asc(Rule.get("groupid")));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Rulefile> findAllOrderedByProgramid() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rulefile> criteria = cb.createQuery(Rulefile.class);
        Root<Rulefile> Rule = criteria.from(Rulefile.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Rule).orderBy(cb.asc(Rule.get(Rule_.name)));
        criteria.select(Rule).orderBy(cb.asc(Rule.get("programid")));
        return em.createQuery(criteria).getResultList();
    }
    
    
}
