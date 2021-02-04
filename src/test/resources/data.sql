INSERT INTO kingdoms (id, kingdomname, fk_player_id)
VALUES (1, 'test1', 1);
INSERT INTO kingdoms (id, kingdomname, fk_player_id)
VALUES (2, 'test2', 2);
INSERT INTO kingdoms (id, kingdomname, fk_player_id)
VALUES (3, 'test2', 3);

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


INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, fk_kingdom_id)
VALUES (1, 10, 20, 200, 100, 1, 100, 1);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, fk_kingdom_id)
VALUES (2, 20, 10, 201, 100, 2, 101, 1);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, fk_kingdom_id)
VALUES (3, 10, 20, 202, 100, 3, 102, 1);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, fk_kingdom_id)
VALUES (4, 20, 10, 200, 100, 1, 100, 2);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, fk_kingdom_id)
VALUES (5, 10, 20, 200, 100, 1, 100, 2);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, fk_kingdom_id)
VALUES (6, 20, 10, 200, 100, 1, 100, 2);


INSERT INTO players (id, username, password, points) VALUES (1, 'Mark', 'markmark', 0);
INSERT INTO players (id, username, password, points) VALUES (2, 'Zdenek', 'zdenekzdenek', 0);
INSERT INTO players (id, username, password, points) VALUES (3, 'Petr', 'petrpetr', 0);
INSERT INTO players (id, username, password, points) VALUES (4, 'Ahmed', 'ahmedahmed', 0);
INSERT INTO players (id, avatar, email, password, points, username)
    VALUES (5,'http://avatar.loc/my.png', 'test@email.com', 'password', 1, 'user1');