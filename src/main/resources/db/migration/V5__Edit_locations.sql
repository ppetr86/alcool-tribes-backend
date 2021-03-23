ALTER TABLE locations
    ADD COLUMN kingdom_id BIGINT,
    ADD COLUMN type       VARCHAR(10) NOT NULL,
    ADD FOREIGN KEY (kingdom_id) references kingdoms (id);

ALTER TABLE kingdoms
    DROP FOREIGN KEY kingdoms_ibfk_2;
ALTER TABLE kingdoms
    DROP COLUMN location_id;


UPDATE locations
SET locations.type = 'KINGDOM'
WHERE locations.kingdom_id IS NOT NULL;