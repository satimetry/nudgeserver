/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package com.satimetry.nudge.service;

import com.satimetry.nudge.data.MsgRepository;
import com.satimetry.nudge.model.Msg;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class MsgCRUDService {

    @Inject
    private Logger log;
    
	@Inject
	private MsgRepository repository;
	
    @Inject
    private EntityManager em;

    @Inject
    private Event<Msg> msgEventSrc;

    public void update(Integer msgid) throws Exception {
    	Msg msg = new Msg();
    	msg = repository.findByMsgid(msgid);
    	msg.setIssent((byte) 1);
        em.persist(msg);
        msgEventSrc.fire(msg);
//        factEventSrc.fire(fact);
    }

    public void delete(Integer msgid) throws Exception {
    	Msg msg = new Msg();
    	msg = repository.findByMsgid(msgid);
        em.remove(msg);
//        factEventSrc.fire(fact);
    }
    
    public void create(Msg msg) throws Exception {
        em.persist(msg);
        msgEventSrc.fire(msg);
    }
    
}
