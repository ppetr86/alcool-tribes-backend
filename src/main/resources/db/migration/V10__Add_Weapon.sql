CREATE TABLE weapons
(
    id                BINARY       NOT NULL UNIQUE,
    name              VARCHAR(100) NOT NULL,
    type              VARCHAR(100) NOT NULL,
    fk_kingdom        BIGINT       NOT NULL,
    magazine_size     INTEGER      NOT NULL,
    is_automaticable  BIT(1),
    is_semiautoable   BIT(1),
    is_concealable    BIT(1),
    shooting_distance INTEGER      NOT NULL,
    blade_length      INTEGER      NOT NULL,
    discriminator     VARCHAR(100) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (fk_kingdom) REFERENCES kingdoms (id)
);