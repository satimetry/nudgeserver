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

import com.satimetry.nudge.model.Rule;

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
public class RuleRepository {

    @Inject
    private EntityManager em;

    public Rule findByRuleid(Integer Ruleid) {
        return em.find(Rule.class, Ruleid);
    }

    public List<Rule> findByRulename(String rulename) {
    	
    	System.out.println("TNM-inputs--->" + rulename);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rule> criteria = cb.createQuery(Rule.class);
        Root<Rule> Rule = criteria.from(Rule.class);
        
        Predicate condition = cb.equal(Rule.get("rulename"), rulename);
       		
        criteria.select(Rule).where(condition);        
        return em.createQuery(criteria).getResultList();
    }
                    
    public List<Rule> findAllOrderedByRulename() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rule> criteria = cb.createQuery(Rule.class);
        Root<Rule> Rule = criteria.from(Rule.class);

        criteria.select(Rule).orderBy(cb.asc(Rule.get("rulename")));
        return em.createQuery(criteria).getResultList();
    }
    
}
