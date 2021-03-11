CREATE TABLE locations
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    x  INT    NOT NULL,
    y  INT    NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE kingdoms
    ADD COLUMN location_id BIGINT,
    ADD FOREIGN KEY (location_id) REFERENCES locations (id);