-- TABLES
create table character_items
(
    instance_id  INTEGER
        primary key autoincrement,
    character_id INTEGER not null
        references characters,
    item_id      INTEGER not null
        references items,
    slot_name    TEXT,
    grid_index   INTEGER
);

create table characters
(
    id       INTEGER
        primary key autoincrement,
    name     TEXT,
    race_id  INTEGER
        references races,
    class_id INTEGER
        references classes,
    gender   TEXT
);

create table classes
(
    id        INTEGER
        primary key autoincrement,
    name      TEXT not null,
    bonus_str INTEGER default 0,
    bonus_dex INTEGER default 0,
    bonus_int INTEGER default 0
);

create table items
(
    id        INTEGER
        primary key autoincrement,
    name      TEXT not null,
    type      TEXT not null,
    weight    REAL not null,
    stat_str  INTEGER default 0,
    stat_dex  INTEGER default 0,
    stat_int  INTEGER default 0,
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
INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, icon_path)
VALUES ('Iron Sword', 'MAIN_HAND', 5.0, 3, 0, 0, 'iron_sword.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, icon_path)
VALUES ('Leather Cap', 'HEAD', 1.0, 0, 1, 0, 'leather_cap.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, icon_path)
VALUES ('Old Staff', 'MAIN_HAND', 2.0, 0, 0, 4, 'wood_staff.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, icon_path)
VALUES ('Rusty Dagger', 'MAIN_HAND', 1.5, 1, 2, 0, 'dagger.png');

-- Races Seeded Data
INSERT INTO races (name, base_str, base_dex, base_int, weight_capacity_modifier)
VALUES ('Human', 5, 5, 5, 1.0);

INSERT INTO races (name, base_str, base_dex, base_int, weight_capacity_modifier)
VALUES ('Elf', 3, 7, 8, 0.8);

INSERT INTO races (name, base_str, base_dex, base_int, weight_capacity_modifier)
VALUES ('Orc', 10, 3, 2, 1.3);

-- Classes Seeded Data
INSERT INTO classes (name, bonus_str, bonus_dex, bonus_int)
VALUES ('Warrior', 5, 2, 0);

INSERT INTO classes (name, bonus_str, bonus_dex, bonus_int)
VALUES ('Mage', 0, 2, 8);

INSERT INTO classes (name, bonus_str, bonus_dex, bonus_int)
VALUES ('Archer', 2, 6, 2);