CREATE TABLE players
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL,
    avatar   VARCHAR(100) NOT NULL,
    points   INT          NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE kingdoms
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    kingdomname VARCHAR(100) NOT NULL,
    player_id   BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (player_id) REFERENCES players (id)
);

CREATE TABLE buildings
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    type        VARCHAR(100) NOT NULL,
    level       INT          NOT NULL,
    hp          INT          NOT NULL,
    started_at  BIGINT       NOT NULL,
    finished_at BIGINT       NOT NULL,
    kingdom_id  BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (kingdom_id) REFERENCES kingdoms (id)
);

CREATE TABLE troops
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    level       INT    NOT NULL,
    hp          INT    NOT NULL,
    attack      INT    NOT NULL,
    defence     INT    NOT NULL,
    started_at  BIGINT NOT NULL,
    finished_at BIGINT NOT NULL,
    kingdom_id  BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (kingdom_id) REFERENCES kingdoms (id)
);
