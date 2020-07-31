package com.gpa.poc.db;

import com.gpa.poc.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class DbHandler {

    @Transactional
    @SuppressWarnings("unchecked")
    public void persistAccess(EntityManager em, Access access) {
            try {
                em.persist(access);
            } catch (Exception e) {
                System.out.println("Cannot save the access. Error: " + e);
            }
    } 
}