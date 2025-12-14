-- TABLES
create table character_items
(
    instance_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    character_id INTEGER NOT NULL REFERENCES characters,
    item_id      INTEGER NOT NULL REFERENCES items,
    slot_name    TEXT,
    grid_index   INTEGER
);

create table characters
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    name     TEXT,
    race_id  INTEGER REFERENCES races,
    class_id INTEGER REFERENCES classes,
    gender   TEXT
);

CREATE TABLE IF NOT EXISTS classes
(
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    name           TEXT NOT NULL,
    bonus_str      INTEGER DEFAULT 0,
    bonus_dex      INTEGER DEFAULT 0,
    bonus_int      INTEGER DEFAULT 0,
    attack_scaling TEXT    DEFAULT 'STR'
);


CREATE TABLE IF NOT EXISTS items
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    name      TEXT NOT NULL,
    type      TEXT NOT NULL,
    weight    REAL NOT NULL,
    stat_str  INTEGER DEFAULT 0,
    stat_dex  INTEGER DEFAULT 0,
    stat_int  INTEGER DEFAULT 0,
    stat_hp   INTEGER DEFAULT 0,
    stat_ap   INTEGER DEFAULT 0,
    stat_atk  INTEGER DEFAULT 0,
    icon_path TEXT
);

create table races
(
    id                       INTEGER
        primary key autoincrement,
    name                     TEXT not null,
    base_str                 INTEGER default 0,
    base_dex                 INTEGER default 0,
    base_int                 INTEGER default 0,
    weight_capacity_modifier REAL    default 1.0
);

-- SEEDED DATA
-- Items Seeded Data
INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Iron Sword', 'MAIN_HAND', 5.0, 3, 0, 0, 0, 0, 15, 'iron_sword.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Leather Cap', 'HEAD', 1.0, 0, 1, 0, 10, 5, 0, 'leather_cap.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Old Staff', 'MAIN_HAND', 2.0, 0, 0, 4, 0, 0, 8, 'wood_staff.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Rusty Dagger', 'MAIN_HAND', 1.5, 1, 2, 0, 0, 0, 4, 'dagger.png');

-- Races Seeded Data
INSERT INTO races (name, base_str, base_dex, base_int, weight_capacity_modifier)
VALUES ('Human', 5, 5, 5, 1.0);

INSERT INTO races (name, base_str, base_dex, base_int, weight_capacity_modifier)
VALUES ('Elf', 3, 7, 8, 0.8);

INSERT INTO races (name, base_str, base_dex, base_int, weight_capacity_modifier)
VALUES ('Orc', 10, 3, 2, 1.3);

-- Classes Seeded Data
INSERT INTO classes (name, bonus_str, bonus_dex, bonus_int, attack_scaling)
VALUES ('Warrior', 5, 2, 0, 'STR');

INSERT INTO classes (name, bonus_str, bonus_dex, bonus_int, attack_scaling)
VALUES ('Mage', 0, 2, 8, 'INT');

INSERT INTO classes (name, bonus_str, bonus_dex, bonus_int, attack_scaling)
VALUES ('Archer', 2, 6, 2, 'DEX');

select *
from classes