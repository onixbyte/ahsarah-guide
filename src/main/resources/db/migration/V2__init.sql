DROP TABLE IF EXISTS firearm CASCADE;
DROP TABLE IF EXISTS modification CASCADE;


CREATE TABLE IF NOT EXISTS firearm
(
    id     BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(64) NOT NULL,
    type   INT         NOT NULL,
    level  INT         NOT NULL,
    review TEXT        NULL
);


CREATE TABLE IF NOT EXISTS modification
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firearm_id BIGINT       NOT NULL,
    name       VARCHAR(64)  NOT NULL,
    code       VARCHAR(64)  NOT NULL,
    tags       JSON         NULL,
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


