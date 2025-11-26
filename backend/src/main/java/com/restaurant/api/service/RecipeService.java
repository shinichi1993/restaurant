package com.restaurant.api.service;

import com.restaurant.api.dto.recipe.RecipeItemRequest;
import com.restaurant.api.dto.recipe.RecipeItemResponse;
import com.restaurant.api.entity.Dish;
import com.restaurant.api.entity.Ingredient;
import com.restaurant.api.entity.RecipeItem;
import com.restaurant.api.repository.DishRepository;
import com.restaurant.api.repository.IngredientRepository;
import com.restaurant.api.repository.RecipeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service xử lý logic cho chức năng Recipe (định lượng món ăn)
 */
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeItemRepository recipeItemRepository;
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * Lấy danh sách định lượng theo 1 món ăn
     *
     * @param dishId ID món ăn
     * @return danh sách RecipeItemResponse
     */
    public List<RecipeItemResponse> getRecipeByDish(Long dishId) {
        List<RecipeItem> items = recipeItemRepository.findByDishId(dishId);

        // Map entity → DTO để trả cho FE
        return items.stream()
                .map(item -> RecipeItemResponse.builder()
                        .id(item.getId())
                        .ingredientId(item.getIngredient().getId())
                        .ingredientName(item.getIngredient().getName())
                        .quantityNeeded(item.getQuantityNeeded())
                        .build())
                .toList();
    }

    /**
     * Thêm 1 nguyên liệu vào món (tạo mới recipe_item)
     *
     * @param dishId ID món ăn
     * @param req    thông tin ingredientId + quantityNeeded
     */
    public RecipeItemResponse addRecipeItem(Long dishId, RecipeItemRequest req) {
        // Tìm món ăn, nếu không có thì báo lỗi
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Món ăn không tồn tại"));

        // Tìm nguyên liệu, nếu không có thì báo lỗi
        Ingredient ingredient = ingredientRepository.findById(req.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Nguyên liệu không tồn tại"));

        // Không cho thêm trùng 1 nguyên liệu cho cùng 1 món
        if (recipeItemRepository.existsByDishIdAndIngredientId(dishId, req.getIngredientId())) {
            throw new RuntimeException("Nguyên liệu này đã được cấu hình trong món");
        }

        // Tạo entity mới
        RecipeItem item = RecipeItem.builder()
                .dish(dish)
                .ingredient(ingredient)
                .quantityNeeded(req.getQuantityNeeded())
                .build();

        recipeItemRepository.save(item);

        // Trả về DTO cho FE
        return RecipeItemResponse.builder()
                .id(item.getId())
                .ingredientId(ingredient.getId())
                .ingredientName(ingredient.getName())
                .quantityNeeded(item.getQuantityNeeded())
                .build();
    }

    /**
     * Cập nhật định lượng của 1 nguyên liệu trong món
     *
     * @param dishId        ID món ăn
     * @param recipeItemId  ID dòng recipe_item
     */
    public RecipeItemResponse updateRecipeItem(Long dishId, Long recipeItemId, RecipeItemRequest req) {
        // Lấy bản ghi recipe_item cần sửa
        RecipeItem item = recipeItemRepository.findById(recipeItemId)
                .orElseThrow(() -> new RuntimeException("Dòng định lượng không tồn tại"));

        // Kiểm tra dòng này có thuộc món truyền vào hay không
        if (!item.getDish().getId().equals(dishId)) {
            throw new RuntimeException("Dòng định lượng không thuộc món này");
        }

        // Nếu FE cho phép đổi ingredientId thì cần xử lý kiểm tra trùng
        if (req.getIngredientId() != null && !req.getIngredientId().equals(item.getIngredient().getId())) {

            Ingredient newIngredient = ingredientRepository.findById(req.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Nguyên liệu mới không tồn tại"));

            if (recipeItemRepository.existsByDishIdAndIngredientId(dishId, req.getIngredientId())) {
                throw new RuntimeException("Nguyên liệu này đã tồn tại trong món");
            }

            item.setIngredient(newIngredient);
        }

        // Cập nhật số lượng cần
        if (req.getQuantityNeeded() != null) {
            item.setQuantityNeeded(req.getQuantityNeeded());
        }

        recipeItemRepository.save(item);

        return RecipeItemResponse.builder()
                .id(item.getId())
                .ingredientId(item.getIngredient().getId())
                .ingredientName(item.getIngredient().getName())
                .quantityNeeded(item.getQuantityNeeded())
                .build();
    }

    /**
     * Xóa 1 dòng định lượng khỏi món
     */
    public void deleteRecipeItem(Long dishId, Long recipeItemId) {
        RecipeItem item = recipeItemRepository.findById(recipeItemId)
                .orElseThrow(() -> new RuntimeException("Dòng định lượng không tồn tại"));

        if (!item.getDish().getId().equals(dishId)) {
            throw new RuntimeException("Dòng định lượng không thuộc món này");
        }

        recipeItemRepository.delete(item);
    }
}
