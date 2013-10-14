SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `celar_db` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `celar_db` ;

-- -----------------------------------------------------
-- Table `celar_db`.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`USER` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`USER` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`APPLICATION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`APPLICATION` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`APPLICATION` (
  `id` INT NOT NULL ,
  `description` BLOB NULL ,
  `submitted` TIMESTAMP NULL ,
  `USER_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_APPLICATION_1`
    FOREIGN KEY (`id` )
    REFERENCES `celar_db`.`USER` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`MODULE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`MODULE` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`MODULE` (
  `id` INT NOT NULL ,
  `APPLICATION_id` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_MODULE_APPLICATION1` (`APPLICATION_id` ASC) ,
  CONSTRAINT `fk_MODULE_APPLICATION1`
    FOREIGN KEY (`APPLICATION_id` )
    REFERENCES `celar_db`.`APPLICATION` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`PROVIDED_RESOURCE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`PROVIDED_RESOURCE` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`PROVIDED_RESOURCE` (
  `id` INT NOT NULL ,
  `type` VARCHAR(45) NULL ,
  `description` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`COMPONENT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`COMPONENT` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`COMPONENT` (
  `id` INT NOT NULL ,
  `MODULE_id` INT NOT NULL ,
  `PROVIDED_RESOURCE_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_COMPONENT_MODULE1` (`MODULE_id` ASC) ,
  CONSTRAINT `fk_COMPONENT_MODULE1`
    FOREIGN KEY (`MODULE_id` )
    REFERENCES `celar_db`.`MODULE` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_COMPONENT_1`
    FOREIGN KEY (`id` )
    REFERENCES `celar_db`.`PROVIDED_RESOURCE` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`DEPLOYMENT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`DEPLOYMENT` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`DEPLOYMENT` (
  `id` INT NOT NULL ,
  `APPLICATION_id` INT NOT NULL ,
  `start_time` TIMESTAMP NULL ,
  `end_time` TIMESTAMP NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_DEPLOYMENT_APPLICATION1` (`APPLICATION_id` ASC) ,
  CONSTRAINT `fk_DEPLOYMENT_APPLICATION1`
    FOREIGN KEY (`APPLICATION_id` )
    REFERENCES `celar_db`.`APPLICATION` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`RESOURCES`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`RESOURCES` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`RESOURCES` (
  `id` INT NOT NULL ,
  `DEPLOYMENT_id` INT NOT NULL ,
  `COMPONENT_id` INT NULL ,
  `PROVIDED_RESOURCE_id` INT NULL ,
  `start_time` TIMESTAMP NULL ,
  `end_time` TIMESTAMP NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_RESOURCES_DEPLOYMENT1` (`DEPLOYMENT_id` ASC) ,
  INDEX `fk_RESOURCES_1_idx` (`COMPONENT_id` ASC) ,
  CONSTRAINT `fk_RESOURCES_DEPLOYMENT1`
    FOREIGN KEY (`DEPLOYMENT_id` )
    REFERENCES `celar_db`.`DEPLOYMENT` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RESOURCES_1`
    FOREIGN KEY (`COMPONENT_id` )
    REFERENCES `celar_db`.`COMPONENT` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RESOURCES_2`
    FOREIGN KEY (`PROVIDED_RESOURCE_id` )
    REFERENCES `celar_db`.`PROVIDED_RESOURCE` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`SPECS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`SPECS` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`SPECS` (
  `id` INT NOT NULL ,
  `PROVIDED_RESOURCE_id` INT NOT NULL ,
  `description` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_SPECS_RESOURCES1`
    FOREIGN KEY (`id` )
    REFERENCES `celar_db`.`PROVIDED_RESOURCE` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`RESIZING_ACTIONS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`RESIZING_ACTIONS` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`RESIZING_ACTIONS` (
  `id` INT NOT NULL ,
  `MODULE_id` INT NOT NULL ,
  `COMPONENT_id` INT NOT NULL ,
  `type` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_DECISION_MODULE1` (`MODULE_id` ASC) ,
  INDEX `fk_DECISION_COMPONENT1` (`COMPONENT_id` ASC) ,
  CONSTRAINT `fk_DECISION_MODULE1`
    FOREIGN KEY (`MODULE_id` )
    REFERENCES `celar_db`.`MODULE` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DECISION_COMPONENT1`
    FOREIGN KEY (`COMPONENT_id` )
    REFERENCES `celar_db`.`COMPONENT` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`METRICS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`METRICS` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`METRICS` (
  `id` INT NOT NULL ,
  `COMPONENT_id` INT NOT NULL ,
  `timestamp` TIMESTAMP NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_METRICS_COMPONENT1` (`COMPONENT_id` ASC) ,
  CONSTRAINT `fk_METRICS_COMPONENT1`
    FOREIGN KEY (`COMPONENT_id` )
    REFERENCES `celar_db`.`COMPONENT` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`METRIC_VALUES`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`METRIC_VALUES` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`METRIC_VALUES` (
  `id` INT NOT NULL ,
  `METRICS_id` INT NOT NULL ,
  `RESOURCES_id` INT NULL ,
  `timestamp` TIMESTAMP NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_METRIC_VALUES_METRICS1` (`METRICS_id` ASC) ,
  INDEX `fk_METRIC_VALUES_RESOURCES1` (`RESOURCES_id` ASC) ,
  CONSTRAINT `fk_METRIC_VALUES_METRICS1`
    FOREIGN KEY (`METRICS_id` )
    REFERENCES `celar_db`.`METRICS` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_METRIC_VALUES_RESOURCES1`
    FOREIGN KEY (`RESOURCES_id` )
    REFERENCES `celar_db`.`RESOURCES` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`DECISIONS`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`DECISIONS` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`DECISIONS` (
  `timestamp` TIMESTAMP NOT NULL ,
  `RECISING_ACTION_id` INT NULL ,
  `size` INT NULL ,
  INDEX `fk_DECISIONS_1_idx` (`RECISING_ACTION_id` ASC) ,
  CONSTRAINT `fk_DECISIONS_1`
    FOREIGN KEY (`RECISING_ACTION_id` )
    REFERENCES `celar_db`.`RESIZING_ACTIONS` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`COMPONENT__DEPENDENCY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`COMPONENT__DEPENDENCY` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`COMPONENT__DEPENDENCY` (
  `COMPONENT_from_id` INT NOT NULL ,
  `COMPONENT_to_id` INT NULL ,
  `type` VARCHAR(45) NULL ,
  INDEX `fk_DEPENDENCY_2_idx` (`COMPONENT_to_id` ASC) ,
  CONSTRAINT `fk_DEPENDENCY_1`
    FOREIGN KEY (`COMPONENT_from_id` )
    REFERENCES `celar_db`.`COMPONENT` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DEPENDENCY_2`
    FOREIGN KEY (`COMPONENT_to_id` )
    REFERENCES `celar_db`.`COMPONENT` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `celar_db`.`MODULE_DEPENDENCY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `celar_db`.`MODULE_DEPENDENCY` ;

CREATE  TABLE IF NOT EXISTS `celar_db`.`MODULE_DEPENDENCY` (
  `MODULE_from_id` INT NOT NULL ,
  `MODULE_to_id` INT NULL ,
  `type` VARCHAR(45) NULL ,
  INDEX `fk_MODULE_DEPENDENCY_1_idx` (`MODULE_from_id` ASC) ,
  INDEX `fk_MODULE_DEPENDENCY_2_idx` (`MODULE_to_id` ASC) ,
  CONSTRAINT `fk_MODULE_DEPENDENCY_1`
    FOREIGN KEY (`MODULE_from_id` )
    REFERENCES `celar_db`.`MODULE` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MODULE_DEPENDENCY_2`
    FOREIGN KEY (`MODULE_to_id` )
    REFERENCES `celar_db`.`MODULE` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
