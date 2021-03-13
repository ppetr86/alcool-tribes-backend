DELIMITER $$

CREATE PROCEDURE generateEmptyLocations(IN n INTEGER)
BEGIN
    SET @i = 0;
    REPEAT
        BEGIN
            SET @y = 0;
            REPEAT
                BEGIN
                    INSERT INTO locations (x, y, type) VALUES (@i-100, @y*(-1)+100, 'EMPTY');
                END;
                SET @y = @y + 1;
            UNTIL @y = n
                END REPEAT;
        END;
        SET @i = @i + 1;
    UNTIL @i = n
        END REPEAT;
END $$

DELIMITER ;

