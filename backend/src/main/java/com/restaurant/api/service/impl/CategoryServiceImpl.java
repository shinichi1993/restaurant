package com.restaurant.api.service.impl;

import com.restaurant.api.dto.category.CategoryCreateRequest;
import com.restaurant.api.dto.category.CategoryResponse;
import com.restaurant.api.dto.category.CategoryUpdateRequest;
import com.restaurant.api.entity.Category;
import com.restaurant.api.repository.CategoryRepository;
import com.restaurant.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CategoryServiceImpl – xử lý logic CRUD cho danh mục.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryCreateRequest request) {

        // Tạo entity mới
        Category c = new Category();
        c.setName(request.getName());
        c.setDescription(request.getDescription());

        // Lưu DB
        categoryRepository.save(c);

        // Convert sang DTO trả về
        return toResponse(c);
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, CategoryUpdateRequest request) {

        // Lấy category theo id
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category không tồn tại"));

        // Cập nhật dữ liệu
        c.setName(request.getName());
        c.setDescription(request.getDescription());

        // Lưu lại
        categoryRepository.save(c);

        return toResponse(c);
    }

    @Override
    public void delete(Long id) {

        // Kiểm tra tồn tại
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category không tồn tại");
        }

        // Xóa theo id
        categoryRepository.deleteById(id);
    }

    /**
     * Hàm convert Entity → DTO Response
     */
    private CategoryResponse toResponse(Category c) {
        CategoryResponse res = new CategoryResponse();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setDescription(c.getDescription());
        res.setCreatedAt(c.getCreatedAt());
        return res;
    }
}
