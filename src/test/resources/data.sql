DELETE FROM buildings;
DELETE FROM troops;
DELETE FROM kingdoms;
DELETE FROM locations;
DELETE FROM players;

INSERT INTO players (id, avatar, email, password, points, username)
VALUES (1, 'http://avatar.loc/my.png', 'test@email.com', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG',
        0, 'furkesz');
INSERT INTO players (id, avatar, username, email, password, points)
VALUES (100, '', 'occupied_username', 'test@email.com', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG', 0);

INSERT INTO locations (id, x, y)
VALUES (1, 10, 10);

INSERT INTO kingdoms (id, kingdomname, player_id, location_id)
VALUES (1, 'furkesz''s kingdom', 1, 1);

INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (1, 0, 0, 1, 0, 'TOWNHALL', 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (2, 0, 0, 1, 0, 'MINE', 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (3, 0, 0, 1, 0, 'ACADEMY', 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)

VALUES (4, 0, 0, 1, 0, 'FARM', 1);

INSERT INTO resources (id, amount, generation, type, updated_at, kingdom_id)
VALUES (1, 10, 10, 'FOOD', 111, 1);
INSERT INTO resources (id, amount, generation, type, updated_at, kingdom_id)
VALUES (2, 11, 10, 'GOLD', 222, 1);

INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id)
VALUES (1, 1, 1, 0, 100, 1, 1, 1);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id)
VALUES (2, 2, 2, 1, 100, 1, 1, 1);

INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id)
VALUES (3, 10, 5, 1613070182, 20, 1, 1613070152, 1);
