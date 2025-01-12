ALTER TABLE menus
    RENAME TO menu_items;

DELETE
FROM menu_items;

DROP TABLE restaurant_menu_associations;

ALTER TABLE menu_items
    ADD COLUMN restaurant_id BIGINT NOT NULL REFERENCES restaurants (id);

ALTER TABLE menu_items
    ADD COLUMN was_removed BOOLEAN NOT NULL;

ALTER SEQUENCE menus_seq RENAME
    TO menu_items_seq;
