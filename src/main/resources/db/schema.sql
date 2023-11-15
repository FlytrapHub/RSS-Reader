CREATE SCHEMA IF NOT EXISTS 'rss_reader';

CREATE TABLE IF NOT EXISTS `member` (
    `id`	bigint	NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`	varchar(255)	NOT NULL,
    `email`	varchar(255)	NOT NULL,
    `profile`	varchar(2500)	NOT NULL,
    `oauth_pk`	bigint	NOT NULL,
    `oauth_server`	enum('GITHUB')	NOT NULL,
    `created_at`	date	NOT NULL
);