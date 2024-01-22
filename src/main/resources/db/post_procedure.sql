CREATE DEFINER=CURRENT_USER PROCEDURE insert_rss_posts(IN total_rows INT, IN batch_size INT)

BEGIN
    DECLARE i INT DEFAULT 134;
    DECLARE batch_counter INT DEFAULT 0;

    WHILE i <= total_rows DO
            START TRANSACTION;

            WHILE batch_counter < batch_size AND i <= total_rows DO
                    INSERT INTO rss_post (guid, subscribe_id, title, thumbnail_url, description, pub_date)
                    VALUES
                        -- Batched values go here, you can generate or fetch them dynamically
                        (UUID(), FLOOR(RAND() * 10) + 1, CONCAT('Title ', i), CONCAT('Thumbnail_URL ', i), CONCAT('Description ', i), NOW());
                    -- Add more values as needed

                    SET i = i + 1;
                    SET batch_counter = batch_counter + 1;
END WHILE;

COMMIT;

SET batch_counter = 0;
END WHILE;
END;

CALL insert_rss_posts(2000000, 100);
