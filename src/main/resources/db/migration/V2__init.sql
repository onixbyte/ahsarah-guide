DROP TABLE IF EXISTS firearm CASCADE;
DROP TABLE IF EXISTS modification CASCADE;


CREATE TABLE IF NOT EXISTS firearm
(
    id     BIGSERIAL   NOT NULL PRIMARY KEY,
    name   VARCHAR(64) NOT NULL,
    type   INT         NOT NULL,
    level  VARCHAR(10) NOT NULL,
    review TEXT        NULL
);


CREATE TABLE IF NOT EXISTS modification
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    firearm_id BIGINT       NOT NULL,
    name       VARCHAR(64)  NOT NULL,
    code       VARCHAR(64)  NOT NULL,
    tags       JSONB        NULL,
    note       TEXT         NULL,
    author     VARCHAR(64)  NULL,
    video_url  VARCHAR(512) NULL,
    CONSTRAINT fk_modification_firearm
        FOREIGN KEY (firearm_id)
            REFERENCES firearm (id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE INDEX idx_modification_firearm_id ON modification (firearm_id);


