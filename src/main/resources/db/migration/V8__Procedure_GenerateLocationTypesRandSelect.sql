DELIMITER $$

CREATE PROCEDURE generateNDesertsAndJunglesRandSelect(IN n INTEGER)
BEGIN
            UPDATE locations SET type = 'DESERT' where type = 'EMPTY' ORDER BY RAND() LIMIT n;
            UPDATE locations SET type = 'JUNGLE' where type = 'EMPTY' ORDER BY RAND() LIMIT n;
END $$

DELIMITER ;