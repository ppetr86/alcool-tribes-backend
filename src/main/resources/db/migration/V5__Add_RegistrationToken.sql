CREATE TABLE registration_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    expire_at DATETIME,
    time_stamp TIMESTAMP,
    token VARCHAR(20),
    player_id BIGINT NOT NULL,
    is_expired BIT(1),
    PRIMARY KEY (id),
    FOREIGN KEY (player_id) REFERENCES players(id)
);