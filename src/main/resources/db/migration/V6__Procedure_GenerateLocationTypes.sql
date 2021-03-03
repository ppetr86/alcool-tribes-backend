DELIMITER $$

CREATE PROCEDURE generate50DesertsAnd50Jungles()
BEGIN
    SET @i = 0;
    REPEAT
        BEGIN
            DECLARE RandomDesertCount INT DEFAULT 1;
            DECLARE RandomDesertX INT;
            DECLARE RandomDesertY INT;

            DECLARE RandomJungleCount INT DEFAULT 1;
            DECLARE RandomJungleX INT;
            DECLARE RandomJungleY INT;

            WHILE RandomDesertCount > 0 DO
                    SET RandomDesertX = RAND() * (200) - 100;
                    SET RandomDesertY = RAND() * (200) - 100;
                    SET RandomDesertCount = (SELECT COUNT(*) FROM locations where x = RandomDesertX AND y = RandomDesertY);
            END WHILE;

            WHILE RandomJungleCount > 0 DO
                    SET RandomJungleX = RAND() * (200) - 100;
                    SET RandomJungleY = RAND() * (200) - 100;
                    SET RandomJungleCount = (SELECT COUNT(*) FROM locations where x = RandomDesertX AND y = RandomDesertY);
            END WHILE;

            INSERT INTO locations (x, y, type) VALUES (RandomDesertX, RandomDesertY, 'DESERT');
            INSERT INTO locations (x, y, type) VALUES (RandomJungleX, RandomJungleY, 'JUNGLE');
        END;
        SET @i = @i +1;
    UNTIL @i = 50
        END REPEAT;
END $$

DELIMITER ;