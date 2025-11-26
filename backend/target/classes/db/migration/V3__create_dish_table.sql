-- V3__create_dish_table.sql
CREATE TABLE dish (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(12,2) NOT NULL DEFAULT 0,
    image_url VARCHAR(500),

    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_dish_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON DELETE CASCADE
);
