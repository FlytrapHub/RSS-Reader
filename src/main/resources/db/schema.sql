DROP DATABASE IF EXISTS `rss_reader`;
CREATE SCHEMA IF NOT EXISTS `rss_reader` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `rss_reader`;
CREATE TABLE IF NOT EXISTS `member`
(
    `id`           bigint          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`         varchar(255)    NOT NULL,
    `email`        varchar(255)    NOT NULL,
    `profile`      varchar(2500)   NOT NULL,
    `oauth_pk`     bigint          NOT NULL,
    `oauth_server` enum ('GITHUB') NOT NULL,
    `created_at`   date            NOT NULL
);

CREATE TABLE IF NOT EXISTS `rss_subscribe`
(
    `id`          bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `description` varchar(2500) NOT NULL,
    `url`         varchar(2500) NOT NULL,
    `member_id`   bigint        NOT NULL,
    `platform`    varchar(25)   NOT NULL
);

CREATE TABLE IF NOT EXISTS `rss_post`
(
    `id`           bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `guid`         varchar(2500) NOT NULL,
    `subscribe_id` bigint        NOT NULL,
    `title`        varchar(2500) NOT NULL,
    `description`  longtext      NOT NULL,
    `pub_date`     timestamp     NOT NULL
);

CREATE TABLE IF NOT EXISTS `folder`
(
    `id`        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`      varchar(255) NOT NULL,
    `member_id` bigint      NOT NULL,
    `is_shared` tinyint(1)  NOT NULL DEFAULT 0,
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `folder_subscribe`
(
    `id`                 bigint     NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `subscribe_id`      bigint      NOT NULL,
    `folder_id`         bigint      NOT NULL
 );

CREATE TABLE IF NOT EXISTS `folder_member`
(
    `id`                 bigint     NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `folder_id`         bigint      NOT NULL,
    `member_id`         bigint      NOT NULL
 );
