SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `celardb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `celardb` ;

-- -----------------------------------------------------
-- Table `celardb`.`USERS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`USERS` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `iaas_credentials` TEXT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`APPLICATION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`APPLICATION` (
  `id` VARCHAR(18) NOT NULL,
  `unique_id` INT NULL,
  `major_version` INT NULL,
  `minor_version` INT NULL,
  `description` TEXT NULL,
  `submitted` TIMESTAMP NULL,
  `USER_id` INT NULL,
  `description_file_location` TEXT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_APPLICATION_1`
    FOREIGN KEY (`USER_id`)
    REFERENCES `celardb`.`USERS` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`MODULE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`MODULE` (
  `id` INT NOT NULL,
  `APPLICATION_id` VARCHAR(18) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_MODULE_APPLICATION1`
    FOREIGN KEY (`APPLICATION_id`)
    REFERENCES `celardb`.`APPLICATION` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`RESOURCE_TYPE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`RESOURCE_TYPE` (
  `id` INT NOT NULL,
  `type` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`COMPONENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`COMPONENT` (
  `id` INT NOT NULL,
  `MODULE_id` INT NOT NULL,
  `RESOURCE_TYPE_id` INT NULL,
  `description` VARCHAR(60) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_COMPONENT_MODULE1`
    FOREIGN KEY (`MODULE_id`)
    REFERENCES `celardb`.`MODULE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_COMPONENT_1`
    FOREIGN KEY (`RESOURCE_TYPE_id`)
    REFERENCES `celardb`.`RESOURCE_TYPE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`DEPLOYMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`DEPLOYMENT` (
  `id` VARCHAR(45) NOT NULL,
  `APPLICATION_id` VARCHAR(18) NOT NULL,
  `start_time` TIMESTAMP NULL,
  `end_time` TIMESTAMP NULL,
  `orchestrator_IP` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_DEPLOYMENT_APPLICATION1`
    FOREIGN KEY (`APPLICATION_id`)
    REFERENCES `celardb`.`APPLICATION` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`PROVIDED_RESOURCE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`PROVIDED_RESOURCE` (
  `id` INT NOT NULL,
  `RESOURCE_TYPE_id` INT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_PROVIDED_RESOURCE_1`
    FOREIGN KEY (`RESOURCE_TYPE_id`)
    REFERENCES `celardb`.`RESOURCE_TYPE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`RESOURCES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`RESOURCES` (
  `id` INT NOT NULL,
  `DEPLOYMENT_id` VARCHAR(45) NOT NULL,
  `COMPONENT_id` INT NULL,
  `PROVIDED_RESOURCE_id` INT NULL,
  `start_time` TIMESTAMP NULL,
  `end_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_RESOURCES_DEPLOYMENT1`
    FOREIGN KEY (`DEPLOYMENT_id`)
    REFERENCES `celardb`.`DEPLOYMENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RESOURCES_1`
    FOREIGN KEY (`COMPONENT_id`)
    REFERENCES `celardb`.`COMPONENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RESOURCES_2`
    FOREIGN KEY (`PROVIDED_RESOURCE_id`)
    REFERENCES `celardb`.`PROVIDED_RESOURCE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`SPECS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`SPECS` (
  `id` INT NOT NULL,
  `PROVIDED_RESOURCE_id` INT NOT NULL,
  `property` VARCHAR(45) NULL,
  `value` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_SPECS_RESOURCES1`
    FOREIGN KEY (`PROVIDED_RESOURCE_id`)
    REFERENCES `celardb`.`PROVIDED_RESOURCE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`RESIZING_ACTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`RESIZING_ACTION` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `MODULE_id` INT NOT NULL,
  `COMPONENT_id` INT NOT NULL,
  `type` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_DECISION_MODULE1`
    FOREIGN KEY (`MODULE_id`)
    REFERENCES `celardb`.`MODULE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DECISION_COMPONENT1`
    FOREIGN KEY (`COMPONENT_id`)
    REFERENCES `celardb`.`COMPONENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`METRICS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`METRICS` (
  `id` INT NOT NULL,
  `Name` VARCHAR(45) NULL,
  `COMPONENT_id` INT NOT NULL,
  `timestamp` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_METRICS_COMPONENT1`
    FOREIGN KEY (`COMPONENT_id`)
    REFERENCES `celardb`.`COMPONENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`METRIC_VALUES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`METRIC_VALUES` (
  `id` INT NOT NULL,
  `METRICS_id` INT NOT NULL,
  `RESOURCES_id` INT NULL,
  `timestamp` TIMESTAMP NULL,
  `value` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_METRIC_VALUES_METRICS1`
    FOREIGN KEY (`METRICS_id`)
    REFERENCES `celardb`.`METRICS` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_METRIC_VALUES_RESOURCES1`
    FOREIGN KEY (`RESOURCES_id`)
    REFERENCES `celardb`.`RESOURCES` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`DECISIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`DECISIONS` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `RESIZING_ACTION_id` INT NULL,
  `DEPLOYMENT_id` VARCHAR(45) NULL,
  `timestamp` TIMESTAMP NOT NULL,
  `size` INT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_DECISIONS_1`
    FOREIGN KEY (`RESIZING_ACTION_id`)
    REFERENCES `celardb`.`RESIZING_ACTION` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DECISIONS_2`
    FOREIGN KEY (`DEPLOYMENT_id`)
    REFERENCES `celardb`.`DEPLOYMENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`COMPONENT_DEPENDENCY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`COMPONENT_DEPENDENCY` (
  `COMPONENT_from_id` INT NOT NULL,
  `COMPONENT_to_id` INT NULL,
  `type` VARCHAR(45) NULL,
  CONSTRAINT `fk_DEPENDENCY_1`
    FOREIGN KEY (`COMPONENT_from_id`)
    REFERENCES `celardb`.`COMPONENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DEPENDENCY_2`
    FOREIGN KEY (`COMPONENT_to_id`)
    REFERENCES `celardb`.`COMPONENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`MODULE_DEPENDENCY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`MODULE_DEPENDENCY` (
  `MODULE_from_id` INT NOT NULL,
  `MODULE_to_id` INT NULL,
  `type` VARCHAR(45) NULL,
  CONSTRAINT `fk_MODULE_DEPENDENCY_1`
    FOREIGN KEY (`MODULE_from_id`)
    REFERENCES `celardb`.`MODULE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MODULE_DEPENDENCY_2`
    FOREIGN KEY (`MODULE_to_id`)
    REFERENCES `celardb`.`MODULE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celardb`.`RESOURCE_PROPERTY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `celardb`.`RESOURCE_PROPERTY` (
  `RESOURCES_id` INT NULL,
  `property` TEXT NULL,
  `value` TEXT NULL,
  CONSTRAINT `fk_RESOURCE_PROPERTY_1`
    FOREIGN KEY (`RESOURCES_id`)
    REFERENCES `celardb`.`RESOURCES` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
