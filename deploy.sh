#!/bin/bash
 
### Openshift deploy script
### Add version as parameter

if [ $1 ]; then
  project=quarkus-test
  registry=default-route-openshift-image-registry.apps.poc-oc.poc.oc
  application=openshift-sap

  echo "Creating package " $1
    mvn clean package -DskipTests=true
    if [ $? != 0 ]; then
        echo "Building error"
        return
    fi

  echo "Podman Build"
    podman build -f src/main/docker/Dockerfile.jvm -t $project/$application .
    if [ $? != 0 ]; then
        echo "Podman build error"
        return
    fi

  echo "Local tag"
    podman tag $project/$application $registry/$project/$application:$1
    if [ $? != 0 ]; then
        echo "Local tag error"
        return
    fi

  echo "Pushing on Openshift using Podman"
    podman push $registry/$project/$application:$1 --tls-verify=false
    if [ $? != 0 ]; then
        echo "Podman push error..trying to login and retry"
        podman login -u admin -p $(oc whoami -t) $registry --tls-verify=false
        podman push $registry/$project/$application:$1 --tls-verify=false
        if [$? != 0 ]; then
                echo "Sometimes 'Podman push' fails, rexecuting"
                podman push $registry/$project/$application:$1 --tls-verify=false
            if [$? != 0 ]; then
                echo "Podman push error, trying to authenticate to Openshift cluster using 'oc login'"
                return
            fi
        fi
    fi

  echo "Openshift tag to allow "
    oc tag $project/$application:$1 $project/$application:latest
    if [ $? != 0 ]; then
        echo "oc tag error"
        return
    fi

else
  echo "Insert version as parameter"

fi