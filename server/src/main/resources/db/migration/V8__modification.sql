BEGIN;

CREATE TABLE IF NOT EXISTS modification_new
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    user_id     BIGINT       NULL REFERENCES app_user (id),
    firearm_id  BIGINT       NOT NULL REFERENCES firearm (id),
    name        VARCHAR(64)  NOT NULL,
    code        VARCHAR(64)  NOT NULL,
    tags        JSONB        NOT NULL DEFAULT '[]'::JSONB,
    note        TEXT         NULL,
    author      VARCHAR(64)  NULL,
    video_url   VARCHAR(512) NULL,
    accessories JSONB        NOT NULL DEFAULT '[]'::JSONB,
    status      INTEGER      NOT NULL DEFAULT 0,
    create_by   BIGINT       NULL,
    create_time TIMESTAMPTZ  NOT NULL,
    CONSTRAINT tags_must_be_string_array CHECK (
        jsonb_typeof(tags) = 'array' AND
        NOT jsonb_path_exists(tags, '$[*] ? (@.type() != "string")')
        )
);

DO
$$
    DECLARE
        v_super_user_id BIGINT;
    BEGIN
        -- Added explicit table reference and LIMIT 1 to prevent multiple-row errors
        SELECT app_user.id
        INTO v_super_user_id
        FROM app_user
                 LEFT JOIN user_role ur ON app_user.id = ur.user_id
        WHERE ur.role = 'SUPER_USER'
        LIMIT 1;

        -- Explicitly check for NULL rather than evaluating the integer
        IF v_super_user_id IS NOT NULL THEN
            INSERT INTO modification_new(id, user_id, firearm_id, name, code, tags, note, author,
                                         video_url, accessories, status, create_by, create_time)
            SELECT m.id,
                   NULL,
                   m.firearm_id,
                   m.name,
                   m.code,
                   COALESCE(m.tags, '[]'::JSONB),
                   m.note,
                   m.author,
                   m.video_url,
                   COALESCE(m.accessories, '[]'::JSONB),
                   2,
                   v_super_user_id,
                   now()
            FROM modification m;
        ELSE
            -- Fixed RAISE NOTICE parameter formatting
            RAISE NOTICE 'Super user not found. Please execute the manual SQL commands after adding a super user.';
        END IF;
    END
$$;

ALTER INDEX idx_modification_firearm_id RENAME TO idx_modification_lagacy_firearm_id;

CREATE INDEX idx_modification_firearm_id ON modification_new (firearm_id);

ALTER TABLE modification
    RENAME TO modification_legacy;
ALTER TABLE modification_new
    RENAME TO modification;

COMMIT;