-- MySQL dump 10.13  Distrib 5.5.37, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: celardb
-- ------------------------------------------------------
-- Server version	5.5.37-0ubuntu0.13.10.1
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO,POSTGRESQL' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table "APPLICATION"
--

DROP TABLE IF EXISTS "APPLICATION";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "APPLICATION" (
  "id" varchar(18) NOT NULL,
  "unique_id" int(11) DEFAULT NULL,
  "major_version" int(11) DEFAULT NULL,
  "minor_version" int(11) DEFAULT NULL,
  "description" text,
  "submitted" timestamp NULL DEFAULT NULL,
  "USER_id" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_APPLICATION_1" ("USER_id"),
  CONSTRAINT "fk_APPLICATION_1" FOREIGN KEY ("USER_id") REFERENCES "USERS" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "APPLICATION"
--

LOCK TABLES "APPLICATION" WRITE;
/*!40000 ALTER TABLE "APPLICATION" DISABLE KEYS */;
/*!40000 ALTER TABLE "APPLICATION" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "COMPONENT"
--

DROP TABLE IF EXISTS "COMPONENT";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "COMPONENT" (
  "id" int(11) NOT NULL,
  "MODULE_id" int(11) NOT NULL,
  "RESOURCE_TYPE_id" int(11) DEFAULT NULL,
  "description" varchar(60) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_COMPONENT_MODULE1" ("MODULE_id"),
  KEY "fk_COMPONENT_1" ("RESOURCE_TYPE_id"),
  CONSTRAINT "fk_COMPONENT_MODULE1" FOREIGN KEY ("MODULE_id") REFERENCES "MODULE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "fk_COMPONENT_1" FOREIGN KEY ("RESOURCE_TYPE_id") REFERENCES "RESOURCE_TYPE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "COMPONENT"
--

LOCK TABLES "COMPONENT" WRITE;
/*!40000 ALTER TABLE "COMPONENT" DISABLE KEYS */;
/*!40000 ALTER TABLE "COMPONENT" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "COMPONENT__DEPENDENCY"
--

DROP TABLE IF EXISTS "COMPONENT__DEPENDENCY";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "COMPONENT__DEPENDENCY" (
  "COMPONENT_from_id" int(11) NOT NULL,
  "COMPONENT_to_id" int(11) DEFAULT NULL,
  "type" varchar(45) DEFAULT NULL,
  KEY "fk_DEPENDENCY_1" ("COMPONENT_from_id"),
  KEY "fk_DEPENDENCY_2" ("COMPONENT_to_id"),
  CONSTRAINT "fk_DEPENDENCY_1" FOREIGN KEY ("COMPONENT_from_id") REFERENCES "COMPONENT" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "fk_DEPENDENCY_2" FOREIGN KEY ("COMPONENT_to_id") REFERENCES "COMPONENT" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "COMPONENT__DEPENDENCY"
--

LOCK TABLES "COMPONENT__DEPENDENCY" WRITE;
/*!40000 ALTER TABLE "COMPONENT__DEPENDENCY" DISABLE KEYS */;
/*!40000 ALTER TABLE "COMPONENT__DEPENDENCY" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "DECISIONS"
--

DROP TABLE IF EXISTS "DECISIONS";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "DECISIONS" (
  "timestamp" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "RECISING_ACTION_id" int(11) DEFAULT NULL,
  "size" int(11) DEFAULT NULL,
  KEY "fk_DECISIONS_1" ("RECISING_ACTION_id"),
  CONSTRAINT "fk_DECISIONS_1" FOREIGN KEY ("RECISING_ACTION_id") REFERENCES "RESIZING_ACTIONS" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "DECISIONS"
--

LOCK TABLES "DECISIONS" WRITE;
/*!40000 ALTER TABLE "DECISIONS" DISABLE KEYS */;
/*!40000 ALTER TABLE "DECISIONS" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "DEPLOYMENT"
--

DROP TABLE IF EXISTS "DEPLOYMENT";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "DEPLOYMENT" (
  "id" int(11) NOT NULL,
  "APPLICATION_id" varchar(18) NOT NULL,
  "start_time" timestamp NULL DEFAULT NULL,
  "end_time" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_DEPLOYMENT_APPLICATION1" ("APPLICATION_id"),
  CONSTRAINT "fk_DEPLOYMENT_APPLICATION1" FOREIGN KEY ("APPLICATION_id") REFERENCES "APPLICATION" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "DEPLOYMENT"
--

LOCK TABLES "DEPLOYMENT" WRITE;
/*!40000 ALTER TABLE "DEPLOYMENT" DISABLE KEYS */;
/*!40000 ALTER TABLE "DEPLOYMENT" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "METRICS"
--

DROP TABLE IF EXISTS "METRICS";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "METRICS" (
  "id" int(11) NOT NULL,
  "COMPONENT_id" int(11) NOT NULL,
  "timestamp" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_METRICS_COMPONENT1" ("COMPONENT_id"),
  CONSTRAINT "fk_METRICS_COMPONENT1" FOREIGN KEY ("COMPONENT_id") REFERENCES "COMPONENT" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "METRICS"
--

LOCK TABLES "METRICS" WRITE;
/*!40000 ALTER TABLE "METRICS" DISABLE KEYS */;
/*!40000 ALTER TABLE "METRICS" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "METRIC_VALUES"
--

DROP TABLE IF EXISTS "METRIC_VALUES";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "METRIC_VALUES" (
  "id" int(11) NOT NULL,
  "METRICS_id" int(11) NOT NULL,
  "RESOURCES_id" int(11) DEFAULT NULL,
  "timestamp" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_METRIC_VALUES_METRICS1" ("METRICS_id"),
  KEY "fk_METRIC_VALUES_RESOURCES1" ("RESOURCES_id"),
  CONSTRAINT "fk_METRIC_VALUES_METRICS1" FOREIGN KEY ("METRICS_id") REFERENCES "METRICS" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "fk_METRIC_VALUES_RESOURCES1" FOREIGN KEY ("RESOURCES_id") REFERENCES "RESOURCES" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "METRIC_VALUES"
--

LOCK TABLES "METRIC_VALUES" WRITE;
/*!40000 ALTER TABLE "METRIC_VALUES" DISABLE KEYS */;
/*!40000 ALTER TABLE "METRIC_VALUES" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "MODULE"
--

DROP TABLE IF EXISTS "MODULE";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "MODULE" (
  "id" int(11) NOT NULL,
  "APPLICATION_id" varchar(18) NOT NULL,
  "name" varchar(45) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_MODULE_APPLICATION1" ("APPLICATION_id"),
  CONSTRAINT "fk_MODULE_APPLICATION1" FOREIGN KEY ("APPLICATION_id") REFERENCES "APPLICATION" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "MODULE"
--

LOCK TABLES "MODULE" WRITE;
/*!40000 ALTER TABLE "MODULE" DISABLE KEYS */;
/*!40000 ALTER TABLE "MODULE" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "MODULE_DEPENDENCY"
--

DROP TABLE IF EXISTS "MODULE_DEPENDENCY";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "MODULE_DEPENDENCY" (
  "MODULE_from_id" int(11) NOT NULL,
  "MODULE_to_id" int(11) DEFAULT NULL,
  "type" varchar(45) DEFAULT NULL,
  KEY "fk_MODULE_DEPENDENCY_1" ("MODULE_from_id"),
  KEY "fk_MODULE_DEPENDENCY_2" ("MODULE_to_id"),
  CONSTRAINT "fk_MODULE_DEPENDENCY_1" FOREIGN KEY ("MODULE_from_id") REFERENCES "MODULE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "fk_MODULE_DEPENDENCY_2" FOREIGN KEY ("MODULE_to_id") REFERENCES "MODULE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "MODULE_DEPENDENCY"
--

LOCK TABLES "MODULE_DEPENDENCY" WRITE;
/*!40000 ALTER TABLE "MODULE_DEPENDENCY" DISABLE KEYS */;
/*!40000 ALTER TABLE "MODULE_DEPENDENCY" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "PROVIDED_RESOURCE"
--

DROP TABLE IF EXISTS "PROVIDED_RESOURCE";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "PROVIDED_RESOURCE" (
  "id" int(11) NOT NULL,
  "RESOURCE_TYPE_id" int(11) DEFAULT NULL,
  "name" varchar(45) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_PROVIDED_RESOURCE_1" ("RESOURCE_TYPE_id"),
  CONSTRAINT "fk_PROVIDED_RESOURCE_1" FOREIGN KEY ("RESOURCE_TYPE_id") REFERENCES "RESOURCE_TYPE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "PROVIDED_RESOURCE"
--

LOCK TABLES "PROVIDED_RESOURCE" WRITE;
/*!40000 ALTER TABLE "PROVIDED_RESOURCE" DISABLE KEYS */;
/*!40000 ALTER TABLE "PROVIDED_RESOURCE" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "RESIZING_ACTIONS"
--

DROP TABLE IF EXISTS "RESIZING_ACTIONS";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "RESIZING_ACTIONS" (
  "id" int(11) NOT NULL,
  "MODULE_id" int(11) NOT NULL,
  "COMPONENT_id" int(11) NOT NULL,
  "type" varchar(45) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_DECISION_MODULE1" ("MODULE_id"),
  KEY "fk_DECISION_COMPONENT1" ("COMPONENT_id"),
  CONSTRAINT "fk_DECISION_MODULE1" FOREIGN KEY ("MODULE_id") REFERENCES "MODULE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "fk_DECISION_COMPONENT1" FOREIGN KEY ("COMPONENT_id") REFERENCES "COMPONENT" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "RESIZING_ACTIONS"
--

LOCK TABLES "RESIZING_ACTIONS" WRITE;
/*!40000 ALTER TABLE "RESIZING_ACTIONS" DISABLE KEYS */;
/*!40000 ALTER TABLE "RESIZING_ACTIONS" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "RESOURCES"
--

DROP TABLE IF EXISTS "RESOURCES";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "RESOURCES" (
  "id" int(11) NOT NULL,
  "DEPLOYMENT_id" int(11) NOT NULL,
  "COMPONENT_id" int(11) DEFAULT NULL,
  "PROVIDED_RESOURCE_id" int(11) DEFAULT NULL,
  "start_time" timestamp NULL DEFAULT NULL,
  "end_time" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_RESOURCES_DEPLOYMENT1" ("DEPLOYMENT_id"),
  KEY "fk_RESOURCES_1" ("COMPONENT_id"),
  KEY "fk_RESOURCES_2" ("PROVIDED_RESOURCE_id"),
  CONSTRAINT "fk_RESOURCES_DEPLOYMENT1" FOREIGN KEY ("DEPLOYMENT_id") REFERENCES "DEPLOYMENT" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "fk_RESOURCES_1" FOREIGN KEY ("COMPONENT_id") REFERENCES "COMPONENT" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "fk_RESOURCES_2" FOREIGN KEY ("PROVIDED_RESOURCE_id") REFERENCES "PROVIDED_RESOURCE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "RESOURCES"
--

LOCK TABLES "RESOURCES" WRITE;
/*!40000 ALTER TABLE "RESOURCES" DISABLE KEYS */;
/*!40000 ALTER TABLE "RESOURCES" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "RESOURCE_TYPE"
--

DROP TABLE IF EXISTS "RESOURCE_TYPE";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "RESOURCE_TYPE" (
  "id" int(11) NOT NULL,
  "type" varchar(45) DEFAULT NULL,
  PRIMARY KEY ("id")
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "RESOURCE_TYPE"
--

LOCK TABLES "RESOURCE_TYPE" WRITE;
/*!40000 ALTER TABLE "RESOURCE_TYPE" DISABLE KEYS */;
/*!40000 ALTER TABLE "RESOURCE_TYPE" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "SPECS"
--

DROP TABLE IF EXISTS "SPECS";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "SPECS" (
  "id" int(11) NOT NULL,
  "PROVIDED_RESOURCE_id" int(11) NOT NULL,
  "property" varchar(45) DEFAULT NULL,
  "value" varchar(45) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "fk_SPECS_RESOURCES1" ("PROVIDED_RESOURCE_id"),
  CONSTRAINT "fk_SPECS_RESOURCES1" FOREIGN KEY ("PROVIDED_RESOURCE_id") REFERENCES "PROVIDED_RESOURCE" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "SPECS"
--

LOCK TABLES "SPECS" WRITE;
/*!40000 ALTER TABLE "SPECS" DISABLE KEYS */;
/*!40000 ALTER TABLE "SPECS" ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table "USERS"
--

DROP TABLE IF EXISTS "USERS";
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE "USERS" (
  "id" int(11) NOT NULL,
  "name" varchar(45) DEFAULT NULL,
  PRIMARY KEY ("id")
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table "USERS"
--

LOCK TABLES "USERS" WRITE;
/*!40000 ALTER TABLE "USERS" DISABLE KEYS */;
/*!40000 ALTER TABLE "USERS" ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-13 12:37:59
