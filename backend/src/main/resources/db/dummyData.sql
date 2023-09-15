DELIMITER $$

CREATE PROCEDURE InsertDummyData3()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 1000000 DO
            -- 랜덤한 title 생성 (임의로 예시를 추가하였으며, 본문에 더 다양한 한국어 내용을 추가할 수 있습니다)
            SET @title = CONCAT('중고거래 물품 #', i);

            -- 랜덤한 price 생성 (1원부터 1,000,000원까지)
            SET @price = FLOOR(RAND() * 1000000) + 1;

            -- 랜덤한 category 생성 (1부터 12까지)
            SET @category = FLOOR(RAND() * 12) + 1;

            -- 랜덤한 region_id 생성 (1168058000, 1168064000, 1168065000, 1168065500, 1168072000 중 하나)
            SET @region_id = CASE FLOOR(RAND() * 5)
                                 WHEN 0 THEN 1168058000
                                 WHEN 1 THEN 1168064000
                                 WHEN 2 THEN 1168065000
                                 WHEN 3 THEN 1168065500
                                 ELSE 1168072000
                END;

            -- 랜덤한 item_contents_id 생성 (item_contents 테이블에 삽입)
            INSERT INTO item_contents (contents, detail_image_url)
            VALUES ('랜덤한 내용 #', "https://placeimg.com/200/100/any");

    SET @item_contents_id = LAST_INSERT_ID();

    -- 랜덤한 item_counts_id 생성 (item_counts 테이블에 삽입)
    INSERT INTO item_counts (hits, like_counts, chat_counts)
    VALUES (FLOOR(RAND() * 1000), FLOOR(RAND() * 1000), FLOOR(RAND() * 1000));

    SET @item_counts_id = LAST_INSERT_ID();

    -- item 테이블에 데이터 삽입
    INSERT INTO item (title, price, status, category, thumbnail_url, created_at, updated_at, seller_id, item_counts_id, region_id, item_contents_id, is_deleted)
    VALUES (@title, @price, 'ON_SALE', @category, "[{'url':'https://placeimg.com/200/100/any'}]", NOW(), NULL, 1, @item_counts_id, @region_id, @item_contents_id, 0);

    SET i = i + 1;

    -- 100000건마다 COMMIT 수행
    IF i % 10000 = 0 THEN
      COMMIT;
    END IF;

  END WHILE;

  COMMIT;

END $$

DELIMITER ;

call InsertDummyData3();
