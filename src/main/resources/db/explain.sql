###offset 버전1
explain
select distinct p1_0.id,
                p1_0.subscribe_id,
                p1_0.guid,
                p1_0.title,
                p1_0.thumbnail_url,
                p1_0.description,
                p1_0.pub_date,
                s2_0.title,
                o1_0.id is not null,
                b1_0.id is not null
from rss_post p1_0
         join
     rss_subscribe s2_0
     on s2_0.id = p1_0.subscribe_id
         left join
     open o1_0
     on p1_0.id = o1_0.post_id
         left join
     bookmark b1_0
     on p1_0.id = b1_0.post_id
         join
     rss_subscribe s1_0
     on p1_0.subscribe_id = s1_0.id
         join
     folder_subscribe f1_0
     on s1_0.id = f1_0.subscribe_id
         join
     folder f2_0
     on f1_0.folder_id = f2_0.id
         left join
     folder_member s3_0
     on f2_0.id = s3_0.folder_id
    # where
    #     (
    #                 f2_0.member_id=?
    #             or s3_0.member_id=?
    #         )
    #   and (
    #             o1_0.member_id=?
    #         or o1_0.member_id is null
    #     )
    #   and (
    #             b1_0.member_id=?
    #         or b1_0.member_id is null
    #     )
order by p1_0.pub_date desc
    limit 400,15;


###nooffset where없이,Join없이
explain
SELECT p.id,
       p.description,
       p.guid,
       p.pub_date,
       s.id AS subscribe_id,
       s.platform,
       s.title AS subscribe_title,
       s.url AS subscribe_url,
       p.thumbnail_url,
       p.title,
       CONCAT(LPAD(TO_DAYS(p.pub_date), 10, '0'), LPAD(p.id, 10, '0')) AS `cursor`
FROM rss_post p
         LEFT JOIN rss_subscribe s ON s.id = p.subscribe_id
WHERE CONCAT(LPAD(CAST(p.pub_date AS CHAR), 10, '0'), LPAD(p.id, 10, '0')) < '9999999999999999999'
ORDER BY p.pub_date DESC, `id` DESC
    LIMIT 15;


###nooffset 커스텀 커서방식 인덱스 있음
explain
select distinct p1_0.id,
                p1_0.subscribe_id,
                p1_0.guid,
                p1_0.title,
                p1_0.thumbnail_url,
                p1_0.description,
                p1_0.pub_date,
                s2_0.title,
                o1_0.id is not null,
                b1_0.id is not null,
                CONCAT(LPAD(TO_SECONDS(p1_0.pub_date), 12, '0'), LPAD(p1_0.id, 8, '0')) AS `cursor`
    #             CONCAT(LPAD(UNIX_TIMESTAMP(p1_0.pub_date), 10, '0'), LPAD(p1_0.id, 8, '0')) AS `cursor`
FROM
    rss_post p1_0
        JOIN rss_subscribe s2_0 ON s2_0.id = p1_0.subscribe_id
        LEFT JOIN open o1_0 ON p1_0.id = o1_0.post_id AND o1_0.member_id = 1
        LEFT JOIN bookmark b1_0 ON p1_0.id = b1_0.post_id AND b1_0.member_id = 1
        JOIN rss_subscribe s1_0 ON p1_0.subscribe_id = s1_0.id
        JOIN folder_subscribe f1_0 ON s1_0.id = f1_0.subscribe_id # 복합인덱스
        JOIN folder f2_0 ON f1_0.folder_id = f2_0.id
    LEFT JOIN folder_member s3_0 ON f2_0.id = s3_0.folder_id AND s3_0.member_id = 1
WHERE

    CONCAT(LPAD(TO_SECONDS(p1_0.pub_date), 12, '0'), LPAD(p1_0.id, 8, '0')) <
    '9999999999999999999'
  and (
    (
    f2_0.member_id=1
   or s3_0.member_id=1
    )
  and (
    o1_0.member_id=1
   or o1_0.member_id is null
    )
  and (
    b1_0.member_id=1
   or b1_0.member_id is null
    )
    )
order by p1_0.pub_date desc,
    p1_0.id desc
    limit 15


