CREATE SCHEMA IF NOT EXISTS 'rss_reader';

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
    `id`        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `guid` varchar(2500) NULL,
    `subscribe_id` bigint NOT NULL,
    `title` varchar(2500) NULL,
    `description` longtext NULL,
    `pub_date` timestamp NULL
);

CREATE TABLE IF NOT EXISTS `folder`
(
    `id`        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`      varchar(255) NOT NULL,
    `member_id` bigint      NOT NULL,
    `is_shared` tinyint(1)  NOT NULL DEFAULT 0
);
