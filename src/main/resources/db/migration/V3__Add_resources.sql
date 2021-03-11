CREATE TABLE resources
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    type       VARCHAR(100) NOT NULL,
    amount     INT          NOT NULL,
    generation INT          NOT NULL,
    updated_at BIGINT       NOT NULL,
    kingdom_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (kingdom_id) REFERENCES kingdoms (id)
);
