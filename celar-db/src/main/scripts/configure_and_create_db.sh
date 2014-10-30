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
    id integer NOT NULL,
    APPLICATION_id varchar(36) NOT NULL,
    start_time timestamp NULL DEFAULT NULL,
    end_time timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE METRICS (
    id integer NOT NULL,
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
    DEPLOYMENT_id integer NOT NULL,
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
    PRIMARY KEY (id)
);


-- Post-data save --
COMMIT;
START TRANSACTION;

-- Typecasts --

-- Foreign keys --
ALTER TABLE APPLICATION ADD CONSTRAINT fk_APPLICATION_1 FOREIGN KEY (USER_id) REFERENCES USERS (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON APPLICATION (USER_id);
ALTER TABLE COMPONENT ADD CONSTRAINT fk_COMPONENT_MODULE1 FOREIGN KEY (MODULE_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON COMPONENT (MODULE_id);
ALTER TABLE COMPONENT ADD CONSTRAINT fk_COMPONENT_1 FOREIGN KEY (RESOURCE_TYPE_id) REFERENCES RESOURCE_TYPE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON COMPONENT (RESOURCE_TYPE_id);
ALTER TABLE COMPONENT__DEPENDENCY ADD CONSTRAINT fk_DEPENDENCY_1 FOREIGN KEY (COMPONENT_from_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON COMPONENT__DEPENDENCY (COMPONENT_from_id);
ALTER TABLE COMPONENT__DEPENDENCY ADD CONSTRAINT fk_DEPENDENCY_2 FOREIGN KEY (COMPONENT_to_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON COMPONENT__DEPENDENCY (COMPONENT_to_id);
ALTER TABLE DECISIONS ADD CONSTRAINT fk_DECISIONS_1 FOREIGN KEY (RECISING_ACTION_id) REFERENCES RESIZING_ACTIONS (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON DECISIONS (RECISING_ACTION_id);
ALTER TABLE DEPLOYMENT ADD CONSTRAINT fk_DEPLOYMENT_APPLICATION1 FOREIGN KEY (APPLICATION_id) REFERENCES APPLICATION (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON DEPLOYMENT (APPLICATION_id);
ALTER TABLE METRICS ADD CONSTRAINT fk_METRICS_COMPONENT1 FOREIGN KEY (COMPONENT_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON METRICS (COMPONENT_id);
ALTER TABLE METRIC_VALUES ADD CONSTRAINT fk_METRIC_VALUES_METRICS1 FOREIGN KEY (METRICS_id) REFERENCES METRICS (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON METRIC_VALUES (METRICS_id);
ALTER TABLE METRIC_VALUES ADD CONSTRAINT fk_METRIC_VALUES_RESOURCES1 FOREIGN KEY (RESOURCES_id) REFERENCES RESOURCES (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON METRIC_VALUES (RESOURCES_id);
ALTER TABLE MODULE ADD CONSTRAINT fk_MODULE_APPLICATION1 FOREIGN KEY (APPLICATION_id) REFERENCES APPLICATION (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON MODULE (APPLICATION_id);
ALTER TABLE MODULE_DEPENDENCY ADD CONSTRAINT fk_MODULE_DEPENDENCY_1 FOREIGN KEY (MODULE_from_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON MODULE_DEPENDENCY (MODULE_from_id);
ALTER TABLE MODULE_DEPENDENCY ADD CONSTRAINT fk_MODULE_DEPENDENCY_2 FOREIGN KEY (MODULE_to_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON MODULE_DEPENDENCY (MODULE_to_id);
ALTER TABLE PROVIDED_RESOURCE ADD CONSTRAINT fk_PROVIDED_RESOURCE_1 FOREIGN KEY (RESOURCE_TYPE_id) REFERENCES RESOURCE_TYPE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON PROVIDED_RESOURCE (RESOURCE_TYPE_id);
ALTER TABLE RESIZING_ACTIONS ADD CONSTRAINT fk_DECISION_MODULE1 FOREIGN KEY (MODULE_id) REFERENCES MODULE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON RESIZING_ACTIONS (MODULE_id);
ALTER TABLE RESIZING_ACTIONS ADD CONSTRAINT fk_DECISION_COMPONENT1 FOREIGN KEY (COMPONENT_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON RESIZING_ACTIONS (COMPONENT_id);
ALTER TABLE RESOURCES ADD CONSTRAINT fk_RESOURCES_DEPLOYMENT1 FOREIGN KEY (DEPLOYMENT_id) REFERENCES DEPLOYMENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON RESOURCES (DEPLOYMENT_id);
ALTER TABLE RESOURCES ADD CONSTRAINT fk_RESOURCES_1 FOREIGN KEY (COMPONENT_id) REFERENCES COMPONENT (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON RESOURCES (COMPONENT_id);
ALTER TABLE RESOURCES ADD CONSTRAINT fk_RESOURCES_2 FOREIGN KEY (PROVIDED_RESOURCE_id) REFERENCES PROVIDED_RESOURCE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON RESOURCES (PROVIDED_RESOURCE_id);
ALTER TABLE SPECS ADD CONSTRAINT fk_SPECS_RESOURCES1 FOREIGN KEY (PROVIDED_RESOURCE_id) REFERENCES PROVIDED_RESOURCE (id) ON DELETE NO ACTION ON UPDATE NO ACTION DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX ON SPECS (PROVIDED_RESOURCE_id);

-- Sequences --
CREATE SEQUENCE COMPONENT_id_seq;
SELECT setval('COMPONENT_id_seq', max(id)) FROM COMPONENT;
ALTER TABLE COMPONENT ALTER COLUMN id SET DEFAULT nextval('COMPONENT_id_seq');
CREATE SEQUENCE DEPLOYMENT_id_seq;
SELECT setval('DEPLOYMENT_id_seq', max(id)) FROM DEPLOYMENT;
ALTER TABLE DEPLOYMENT ALTER COLUMN id SET DEFAULT nextval('DEPLOYMENT_id_seq');
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




#trust local users
sed -i "s/local   all         all                               ident/local   all         all                               trust/g" -i /var/lib/pgsql/data/pg_hba.conf

#restart to apply changes
service postgresql restart


# drop and re-create the DB
echo "DROP DATABASE celardb;
    CREATE DATABASE celardb;
" | psql -U celaruser postgres >/dev/null

psql -U celaruser -d celardb -a -f db_temp &>/dev/null
rm db_temp
