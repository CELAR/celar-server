#!/bin/bash

RANDOM_PASSWORD=$(/bin/cat /dev/urandom | tr -dc 0-9a-zA-Z | head -c 16)
CELAR_SERVER_HOME=/opt/celar/celar-server
KEYSTORE_PATH=$CELAR_SERVER_HOME/keystore.jks
CONF_FILE=$CELAR_SERVER_HOME/conf/celar-server.properties

create_keystore(){
/usr/bin/keytool -genkey \
-keyalg RSA \
-alias celar-server \
-keypass $RANDOM_PASSWORD \
-storepass $RANDOM_PASSWORD \
-keystore $KEYSTORE_PATH \
-validity 730 << EOF
Giannis Giannakopoulos
ATHENA
CELAR
Europe
Europe
EU
yes
EOF
}

configure_server(){
/bin/sed -i 's|# server.ssl.keystore.path = |server.ssl.keystore.path = '$KEYSTORE_PATH'|' $CONF_FILE;
/bin/sed -i "s/# server.ssl.keystore.password = /server.ssl.keystore.password = $RANDOM_PASSWORD/" $CONF_FILE
/bin/sed -i "s/# server.ssl.port = 8443/server.ssl.port = 8443/" $CONF_FILE
}

create_service(){
	/bin/ln -sv $CELAR_SERVER_HOME/bin/celar-server /etc/init.d/
}
create_keystore;

configure_server;

create_service

/bin/rm $CELAR_SERVER_HOME/lib/slf4j-jdk14-1.4.2.jar

service celar-server start;


############################################ DATABASE   

user=celaruser
password=celar-user


#trust local users
sed -i "s/local   all         all                               ident/local   all         all                               trust/g" -i /var/lib/pgsql/data/pg_hba.conf
sed -i "s/host    all         all         ::1\/128               ident/host    all         all         ::1\/128               trust/g" -i /var/lib/pgsql/data/pg_hba.conf

#restart to apply changes
service postgresql restart

service postgresql initdb &>/dev/null

#create user
echo "create user $user password '$password';" | psql -U postgres &>/dev/null
# drop and re-create the DB
echo "DROP DATABASE celardb;
    CREATE DATABASE celardb
;" | psql -U celaruser postgres >/dev/null



cat <<EOF >db_temp
-- Converted by db_converter
START TRANSACTION;
SET standard_conforming_strings=off;
SET escape_string_warning=off;
SET CONSTRAINTS ALL DEFERRED;

CREATE TABLE APPLICATION (
    id varchar(36) NOT NULL,
    unique_id integer DEFAULT NULL,
    major_version integer DEFAULT NULL,
    minor_version integer DEFAULT NULL,
    description text ,
    submitted timestamp NULL DEFAULT NULL,
    USER_id integer DEFAULT NULL,
    description_file_location text ,
    PRIMARY KEY (id)
);

CREATE TABLE COMPONENT (
    id integer NOT NULL,
    MODULE_id integer NOT NULL,
    RESOURCE_TYPE_id integer DEFAULT NULL,
    description varchar(120) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE COMPONENT__DEPENDENCY (
    COMPONENT_from_id integer NOT NULL,
    COMPONENT_to_id integer DEFAULT NULL,
    type varchar(90) DEFAULT NULL
);

CREATE TABLE DECISIONS (
    timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    RECISING_ACTION_id integer DEFAULT NULL,
    size integer DEFAULT NULL
);

CREATE TABLE DEPLOYMENT (
    id varchar(90) NOT NULL,
    APPLICATION_id varchar(36) NOT NULL,
    start_time timestamp NULL DEFAULT NULL,
    end_time timestamp NULL DEFAULT NULL,
    orchestrator_IP varchar(90) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE METRICS (
    id integer NOT NULL,
    Name varchar(90) DEFAULT NULL,
    COMPONENT_id integer NOT NULL,
    timestamp timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE METRIC_VALUES (
    id integer NOT NULL,
    METRICS_id integer NOT NULL,
    RESOURCES_id integer DEFAULT NULL,
    timestamp timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE MODULE (
    id integer NOT NULL,
    APPLICATION_id varchar(36) NOT NULL,
    name varchar(90) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE MODULE_DEPENDENCY (
    MODULE_from_id integer NOT NULL,
    MODULE_to_id integer DEFAULT NULL,
    type varchar(90) DEFAULT NULL
);

CREATE TABLE PROVIDED_RESOURCE (
    id integer NOT NULL,
    RESOURCE_TYPE_id integer DEFAULT NULL,
    name varchar(90) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE RESIZING_ACTIONS (
    id integer NOT NULL,
    MODULE_id integer NOT NULL,
    COMPONENT_id integer NOT NULL,
    type varchar(90) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE RESOURCES (
    id integer NOT NULL,
    DEPLOYMENT_id varchar(90) NOT NULL,
    COMPONENT_id integer DEFAULT NULL,
    PROVIDED_RESOURCE_id integer DEFAULT NULL,
    start_time timestamp NULL DEFAULT NULL,
    end_time timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE RESOURCE_TYPE (
    id integer NOT NULL,
    type varchar(90) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE SPECS (
    id integer NOT NULL,
    PROVIDED_RESOURCE_id integer NOT NULL,
    property varchar(90) DEFAULT NULL,
    value varchar(90) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE USERS (
    id integer NOT NULL,
    name varchar(90) DEFAULT NULL,
    iaas_credentials text ,
    PRIMARY KEY (id)
);


-- Post-data save --
COMMIT;
START TRANSACTION;

-- Typecasts --

-- Foreign keys --
ALTER TABLE APPLICATION ADD CONSTRAINT fk_APPLICATION_1 FOREIGN KEY (USER_id) REFERENCES USERS (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX USER_idAPPLICATION ON APPLICATION (USER_id);
ALTER TABLE COMPONENT ADD CONSTRAINT fk_COMPONENT_MODULE1 FOREIGN KEY (MODULE_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX MODULE_idCOMPONENT ON COMPONENT (MODULE_id);
ALTER TABLE COMPONENT ADD CONSTRAINT fk_COMPONENT_1 FOREIGN KEY (RESOURCE_TYPE_id) REFERENCES RESOURCE_TYPE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX RESOURCE_TYPE_idCOMPONENT ON COMPONENT (RESOURCE_TYPE_id);
ALTER TABLE COMPONENT__DEPENDENCY ADD CONSTRAINT fk_DEPENDENCY_1 FOREIGN KEY (COMPONENT_from_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX COMPONENT_from_idCOMPONENT__DEPENDENCY ON COMPONENT__DEPENDENCY (COMPONENT_from_id);
ALTER TABLE COMPONENT__DEPENDENCY ADD CONSTRAINT fk_DEPENDENCY_2 FOREIGN KEY (COMPONENT_to_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX COMPONENT_to_idCOMPONENT__DEPENDENCY ON COMPONENT__DEPENDENCY (COMPONENT_to_id);
ALTER TABLE DECISIONS ADD CONSTRAINT fk_DECISIONS_1 FOREIGN KEY (RECISING_ACTION_id) REFERENCES RESIZING_ACTIONS (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX RECISING_ACTION_idDECISIONS ON DECISIONS (RECISING_ACTION_id);
ALTER TABLE DEPLOYMENT ADD CONSTRAINT fk_DEPLOYMENT_APPLICATION1 FOREIGN KEY (APPLICATION_id) REFERENCES APPLICATION (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX APPLICATION_idDEPLOYMENT ON DEPLOYMENT (APPLICATION_id);
ALTER TABLE METRICS ADD CONSTRAINT fk_METRICS_COMPONENT1 FOREIGN KEY (COMPONENT_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX COMPONENT_idMETRICS ON METRICS (COMPONENT_id);
ALTER TABLE METRIC_VALUES ADD CONSTRAINT fk_METRIC_VALUES_METRICS1 FOREIGN KEY (METRICS_id) REFERENCES METRICS (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX METRICS_idMETRIC_VALUES ON METRIC_VALUES (METRICS_id);
ALTER TABLE METRIC_VALUES ADD CONSTRAINT fk_METRIC_VALUES_RESOURCES1 FOREIGN KEY (RESOURCES_id) REFERENCES RESOURCES (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX RESOURCES_idMETRIC_VALUES ON METRIC_VALUES (RESOURCES_id);
ALTER TABLE MODULE ADD CONSTRAINT fk_MODULE_APPLICATION1 FOREIGN KEY (APPLICATION_id) REFERENCES APPLICATION (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX APPLICATION_idMODULE ON MODULE (APPLICATION_id);
ALTER TABLE MODULE_DEPENDENCY ADD CONSTRAINT fk_MODULE_DEPENDENCY_1 FOREIGN KEY (MODULE_from_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX MODULE_from_idMODULE_DEPENDENCY ON MODULE_DEPENDENCY (MODULE_from_id);
ALTER TABLE MODULE_DEPENDENCY ADD CONSTRAINT fk_MODULE_DEPENDENCY_2 FOREIGN KEY (MODULE_to_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX MODULE_to_idMODULE_DEPENDENCY ON MODULE_DEPENDENCY (MODULE_to_id);
ALTER TABLE PROVIDED_RESOURCE ADD CONSTRAINT fk_PROVIDED_RESOURCE_1 FOREIGN KEY (RESOURCE_TYPE_id) REFERENCES RESOURCE_TYPE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX RESOURCE_TYPE_idPROVIDED_RESOURCE ON PROVIDED_RESOURCE (RESOURCE_TYPE_id);
ALTER TABLE RESIZING_ACTIONS ADD CONSTRAINT fk_DECISION_MODULE1 FOREIGN KEY (MODULE_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX MODULE_idRESIZING_ACTIONS ON RESIZING_ACTIONS (MODULE_id);
ALTER TABLE RESIZING_ACTIONS ADD CONSTRAINT fk_DECISION_COMPONENT1 FOREIGN KEY (COMPONENT_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX COMPONENT_idRESIZING_ACTIONS ON RESIZING_ACTIONS (COMPONENT_id);
ALTER TABLE RESOURCES ADD CONSTRAINT fk_RESOURCES_DEPLOYMENT1 FOREIGN KEY (DEPLOYMENT_id) REFERENCES DEPLOYMENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX DEPLOYMENT_idRESOURCES ON RESOURCES (DEPLOYMENT_id);
ALTER TABLE RESOURCES ADD CONSTRAINT fk_RESOURCES_1 FOREIGN KEY (COMPONENT_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX COMPONENT_idRESOURCES ON RESOURCES (COMPONENT_id);
ALTER TABLE RESOURCES ADD CONSTRAINT fk_RESOURCES_2 FOREIGN KEY (PROVIDED_RESOURCE_id) REFERENCES PROVIDED_RESOURCE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX PROVIDED_RESOURCE_idRESOURCES ON RESOURCES (PROVIDED_RESOURCE_id);
ALTER TABLE SPECS ADD CONSTRAINT fk_SPECS_RESOURCES1 FOREIGN KEY (PROVIDED_RESOURCE_id) REFERENCES PROVIDED_RESOURCE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX PROVIDED_RESOURCE_idSPECS ON SPECS (PROVIDED_RESOURCE_id);

-- Sequences --
CREATE SEQUENCE COMPONENT_id_seq;
SELECT setval('COMPONENT_id_seq', max(id)) FROM COMPONENT;
ALTER TABLE COMPONENT ALTER COLUMN id SET DEFAULT nextval('COMPONENT_id_seq');
CREATE SEQUENCE METRICS_id_seq;
SELECT setval('METRICS_id_seq', max(id)) FROM METRICS;
ALTER TABLE METRICS ALTER COLUMN id SET DEFAULT nextval('METRICS_id_seq');
CREATE SEQUENCE METRIC_VALUES_id_seq;
SELECT setval('METRIC_VALUES_id_seq', max(id)) FROM METRIC_VALUES;
ALTER TABLE METRIC_VALUES ALTER COLUMN id SET DEFAULT nextval('METRIC_VALUES_id_seq');
CREATE SEQUENCE MODULE_id_seq;
SELECT setval('MODULE_id_seq', max(id)) FROM MODULE;
ALTER TABLE MODULE ALTER COLUMN id SET DEFAULT nextval('MODULE_id_seq');
CREATE SEQUENCE PROVIDED_RESOURCE_id_seq;
SELECT setval('PROVIDED_RESOURCE_id_seq', max(id)) FROM PROVIDED_RESOURCE;
ALTER TABLE PROVIDED_RESOURCE ALTER COLUMN id SET DEFAULT nextval('PROVIDED_RESOURCE_id_seq');
CREATE SEQUENCE RESIZING_ACTIONS_id_seq;
SELECT setval('RESIZING_ACTIONS_id_seq', max(id)) FROM RESIZING_ACTIONS;
ALTER TABLE RESIZING_ACTIONS ALTER COLUMN id SET DEFAULT nextval('RESIZING_ACTIONS_id_seq');
CREATE SEQUENCE RESOURCES_id_seq;
SELECT setval('RESOURCES_id_seq', max(id)) FROM RESOURCES;
ALTER TABLE RESOURCES ALTER COLUMN id SET DEFAULT nextval('RESOURCES_id_seq');
CREATE SEQUENCE RESOURCE_TYPE_id_seq;
SELECT setval('RESOURCE_TYPE_id_seq', max(id)) FROM RESOURCE_TYPE;
ALTER TABLE RESOURCE_TYPE ALTER COLUMN id SET DEFAULT nextval('RESOURCE_TYPE_id_seq');
CREATE SEQUENCE SPECS_id_seq;
SELECT setval('SPECS_id_seq', max(id)) FROM SPECS;
ALTER TABLE SPECS ALTER COLUMN id SET DEFAULT nextval('SPECS_id_seq');
CREATE SEQUENCE USERS_id_seq;
SELECT setval('USERS_id_seq', max(id)) FROM USERS;
ALTER TABLE USERS ALTER COLUMN id SET DEFAULT nextval('USERS_id_seq');

-- Full Text keys --

COMMIT;

EOF

psql -U celaruser -d celardb -a -f db_temp &>/dev/null

echo "CELAR DB is created"
rm db_temp

echo "ALL DONE"
