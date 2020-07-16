package com.gpa.poc.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gpa.poc.jco.JcoHandler;
import com.sap.conn.jco.JCoException;

@Path("/rest")
public class FunctionService {

    private JcoHandler jco = new JcoHandler();

    @GET
    @Path("/jcotest")
    @Produces(MediaType.TEXT_PLAIN)
    public String jcotest() throws JCoException {
        System.out.println("here to serve");
        return jco.jcoTest();
    }
}