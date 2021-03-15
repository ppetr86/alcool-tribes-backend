DELIMITER $$

CREATE PROCEDURE generateEmptyLocations(IN n INTEGER)
BEGIN
    SET @i = 0;
    REPEAT
        BEGIN
            SET @y = 0;
            REPEAT
                BEGIN
                    INSERT INTO locations (x, y, type) VALUES (@i-n, @y*(-1)+n, 'EMPTY');
                END;
                SET @y = @y + 1;
            UNTIL @y = n * 2 + 1
                END REPEAT;
        END;
        SET @i = @i + 1;
    UNTIL @i = n * 2 + 1
        END REPEAT;
END $$

DELIMITER ;