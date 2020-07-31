package com.gpa.poc.rest;

import java.sql.Time;
import java.sql.Timestamp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gpa.poc.jco.JcoHandler;
import com.sap.conn.jco.JCoException;

@Path("/rest")
public class FunctionService {

    @Inject
    private EntityManager em;

    private JcoHandler jco = new JcoHandler();

    @GET
    @Path("/jcotest")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public String jcotest() throws JCoException {

        return jco.jcoTest(em);

    }
}