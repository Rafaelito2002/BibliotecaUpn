-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bibliotecaupn
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bibliotecaupn
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bibliotecaupn` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `bibliotecaupn` ;

-- -----------------------------------------------------
-- Table `bibliotecaupn`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`usuario` (
  `IdUsuario` CHAR(8) NOT NULL,
  `NombreUsuario` VARCHAR(50) NOT NULL,
  `EmailUsuario` VARCHAR(50) NOT NULL,
  `Password` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`IdUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- Inserción de dos usuarios en la tabla 'usuario'
INSERT INTO `bibliotecaupn`.`usuario` (`IdUsuario`, `NombreUsuario`, `EmailUsuario`, `Password`)
VALUES 
('A0000001', 'Admin1', 'admin1@biblioteca.com', 'admin1password'),
('A0000002', 'Admin2', 'admin2@biblioteca.com', 'admin2password');

-- Inserción de los administradores en la tabla 'administradorsistema'
-- -----------------------------------------------------
-- Table `bibliotecaupn`.`administradorsistema`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`administradorsistema` (
  `IdAdmin` CHAR(8) NOT NULL,
  PRIMARY KEY (`IdAdmin`),
  CONSTRAINT `administradorsistema_ibfk_1`
    FOREIGN KEY (`IdAdmin`)
    REFERENCES `bibliotecaupn`.`usuario` (`IdUsuario`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bibliotecaupn`.`bibliotecario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`bibliotecario` (
  `IdBiblio` CHAR(8) NOT NULL,
  PRIMARY KEY (`IdBiblio`),
  CONSTRAINT `bibliotecario_ibfk_1`
    FOREIGN KEY (`IdBiblio`)
    REFERENCES `bibliotecaupn`.`usuario` (`IdUsuario`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bibliotecaupn`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `direccion` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  `apellidos` VARCHAR(255) NULL DEFAULT NULL,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `telefono` VARCHAR(255) NULL DEFAULT NULL,
  `tipo` VARCHAR(255) NULL DEFAULT NULL,
  `username` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
select * from usuarios;

-- -----------------------------------------------------
-- Table `bibliotecaupn`.`ordenes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`ordenes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fecha_creacion` DATETIME(6) NULL DEFAULT NULL,
  `fecha_recibida` DATETIME(6) NULL DEFAULT NULL,
  `numero` VARCHAR(255) NULL DEFAULT NULL,
  `usuario_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKsqu43gsd6mtx7b1siww96324` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `FKsqu43gsd6mtx7b1siww96324`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `bibliotecaupn`.`usuarios` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bibliotecaupn`.`productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NOT NULL,
  `descripcion` VARCHAR(255) NULL DEFAULT NULL,
  `imagen` VARCHAR(255) NULL DEFAULT NULL,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  `usuario_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKo8g0kqq9awvgh4elqai7tdhu` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `FKo8g0kqq9awvgh4elqai7tdhu`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `bibliotecaupn`.`usuarios` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bibliotecaupn`.`detalles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`detalles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cantidad` DOUBLE NOT NULL,
  `nombre` VARCHAR(255) NULL DEFAULT NULL,
  `orden_id` INT NULL DEFAULT NULL,
  `producto_id` INT NULL DEFAULT NULL,
  `correo` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKdurdo71oa161lmmal7oeaor74` (`orden_id` ASC) VISIBLE,
  INDEX `FKio4oyl8qt5jclekxp7bwws2iy` (`producto_id` ASC) VISIBLE,
  CONSTRAINT `FKdurdo71oa161lmmal7oeaor74`
    FOREIGN KEY (`orden_id`)
    REFERENCES `bibliotecaupn`.`ordenes` (`id`),
  CONSTRAINT `FKio4oyl8qt5jclekxp7bwws2iy`
    FOREIGN KEY (`producto_id`)
    REFERENCES `bibliotecaupn`.`productos` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bibliotecaupn`.`usuariogeneral`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliotecaupn`.`usuariogeneral` (
  `IdUsuariog` CHAR(8) NOT NULL,
  PRIMARY KEY (`IdUsuariog`),
  CONSTRAINT `usuariogeneral_ibfk_1`
    FOREIGN KEY (`IdUsuariog`)
    REFERENCES `bibliotecaupn`.`usuario` (`IdUsuario`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
