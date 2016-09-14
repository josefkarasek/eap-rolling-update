# Kubernetes Rolling Update Demo
This example app leverages the functionality of Kubernetes and OpenShift projects to run enterprise software in distributed environment.
Main goal is to demonstrate the application architecture - an application cluster that can be scaled and cluster nodes can survive restart.

```
# add --insecure-registry 172.30.0.0/16 to /etc/sysconfig/docker
oc cluster up --version=latest

# create git infrastructure
oc new-project git --display-name="GIT"
oc process -f https://raw.githubusercontent.com/josefkarasek/eap-rolling-update/master/gogs-postgresql.json | oc create -f -
oc deploy dc/postgresql-gogs --latest
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default

# create the app cluster
oc new-project cluster --display-name="CLUSTER"

# service account with secret
oc create -f https://raw.githubusercontent.com/jboss-openshift/application-templates/master/secrets/eap7-app-secret.json

# grant view rights on the project
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):eap7-service-account -n $(oc project -q)

# image streams
oc create -f https://raw.githubusercontent.com/josefkarasek/eap-rolling-update/master/eap70-is.json
oc create -f https://raw.githubusercontent.com/josefkarasek/eap-rolling-update/master/postgres-is.json

# template
oc create -f https://raw.githubusercontent.com/josefkarasek/eap-rolling-update/master/eap70-postgresql-demo-s2i.json

# build and deploy
oc new-app --template=eap70-postgresql-demo-s2i -p \
SOURCE_REPOSITORY_URL=http://gogs-git.10.40.3.24.xip.io/gogs/eap-rolling-update,\
SOURCE_REPOSITORY_REF=master,\
CONTEXT_DIR=greeter,\
DB_JNDI=java:jboss/datasources/GreeterQuickstartDS,\
DB_DATABASE=USERS,\
HTTPS_NAME=jboss,\
HTTPS_PASSWORD=mykeystorepass,\
JGROUPS_ENCRYPT_NAME=secret-key,\
JGROUPS_ENCRYPT_PASSWORD=password,\
IMAGE_STREAM_NAMESPACE=cluster
```
