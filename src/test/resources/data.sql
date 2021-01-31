INSERT INTO kingdom_entity (id) VALUES (1);
INSERT INTO kingdom_entity (id) VALUES (2);

INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_buildings_kingdom)
VALUES (1, 988, 100, 1, 888, 1, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_buildings_kingdom)
VALUES (2, 1008, 100, 1, 888, 0, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_buildings_kingdom)
VALUES (13, 948, 100, 1, 888, 1, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_buildings_kingdom)
VALUES (14, 1008, 200, 1, 888, 0, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_buildings_kingdom)
VALUES (15, 948, 100, 1, 888, 2, 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, fk_buildings_kingdom)
VALUES (16, 978, 150, 1, 888, 3, 2);

CREATE TABLE player (id int, avatar varchar, email varchar, password varchar, points int, username varchar(20), kingdom_id int);

INSERT INTO players (id, avatar, email, password, points, username, kingdom_id)
VALUES (1,'http://avatar.loc/my.png', 'test@email.com', 'password', 1, 'user1', 1);