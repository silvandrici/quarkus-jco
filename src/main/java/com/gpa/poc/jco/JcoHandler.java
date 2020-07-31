package com.gpa.poc.jco;

import java.util.*;

import java.sql.Timestamp;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.gpa.poc.db.DbHandler;
import com.gpa.poc.model.Access;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class JcoHandler{

	DbHandler db;
	private DbHandler getDb(){
		if (db == null){
			db =  new DbHandler();
		}
		return db;
	}

    // sap connection properties

    /*Injecting from application.properties in this way isn't working in a container 
    @ConfigProperty(name = "jco.host")
	String host;
	@ConfigProperty(name = "jco.sysnr")
	String sysnr;
	@ConfigProperty(name = "jco.client")
	String client;
	@ConfigProperty(name = "jco.user")
	String user;
	@ConfigProperty(name = "jco.passsword")
	String passsword;
	@ConfigProperty(name = "jco.language")
    String language; */

	String host = "";
	String sysnr = "";
	String client = "";
	String user = "";
	String passsword = "";
    String language = "";

    JCoDestination destination;
    String RFC_DESTINATION_NAME = "ABAP_SYSTEM2";
	
    //RFC to call
    private final String STFC_CONNECTION= "STFC_CONNECTION";

    public String test(){
        return "test";
    }
    
    public String jcoTest(EntityManager em) throws JCoException {
        DbHandler db = getDb();
    	JCoDestination destination;
		try{
			destination = JCoDestinationManager.getDestination(RFC_DESTINATION_NAME);
		}catch(Exception e){
	        createDestination();
			destination = JCoDestinationManager.getDestination(RFC_DESTINATION_NAME);
		}
		
        JCoFunction function = destination.getRepository().getFunction(STFC_CONNECTION);
        if (function == null)
            throw new RuntimeException(STFC_CONNECTION + " not found in SAP.");

        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e);
        }

        //LOG ACCESS
        String toReturn = function.getExportParameterList().getString("RESPTEXT");
        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));

        //SAVE ACCESS RECORD ON DB
        Timestamp sqlTime = new Timestamp(System.currentTimeMillis());
        Access access = new Access();
        access.setTime(sqlTime);
        access.setInfo(toReturn);
        db.persistAccess(em, access);

        return toReturn;
    }

    private void createDestination() {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, host);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, sysnr);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, client);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, user);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, passsword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, language);
        createDataFile(RFC_DESTINATION_NAME, "jcoDestination", connectProperties);

    }


    private void createDataFile(String name, String suffix, Properties properties) {

        File cfg = new File(name + "." + suffix);
        if (!cfg.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(cfg, false);
                properties.store(fos, "for tests only !");
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);
            }
        }
    }
}