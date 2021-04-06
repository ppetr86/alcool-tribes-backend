DELETE
FROM buildings;
DELETE
FROM troops;
DELETE
FROM kingdoms;
DELETE
FROM locations;
DELETE
FROM players;


INSERT INTO players (id, avatar, email, password, points, username, is_account_verified, role_type)
VALUES (1, 'http://avatar.loc/my.png', 'test@email.com', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG',
        0, 'furkesz', true, 'ROLE_USER');
INSERT INTO players (id, avatar, username, email, password, points, is_account_verified, role_type)
VALUES (2, '', 'zdenek', 'test@email.com', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG', 0, true, 'ROLE_USER');
INSERT INTO players (id, avatar, username, email, password, points, is_account_verified, role_type)
VALUES (100, '', 'occupied_username', 'test@email.com', '$2a$10$NaD84OJw/IJCe6jExv21Reah0hCOgZDhp1N8D.ovKHeKtAYzevcQG',
        0, true, 'ROLE_USER');

INSERT INTO kingdoms (id, kingdomname, player_id)
VALUES (1, 'furkesz''s kingdom', 1);
INSERT INTO kingdoms (id, kingdomname, player_id)
VALUES (2, 'zdenek kingdom', 2);

INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (1, 10, 10, 1, 'KINGDOM');
INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (2, 20, 20, 2, 'KINGDOM');

INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (3, 0, 0, NULL, 'EMPTY');
INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (4, 0, 1, NULL, 'EMPTY');
INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (5, 0, -1, NULL, 'EMPTY');
INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (6, -1, 0, NULL, 'EMPTY');
INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (7, 1, 0, NULL, 'EMPTY');
INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (8, 1, -1, NULL, 'EMPTY');
INSERT INTO locations (id, x, y, kingdom_id, type)
VALUES (9, 5, -5, NULL, 'EMPTY');

INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (1, 0, 0, 1, 0, 'TOWNHALL', 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (2, 0, 0, 1, 0, 'MINE', 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (3, 0, 0, 1, 0, 'ACADEMY', 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (4, 0, 0, 1, 0, 'FARM', 1);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (5, 0, 0, 1, 0, 'TOWNHALL', 2);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (6, 0, 0, 1, 0, 'MINE', 2);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (7, 0, 0, 1, 0, 'ACADEMY', 2);
INSERT INTO buildings (id, finished_at, hp, level, started_at, type, kingdom_id)
VALUES (8, 0, 0, 1, 0, 'FARM', 2);


INSERT INTO resources (id, amount, generation, type, updated_at, kingdom_id)
VALUES (1, 10, 10, 'FOOD', 111, 1);
INSERT INTO resources (id, amount, generation, type, updated_at, kingdom_id)
VALUES (2, 11, 10, 'GOLD', 222, 1);
INSERT INTO resources (id, amount, generation, type, updated_at, kingdom_id)
VALUES (3, 10, 10, 'FOOD', 111, 2);
INSERT INTO resources (id, amount, generation, type, updated_at, kingdom_id)
VALUES (4, 11, 10, 'GOLD', 222, 2);

INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id, is_home)
VALUES (1, 1, 1, 0, 100, 1, 1, 1,true);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id, is_home)
VALUES (2, 2, 2, 1, 100, 1, 1, 1,true);

INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id, is_home)
VALUES (3, 10, 5, 1613070182, 20, 1, 1613070152, 1,true);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id, is_home)
VALUES (4, 1, 1, 0, 100, 1, 1, 2,true);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id, is_home)
VALUES (5, 2, 2, 1, 100, 1, 1, 2,true);
INSERT INTO troops (id, attack, defence, finished_at, hp, level, started_at, kingdom_id, is_home)
VALUES (6, 10, 5, 1613070182, 20, 1, 1613070152, 2,true);