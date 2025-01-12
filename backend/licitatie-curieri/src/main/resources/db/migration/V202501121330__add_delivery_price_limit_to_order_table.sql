DELETE
FROM order_menu_item_associations;

DELETE
FROM orders;

ALTER TABLE orders
    ALTER COLUMN delivery_price DROP NOT NULL;

ALTER TABLE orders
    ADD COLUMN delivery_price_limit DECIMAL NOT NULL DEFAULT 1;

ALTER TABLE orders
    ALTER COLUMN delivery_price_limit drop DEFAULT;
