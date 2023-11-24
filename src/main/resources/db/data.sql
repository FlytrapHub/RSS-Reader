-- INSERT DATA
INSERT INTO `member`(name, email, profile, oauth_pk, oauth_server, created_at)
VALUES ('test', 'test@test.com', 'profile.png', '11111', 'GITHUB', '2023-11-16');

INSERT INTO `rss_subscribe`(title, description, url, platform)
VALUES ('조금씩, 꾸준히, 자주', '공부는 마라톤이다. 한꺼번에 많은 것을 하다 지치지 말고 조금씩, 꾸준히, 자주하자.', 'https://v2.velog.io/rss/jinny-l', 'VELOG'),
       ('ape.log', '구명조끼', 'https://v2.velog.io/rss/ape', 'VELOG'),
       ('janeljs.log', '', 'https://v2.velog.io/rss/janeljs', 'VELOG'),
       ('louie.log', '백엔드 개발자를 준비하고 있는 Louie입니다.', 'https://v2.velog.io/rss/louie', 'VELOG');

INSERT INTO `folder`(name, member_id, is_shared, is_deleted)
VALUES ('나의 폴더 1', 1, false, false),
       ('나의 폴더 2', 1, false, false),
       ('나의 폴더 3', 1, false, false);

INSERT INTO `folder_subscribe`(folder_id, subscribe_id)
VALUES (1, 1), (1, 2),
       (2, 1), (2, 3),
       (3, 4);