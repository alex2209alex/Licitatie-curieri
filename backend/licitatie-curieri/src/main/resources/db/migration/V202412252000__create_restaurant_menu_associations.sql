CREATE TABLE restaurant_menu_associations
(
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id),
    menu_id       BIGINT NOT NULL REFERENCES menus(id),
    primary key (restaurant_id, menu_id)
);