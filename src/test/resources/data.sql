INSERT INTO kingdom_entity (id) VALUES (1);
INSERT INTO kingdom_entity (id) VALUES (2);

INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_kingdom_id)
VALUES (1, 988, 100, 1, 888, 1, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_kingdom_id)
VALUES (2, 1008, 100, 1, 888, 0, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_kingdom_id)
VALUES (13, 948, 100, 1, 888, 1, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_kingdom_id)
VALUES (14, 1008, 200, 1, 888, 0, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_kingdom_id)
VALUES (15, 948, 100, 1, 888, 2, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_kingdom_id)
VALUES (16, 978, 150, 1, 888, 3, 2);