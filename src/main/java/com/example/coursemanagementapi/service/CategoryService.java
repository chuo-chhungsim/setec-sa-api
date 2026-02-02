package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.dto.CategoryDTO;
import com.example.coursemanagementapi.model.entity.Category;
import com.example.coursemanagementapi.model.request.CategoryRequest;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;

public interface CategoryService {
    PayloadResponse<Category> getAllCategories(Integer page, Integer size, String sortBy, Sort.Direction direction);

    CategoryDTO createCategory(@Valid CategoryRequest request);

    CategoryDTO updateCategory(Long categoryId, @Valid CategoryRequest request);

    void deleteCategory(Long categoryId);
}
