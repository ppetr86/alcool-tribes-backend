INSERT INTO players (id, avatar, email, password, points, username)
VALUES (1,'http://avatar.loc/my.png', 'test@email.com', 'password', 1, 'user1');

INSERT INTO king.players (id, avatar, email, password, points, username)
VALUES (2, 'http://avatar.loc/my.png', 'test@email.com', 'password', 1, 'user1');

INSERT INTO king.players (id, avatar, email, password, points, username)
VALUES (3, 'http://avatar.loc/my.png', 'test2@email.com', 'password', 2, 'user2');


INSERT INTO players (id, username, password) VALUES (1, 'Mark', 'markmark');
INSERT INTO players (id, username, password) VALUES (2, 'Zdenek', 'zdenekzdenek');
INSERT INTO players (id, username, password) VALUES (3, 'Petr', 'petrpetr');
INSERT INTO players (id, username, password) VALUES (4, 'Ahmed', 'ahmedahmed');

CREATE TABLE player (id int, avatar varchar, email varchar, password varchar, points int, username varchar(20), kingdom_id int);

INSERT INTO players (id, avatar, email, password, points, username, kingdom_id)
VALUES (1,'http://avatar.loc/my.png', 'test@email.com', 'password', 1, 'user1', 1);