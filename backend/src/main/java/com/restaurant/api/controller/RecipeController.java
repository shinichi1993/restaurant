package com.restaurant.api.controller;

import com.restaurant.api.dto.recipe.RecipeItemRequest;
import com.restaurant.api.dto.recipe.RecipeItemResponse;
import com.restaurant.api.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller quản lý định lượng món ăn (Recipe)
 *
 * QUY ƯỚC URL:
 * - GET    /api/dishes/{dishId}/recipes               → Lấy danh sách định lượng của món
 * - POST   /api/dishes/{dishId}/recipes               → Thêm nguyên liệu vào món
 * - PUT    /api/dishes/{dishId}/recipes/{recipeId}    → Sửa định lượng
 * - DELETE /api/dishes/{dishId}/recipes/{recipeId}    → Xóa nguyên liệu khỏi món
 *
 * Lưu ý:
 * - Tạm thời chưa gắn @PreAuthorize để tránh lệch permission so với SRS.
 *   Khi chốt danh sách quyền, chỉ cần bổ sung vào đây.
 */
@RestController
@RequestMapping("/api/dishes/{dishId}/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Lấy danh sách định lượng của 1 món ăn
     */
    @GetMapping
    public List<RecipeItemResponse> getRecipe(@PathVariable Long dishId) {
        return recipeService.getRecipeByDish(dishId);
    }

    /**
     * Thêm 1 nguyên liệu vào món
     */
    @PostMapping
    public RecipeItemResponse addRecipeItem(@PathVariable Long dishId,
                                            @RequestBody RecipeItemRequest request) {
        return recipeService.addRecipeItem(dishId, request);
    }

    /**
     * Cập nhật định lượng / nguyên liệu cho món
     */
    @PutMapping("/{recipeItemId}")
    public RecipeItemResponse updateRecipeItem(@PathVariable Long dishId,
                                               @PathVariable Long recipeItemId,
                                               @RequestBody RecipeItemRequest request) {
        return recipeService.updateRecipeItem(dishId, recipeItemId, request);
    }

    /**
     * Xóa 1 dòng định lượng khỏi món
     */
    @DeleteMapping("/{recipeItemId}")
    public void deleteRecipeItem(@PathVariable Long dishId,
                                 @PathVariable Long recipeItemId) {
        recipeService.deleteRecipeItem(dishId, recipeItemId);
    }
}
