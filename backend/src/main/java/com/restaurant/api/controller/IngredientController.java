package com.restaurant.api.controller;

import com.restaurant.api.dto.ingredient.IngredientRequest;
import com.restaurant.api.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * API lấy danh sách nguyên liệu
     */
    @GetMapping
    public Object getAll() {
        return ingredientService.getAll();
    }

    /**
     * API tạo mới nguyên liệu
     */
    @PostMapping
    public Object create(@RequestBody IngredientRequest req) {
        return ingredientService.create(req);
    }

    /**
     * API cập nhật nguyên liệu
     */
    @PutMapping("/{id}")
    public Object update(
            @PathVariable Long id,
            @RequestBody IngredientRequest req
    ) {
        return ingredientService.update(id, req);
    }

    /**
     * API xóa nguyên liệu
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ingredientService.delete(id);
    }
}
