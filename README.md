# Quarkus-sap project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

The application allows connection to Sap from an application deployed on an Openshift Cluster



## To configure the application do the following:

- on JcoHandler.java file
    - modify sap connection properties 

## To run locally do the following:
- on dockerrun.sh file modify the variables
    - 'project': referencing the project on Openshift you want to deploy the application
    - 'application': referencing the application name
Run dockerrun.sh

## To deploy on Openshift modify the following files
- on deploy.sh file modify the variables
    - 'project': referencing the project on Openshift you want to deploy the application
    - 'application': referencing the application name
    - 'registry': referencing the internal registry of the openshift cluster 
   
    - on Deployment.yaml file
        - <app_name>: indicating application name
        - <project_name>: indicating project name
        - < sha256 >: indicating the sha of the latest image uploaded pointed from imageStream resource
            (example:b7233352238b2bcbb699b26e3b57e2946ad793b93d07afa8398db372c19744t2
       
    ## Then run
    'oc login' 
    'oc new-project' to create a new project or move to an existing project  
    Launch deploy.sh script specifing a version ( eg.: . delpoy.sh 1.0)

    ## Only the first time
    Create a Deployment resource on Openshift with the Deploymeny.yaml content

## For new updates launch deploy.sh script again specifying the new version

## Updated: added Database connection.
- On calling /rest/jcotest api, the server will connect to Sap and then will save an access log into Access table.
  We connected to Postgresql, database available as a template on Openshift Cluster and connect to it using postgresql svc name, by setting the appropriate jdbc string in application.properties

