DELIMITER $$

CREATE PROCEDURE generateNDesertsAndJunglesRandSelect(IN n INTEGER)
BEGIN
            UPDATE locations SET type = 'DESERT' WHERE type = 'EMPTY' ORDER BY RAND() LIMIT n;
            UPDATE locations SET type = 'JUNGLE' WHERE type = 'EMPTY' ORDER BY RAND() LIMIT n;
END $$

DELIMITER ;

CALL generateNDesertsAndJunglesRandSelect(50);