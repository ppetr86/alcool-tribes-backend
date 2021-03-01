ALTER TABLE locations
    ADD COLUMN kingdom_id BIGINT,
    ADD COLUMN type       varchar(10),
    ADD FOREIGN KEY (kingdom_id) references kingdoms (id);

UPDATE locations
SET locations.type = 'KINGDOM'
WHERE locations.kingdom_id IS NOT NULL;

/*locations table should have a kingdom_id column which is nullable
  kingdom should habe a location too*/
