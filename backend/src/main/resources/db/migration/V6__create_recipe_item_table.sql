-- V6__create_recipe_item_table.sql
-- Tạo bảng recipe_item để lưu định lượng nguyên liệu cho từng món ăn

CREATE TABLE recipe_item (
    id              BIGSERIAL PRIMARY KEY,
    dish_id         BIGINT NOT NULL,
    ingredient_id   BIGINT NOT NULL,
    quantity_needed NUMERIC(10,2) NOT NULL, -- số gram / ml cần cho 1 phần món

    CONSTRAINT fk_recipe_item_dish
        FOREIGN KEY (dish_id) REFERENCES dish(id),

    CONSTRAINT fk_recipe_item_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredient(id),

    -- Đảm bảo 1 nguyên liệu chỉ xuất hiện 1 lần trong 1 món
    CONSTRAINT uk_recipe_item_dish_ingredient UNIQUE (dish_id, ingredient_id)
);
