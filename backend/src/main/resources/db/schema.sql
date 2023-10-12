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

create table if not exists category
(
    id      bigint       not null,
    title   varchar(16)  not null,
    img_url varchar(300) not null,
    primary key (id),
    constraint title_UNIQUE
        unique (title)
);

create table if not exists item_contents
(
    id               bigint auto_increment
        primary key,
    contents         text             null,
    detail_image_url text             null,
    is_deleted       bit default b'0' null
);

create table if not exists item_counts
(
    id          bigint auto_increment
        primary key,
    hits        bigint           not null,
    like_counts bigint           not null,
    chat_counts bigint           not null,
    is_deleted  bit default b'0' null
);

create table if not exists member
(
    id              bigint auto_increment
        primary key,
    member_id       varchar(16)             not null,
    profile_img_url varchar(300)            null,
    oauth           enum ('GITHUB', 'NONE') null
);

create index memberId_UNIQUE
    on member (member_id);

create table if not exists region
(
    id       bigint      not null,
    city     varchar(45) not null,
    county   varchar(45) null,
    district varchar(45) not null,
    primary key (id)
);

create table if not exists based_region
(
    id          bigint auto_increment
        primary key,
    member_id   bigint  not null,
    region_id   bigint  not null,
    represented tinyint not null,
);

create index fk_member_has_region_member1_idx
    on based_region (member_id);

create index fk_member_has_region_region1_idx
    on based_region (region_id);

create table if not exists item
(
    id               bigint auto_increment
        primary key,
    title            varchar(45)      not null,
    price            int              null,
    status           varchar(16)      not null,
    category         varchar(45)      not null,
    thumbnail_url    varchar(300)     null,
    created_at       datetime         not null,
    updated_at       datetime         null,
    seller_id        bigint           not null,
    item_counts_id   bigint           not null,
    region_id        bigint           not null,
    item_contents_id bigint           not null,
    is_deleted       bit default b'0' null,
);

DROP TABLE IF EXISTS chatroom;
create table if not exists chatroom
(
    id         bigint auto_increment primary key,
    chatroom_id varchar(255) not null unique,
    item_id    bigint   not null,
    seller_id bigint not null,
    buyer_id   bigint   not null,
    created_at datetime null,
    chatroom_status enum('EMPTY', 'SELLER_ONLY', 'BUYER_ONLY', 'FULL') default 'FULL' null,
);
CREATE UNIQUE INDEX idx_my_chatroom_id on chatroom (chatroom_id);

create table if not exists chat_log
(
    id           bigint auto_increment
        primary key,
    contents     varchar(300) null,
    sender_id    bigint       not null,
    reciver_id   bigint       not null,
    chat_room_id bigint       not null,
);

create index fk_chat_log_member1_idx
    on chat_log (sender_id);

create index fk_chat_log_member2_idx
    on chat_log (reciver_id);

create index fk_chat_log_member_chat_about_item1_idx
    on chat_log (chat_room_id);

create index fk_item_has_member_item1_idx
    on chatroom (item_id);

create index fk_item_has_member_member1_idx
    on chatroom (buyer_id);

create index fk_item_item_contents1_idx
    on item (item_contents_id);

create index fk_item_item_image1_idx
    on item (item_counts_id);

create index fk_item_member_idx
    on item (seller_id);

create index fk_item_region1_idx
    on item (region_id);

create fulltext index fulltext_address
    on region (city, county, district);

create table if not exists wishlist
(
    id         bigint auto_increment
        primary key,
    member_id  bigint   not null,
    item_id    bigint   not null,
    created_at datetime null,
);

create index fk_member_has_item_item1_idx
    on wishlist (item_id);

create index fk_member_has_item_member1_idx
    on wishlist (member_id);

create table if not exists chatbubble
(
   id       bigint auto_increment primary key ,
   room_id  varchar(25),
   sender   bigint not null ,
   receiver bigint not null ,
   message text,
   created_at datetime null
);



SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
