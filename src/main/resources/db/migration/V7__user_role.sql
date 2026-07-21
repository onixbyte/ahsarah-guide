CREATE TABLE user_role
(
    user_id BIGINT      NOT NULL REFERENCES app_user (id),
    role    VARCHAR(32) NOT NULL,
    CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role)
);
