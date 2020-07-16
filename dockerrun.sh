#!/bin/bash
 
### Local run script

  project=quarkus-test
  application=openshift-sap

  echo "Creating package "
    mvn clean package -DskipTests=true
    if [ $? != 0 ]; then
        echo "Building error"
        return
    fi

  echo "Build with docker"
    docker build -f src/main/docker/Dockerfile.jvm -t $project/$application .
    if [ $? != 0 ]; then
        echo "DOCKER build error"
        return
    fi

  echo "Local execution"
    docker run -i --rm -p 8080:8080 $project/$application
    if [ $? != 0 ]; then
        echo "Local execution error"
        return
    fi
