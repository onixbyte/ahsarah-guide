DROP TABLE IF EXISTS app_user CASCADE;
CREATE TABLE app_user
(
    id       BIGSERIAL    NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    CONSTRAINT app_user_username_key UNIQUE (username),
    CONSTRAINT app_user_email_key UNIQUE (email)
);

DROP TABLE IF EXISTS app_user_credential CASCADE;
CREATE TABLE app_user_credential
(
    user_id    BIGINT       NOT NULL REFERENCES app_user (id),
    provider   VARCHAR(255) NOT NULL,
    credential VARCHAR(255) NOT NULL,
    CONSTRAINT app_user_credential_pkey PRIMARY KEY (user_id, provider)
);