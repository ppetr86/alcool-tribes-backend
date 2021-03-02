/*Create a stored procedure with a
Flyway script that generates 50 deserts and 50 jungles to
random free locations on the map (coordinates from -100 to +100).*/

CREATE PROCEDURE generate50DesertsAnd50Jungles()
BEGIN
    /* loop to get 50 locations,
       while loop to check if generated location is already in db
       if in DB, generate again, else move on*/
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
                    SET RandomDesertCount = SELECT COUNT(*) FROM locations where x = RandomDesertX AND y = RandomDesertY;
                END WHILE;

            WHILE RandomJungleCount > 0 DO
                    SET RandomJungleX = RAND() * (200) - 100;
                    SET RandomJungleY = RAND() * (200) - 100;
                    SET RandomJungleCount = SELECT COUNT(*) FROM locations where x = RandomJungleX AND y = RandomJungleY;
                END WHILE;

            INSERT INTO locations (x, y, type) VALUES (RandomDesertX, RandomDesertY, 'DESERT');
            INSERT INTO locations (x, y, type) VALUES (RandomJungleX, RandomJungleY, 'JUNGLE');
            SET @i = @i + 1;
            UNTIL @i = 49;
        END;
    END REPEAT;
END;