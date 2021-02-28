/*Create a stored procedure with a
Flyway script that generates 50 deserts and 50 jungles to
random free locations on the map (coordinates from -100 to +100).*/

CREATE PROCEDURE generate50DesertsAnd50Jungles()
BEGIN
    SET @i = 0;
    REPEAT
        SELECT RAND()*(101-0)*(-1) AS 'xdesert';
        SELECT RAND()*(101-0)*(+1) AS 'ydesert';

        SELECT * FROM locations where x = xdesert x = ydesert;

        SELECT RAND()*(101-0)*(-1) AS 'xjungle';
        SELECT RAND()*(101-0)*(+1) AS 'yjungle';

    until  @i>50 END REPEAT;

END;