-- INSERT DATA
INSERT INTO `member`(name, email, profile, oauth_pk, oauth_server, created_at)
VALUES ('admin', 'admin@gmail.com', 'https://avatars.githubusercontent.com/u/86359180?v=4', '11111', 'GITHUB', '2023-11-16');

INSERT INTO `folder`(name, member_id, is_shared, is_deleted)
VALUES ('나의 폴더 1', 1, false, false),
       ('나의 폴더 2', 1, false, false),
       ('나의 폴더 3', 1, false, false);

INSERT INTO `folder_subscribe`(folder_id, subscribe_id)
VALUES (1, 1), (1, 2),
       (2, 1), (2, 3),
       (3, 4);

INSERT INTO `rss_subscribe`(title, description, url, platform)
VALUES ('조금씩, 꾸준히, 자주', '공부는 마라톤이다. 한꺼번에 많은 것을 하다 지치지 말고 조금씩, 꾸준히, 자주하자.', 'https://v2.velog.io/rss/jinny-l', 'VELOG'),
       ('ape.log', '구명조끼', 'https://v2.velog.io/rss/ape', 'VELOG'),
       ('janeljs.log', '', 'https://v2.velog.io/rss/janeljs', 'VELOG'),
       ('louie.log', '백엔드 개발자를 준비하고 있는 Louie입니다.', 'https://v2.velog.io/rss/louie', 'VELOG');

INSERT INTO `rss_post`(guid, subscribe_id, title, description, pub_date)
VALUES ('https://velog.io/@ape/test-l4lqt827', 2, 'test', '<p>test</p>\n', '2023-11-08 10:40:18.000000'),
       ('https://velog.io/@ape/test-9myxk9zq', 2, 'test', '<p>tttt</p>\n', '2023-11-08 10:38:36.000000'),
       ('https://velog.io/@ape/test', 2, 'test test test test test test test', '<p>test</p>\n', '2023-11-08 10:36:46.000000');

INSERT INTO `bookmark`(member_id, post_id)
VALUES (1, 1), (1, 2), (1, 3);