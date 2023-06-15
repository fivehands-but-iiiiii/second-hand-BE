-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema secondhand-db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `secondhand-db`;

-- -----------------------------------------------------
-- Schema secondhand-db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `secondhand-db` DEFAULT CHARACTER SET utf8mb4;
USE `secondhand-db`;

-- -----------------------------------------------------
-- Table `secondhand-db`.`member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`member`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`member`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`        VARCHAR(16)  NOT NULL,
    `profile_img_url` VARCHAR(300) NULL,
    `oauth`           ENUM('GITHUB', 'NONE'),
    PRIMARY KEY (`id`),
    UNIQUE INDEX `memberId_UNIQUE` (`member_id` ASC) VISIBLE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`region`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`region`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`region`
(
    `id`       BIGINT      NOT NULL,
    `city`     VARCHAR(45) NOT NULL,
    `county`   VARCHAR(45) NOT NULL,
    `district` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`category`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`category`
(
    `id`      BIGINT       NOT NULL,
    `title`   VARCHAR(16)  NOT NULL,
    `img_url` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`item_counts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`item_counts`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`item_counts`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `hits`        BIGINT      NOT NULL,
    `like_counts` VARCHAR(45) NOT NULL,
    `chat_counts` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`item_contents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`item_contents`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`item_contents`
(
    `id`               BIGINT        NOT NULL AUTO_INCREMENT,
    `contents`         TEXT          NULL,
    `detail_image_url` VARCHAR(2000) NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`item`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`item`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `title`            VARCHAR(45)  NOT NULL,
    `price`            INT          NULL,
    `status`           VARCHAR(16)  NOT NULL,
    `category`         VARCHAR(45)  NOT NULL,
    `thumbnail_url`    VARCHAR(300) NOT NULL,
    `created_at`       DATETIME     NOT NULL,
    `updated_at`       DATETIME     NULL,
    `seller_id`        BIGINT       NOT NULL,
    `item_counts_id`   BIGINT       NOT NULL,
    `region_id`        BIGINT       NOT NULL,
    `item_contents_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_item_member_idx` (`seller_id` ASC) VISIBLE,
    INDEX `fk_item_item_image1_idx` (`item_counts_id` ASC) VISIBLE,
    INDEX `fk_item_region1_idx` (`region_id` ASC) VISIBLE,
    INDEX `fk_item_item_contents1_idx` (`item_contents_id` ASC) VISIBLE,
    CONSTRAINT `fk_item_member`
        FOREIGN KEY (`seller_id`)
            REFERENCES `secondhand-db`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_item_item_image1`
        FOREIGN KEY (`item_counts_id`)
            REFERENCES `secondhand-db`.`item_counts` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_item_region1`
        FOREIGN KEY (`region_id`)
            REFERENCES `secondhand-db`.`region` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_item_item_contents1`
        FOREIGN KEY (`item_contents_id`)
            REFERENCES `secondhand-db`.`item_contents` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`member_like_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`member_like_item`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`member_like_item`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT,
    `member_id`  BIGINT   NOT NULL,
    `item_id`    BIGINT   NOT NULL,
    `created_at` DATETIME NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_has_item_item1_idx` (`item_id` ASC) VISIBLE,
    INDEX `fk_member_has_item_member1_idx` (`member_id` ASC) VISIBLE,
    CONSTRAINT `fk_member_has_item_member1`
        FOREIGN KEY (`member_id`)
            REFERENCES `secondhand-db`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_member_has_item_item1`
        FOREIGN KEY (`item_id`)
            REFERENCES `secondhand-db`.`item` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`chat_room`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`chat_room`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`chat_room`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT,
    `item_id`    BIGINT   NOT NULL,
    `buyer_id`   BIGINT   NOT NULL,
    `created_at` DATETIME NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_item_has_member_member1_idx` (`buyer_id` ASC) VISIBLE,
    INDEX `fk_item_has_member_item1_idx` (`item_id` ASC) VISIBLE,
    CONSTRAINT `fk_item_has_member_item1`
        FOREIGN KEY (`item_id`)
            REFERENCES `secondhand-db`.`item` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_item_has_member_member1`
        FOREIGN KEY (`buyer_id`)
            REFERENCES `secondhand-db`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`chat_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`chat_log`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`chat_log`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `contents`     VARCHAR(300) NULL,
    `sender_id`    BIGINT       NOT NULL,
    `reciver_id`   BIGINT       NOT NULL,
    `chat_room_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_chat_log_member1_idx` (`sender_id` ASC) VISIBLE,
    INDEX `fk_chat_log_member2_idx` (`reciver_id` ASC) VISIBLE,
    INDEX `fk_chat_log_member_chat_about_item1_idx` (`chat_room_id` ASC) VISIBLE,
    CONSTRAINT `fk_chat_log_member1`
        FOREIGN KEY (`sender_id`)
            REFERENCES `secondhand-db`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_chat_log_member2`
        FOREIGN KEY (`reciver_id`)
            REFERENCES `secondhand-db`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_chat_log_member_chat_about_item1`
        FOREIGN KEY (`chat_room_id`)
            REFERENCES `secondhand-db`.`chat_room` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `secondhand-db`.`based_region`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `secondhand-db`.`based_region`;

CREATE TABLE IF NOT EXISTS `secondhand-db`.`based_region`
(
    `id`          BIGINT  NOT NULL auto_increment,
    `member_id`   BIGINT  NOT NULL,
    `region_id`   BIGINT  NOT NULL,
    `represented` TINYINT NOT NULL,
    INDEX `fk_member_has_region_region1_idx` (`region_id` ASC) VISIBLE,
    INDEX `fk_member_has_region_member1_idx` (`member_id` ASC) VISIBLE,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_member_has_region_member1`
        FOREIGN KEY (`member_id`)
            REFERENCES `secondhand-db`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_member_has_region_region1`
        FOREIGN KEY (`region_id`)
            REFERENCES `secondhand-db`.`region` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
