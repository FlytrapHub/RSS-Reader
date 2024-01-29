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
    `id`       bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `title`    varchar(2500) NOT NULL,
    `url`      varchar(2500) NOT NULL,
    `platform` varchar(25)   NOT NULL
);

CREATE TABLE IF NOT EXISTS `open`
(
    `id`        bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `post_id`   bigint NOT NULL,
    `member_id` bigint NOT NULL
);
CREATE INDEX idx_post_id_member_id ON open (post_id, member_id);

CREATE TABLE IF NOT EXISTS `rss_post`
(
    `id`            bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `guid`          varchar(2500) NOT NULL,
    `subscribe_id`  bigint        NOT NULL,
    `title`         varchar(2500) NOT NULL,
    `thumbnail_url` varchar(2500),
    `description`   longtext      NOT NULL,
    `pub_date`      timestamp     NOT NULL
);
CREATE INDEX idx_pub_date ON rss_post (pub_date);
CREATE INDEX idx_subscribe_id ON rss_post (subscribe_id);

#     INDEX rss_post_cursor ((CONCAT(LPAD(TO_SECONDS(pub_date), 15, '0'), LPAD(id, 10, '0'))))

CREATE TABLE IF NOT EXISTS `folder`
(
    `id`         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`       varchar(255) NOT NULL,
    `member_id`  bigint       NOT NULL,
    `is_shared`  tinyint(1)   NOT NULL DEFAULT 0,
    `is_deleted` tinyint(1)   NOT NULL DEFAULT 0
);
CREATE INDEX idx_member_id ON folder (member_id);

CREATE TABLE IF NOT EXISTS `folder_subscribe`
(
    `id`           bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `subscribe_id` bigint        NOT NULL,
    `folder_id`    bigint        NOT NULL,
    `description`  varchar(2500) NOT NULL default 'empty'
);
CREATE INDEX idx_folder_subscribe ON folder_subscribe (folder_id, subscribe_id);
CREATE INDEX idx_subscribe_id ON folder_subscribe (subscribe_id);

CREATE TABLE IF NOT EXISTS `folder_member`
(
    `id`        bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `folder_id` bigint NOT NULL,
    `member_id` bigint NOT NULL
);
CREATE INDEX idx_folder_id ON folder_member (folder_id);
CREATE INDEX idx_member_id ON folder_member (member_id);
CREATE INDEX idx_folder_member ON folder_member (folder_id, member_id);

CREATE TABLE IF NOT EXISTS `bookmark`
(
    `id`        bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `member_id` bigint NOT NULL,
    `post_id`   bigint NOT NULL
);
CREATE INDEX idx_member_id_post_id ON bookmark (member_id, post_id);


CREATE TABLE IF NOT EXISTS `post_react`
(
    `id`        bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `member_id` bigint NOT NULL,
    `post_id`   bigint NOT NULL,
    `emoji`     bigint NOT NULL,
    FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY (`post_id`) REFERENCES `rss_post` (`id`)
);

CREATE TABLE IF NOT EXISTS `alert`
(
    `id`         bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `member_id`  bigint NOT NULL,
    `folder_id`  bigint NOT NULL,
    `service_id` bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS `alert_service`
(
    `id`      bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `service` varchar(30) NOT NULL
);

