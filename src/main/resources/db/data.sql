-- INSERT DATA
INSERT INTO `member`(name, email, profile, oauth_pk, oauth_server, created_at)
VALUES ('test', 'test@test.com', 'profile.png', '11111', 'GITHUB', '2023-11-16');

INSERT INTO `rss_subscribe`(description, url, member_id, platform)
VALUES ('description', 'https://v2.velog.io/rss/jinny-l', '1', 'VELOG'),
       ('description', 'https://v2.velog.io/rss/ape', '1', 'VELOG');