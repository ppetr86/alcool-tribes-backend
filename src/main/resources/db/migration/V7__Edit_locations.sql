ALTER TABLE locations
    ADD COLUMN kingdom_id BIGINT,
    ADD COLUMN type       varchar(10),
    ADD FOREIGN KEY (kingdom_id) references kingdoms (id);

ALTER TABLE kingdoms
    DROP FOREIGN KEY kingdoms_ibfk_2;
ALTER TABLE kingdoms
    DROP COLUMN location_id;


UPDATE locations
SET locations.type = 'KINGDOM'
WHERE locations.kingdom_id IS NOT NULL;

/*locations table should have a kingdom_id column which is nullable
*/
