ALTER TABLE players
    ADD COLUMN is_account_verified BIT(1);

CREATE TABLE registration_tokens(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    token      VARCHAR(20) NOT NULL,
    time_stamp TIMESTAMP   NOT NULL,
    expire_at  TIMESTAMP   NOT NULL,
    player_id  BIGINT      NOT NULL,
    is_expired BOOLEAN     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (player_id) REFERENCES players (id)
);