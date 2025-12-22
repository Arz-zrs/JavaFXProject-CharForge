-- TABLES
CREATE TABLE IF NOT EXISTS character_items
(
    instance_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    character_id INTEGER NOT NULL REFERENCES characters ON DELETE CASCADE,
    item_id      INTEGER NOT NULL REFERENCES items ON DELETE CASCADE,
    slot_name    TEXT,
    grid_index   INTEGER
);

CREATE TABLE IF NOT EXISTS characters
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    name     TEXT,
    race_id  INTEGER REFERENCES races ON DELETE CASCADE,
    class_id INTEGER REFERENCES classes ON DELETE CASCADE,
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

CREATE TABLE IF NOT EXISTS races
(
    id                       INTEGER
        primary key autoincrement,
    name                     TEXT not null,
    base_hp                  INTEGER default 100,
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
VALUES ('Leather Cap', 'HEAD', 1.0, 0, 1, 0, 2, 5, 0, 'leather_cap.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Old Staff', 'MAIN_HAND', 2.0, 0, 0, 4, 0, 0, 8, 'wood_staff.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Rusty Dagger', 'OFFHAND', 1.5, 1, 2, 0, 0, 0, 4, 'dagger.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Chainmail', 'BODY', 1.0, 1, 3, 0, 5, 10, 0, 'chainmail.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Hunter Bow', 'MAIN_HAND', 1.5, 1, 5, 0, 0, 0, 6, 'hunter_bow.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Iron Armor', 'BODY', 9, 10, 2, 0, 5, 15, 0, 'iron_armor.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Iron Helmet', 'HEAD', 4, 4, 0, 0, 3, 10, 0, 'iron_helmet.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Iron Leggings', 'LEGS', 3, 5, 1, 0, 2, 8, 0, 'iron_leggings.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Iron Shield', 'OFFHAND', 2, 3, 0, 0, 0, 5, 0, 'iron_shield.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Leather Boots', 'LEGS', 0.5, 1, 3, 0, 2, 3, 0, 'leather_boots.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Mage Hat', 'HEAD', 0.5, 0, 2, 10, 3, 1, 0, 'mage_hat.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Magic Necklace', 'UTILITY', 0.5, 0, 0, 15, 0, 0, 0, 'magic_necklace.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Magic Tome', 'OFFHAND', 0.5, 0, 1, 12, 0, 0, 5, 'magic_tome.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Quiver', 'UTILITY', 1, 1, 6, 0, 0, 0, 7, 'quiver.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Pendant of Vigor', 'UTILITY', 0.5, 10, 4, 0, 5, 0, 0, 'vigor_pendant.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('Mage Robe', 'BODY', 0.5, 0, 1, 15, 5, 2, 0, 'mage_robe.png');

INSERT INTO items (name, type, weight, stat_str, stat_dex, stat_int, stat_hp, stat_ap, stat_atk, icon_path)
VALUES ('A Normal Stick', 'MAIN_HAND', 0.1, 999, 999, 999, 999, 999, 999, 'placeholder.png');

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
