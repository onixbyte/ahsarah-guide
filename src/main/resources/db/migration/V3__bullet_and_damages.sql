-- 创建新表
DROP TABLE IF EXISTS firearm_new;
CREATE TABLE firearm_new
(
    id            BIGSERIAL   NOT NULL,
    name          VARCHAR(64) NOT NULL,
    type          INTEGER     NOT NULL,
    level         VARCHAR(10) NOT NULL,
    calibre       VARCHAR(20) NOT NULL,
    fire_rate     INTEGER     NOT NULL,
    armour_damage INTEGER     NOT NULL,
    body_damage   INTEGER     NOT NULL,
    review        TEXT        NULL,
    CONSTRAINT firearm_new_pkey PRIMARY KEY (id)
);

-- 迁移数据
INSERT INTO firearm_new(id, name, type, level, calibre, fire_rate, armour_damage, body_damage,
                        review)
SELECT id,
       name,
       type,
       level,
       '',
       0,
       0,
       0,
       review
FROM firearm;

-- 处理外键（关键步骤）
-- 先删除指向旧表的外键约束
ALTER TABLE modification
    DROP CONSTRAINT fk_modification_firearm;

-- 重命名旧表和索引
ALTER TABLE firearm
    RENAME TO firearm_legacy;
ALTER INDEX firearm_pkey RENAME TO firearm_legacy_pkey;

-- 重命名新表和索引
ALTER TABLE firearm_new
    RENAME TO firearm;
ALTER INDEX firearm_new_pkey RENAME TO firearm_pkey;

-- 重新建立外键，指向新的 firearm 表
ALTER TABLE modification
    ADD CONSTRAINT fk_modification_firearm
        FOREIGN KEY (firearm_id) REFERENCES firearm (id);

-- 序列所有权与名称修正
ALTER SEQUENCE firearm_id_seq RENAME TO firearm_legacy_id_seq;
ALTER SEQUENCE firearm_new_id_seq RENAME TO firearm_id_seq;
ALTER SEQUENCE firearm_id_seq OWNED BY firearm.id;

-- 更新序列计数器
SELECT setval('firearm_id_seq', coalesce(max(id), 1))
FROM firearm;

-- 删除旧表
DROP TABLE IF EXISTS firearm_legacy CASCADE;