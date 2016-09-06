# /etc/sysconfig/docker must contain insecure registry for openshift
oc cluster up --version=v1.3.0-alpha.2
oc new-project container-con

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
SOURCE_REPOSITORY_URL=https://github.com/josefkarasek/eap-rolling-update,\
SOURCE_REPOSITORY_REF=master,\
CONTEXT_DIR=greeter,\
DB_JNDI=java:jboss/datasources/GreeterQuickstartDS,\
DB_DATABASE=USERS,\
HTTPS_NAME=jboss,\
HTTPS_PASSWORD=mykeystorepass,\
JGROUPS_ENCRYPT_NAME=secret-key,\
JGROUPS_ENCRYPT_PASSWORD=password,\
IMAGE_STREAM_NAMESPACE=sample-project