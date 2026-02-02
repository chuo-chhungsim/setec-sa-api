package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.CategoryDTO;
import com.example.coursemanagementapi.model.entity.Category;
import com.example.coursemanagementapi.model.request.CategoryRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<Category>>> getAllCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        PayloadResponse<Category> payloadResponse = categoryService.getAllCategories(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.<PayloadResponse<Category>>builder()
                .message("Get all categories successfully")
                .status(HttpStatus.OK)
                .payload(payloadResponse)
                .build());
    }
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryDTO createdCategory = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.<CategoryDTO>builder()
                .message("Create category successfully")
                .status(HttpStatus.CREATED)
                .payload(createdCategory)
                .build());
    }
    @PutMapping("/{category-id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable("category-id") Long categoryId, @RequestBody @Valid CategoryRequest request) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.<CategoryDTO>builder()
                .message("Update category successfully")
                .status(HttpStatus.OK)
                .payload(updatedCategory)
                .build());
    }
    @DeleteMapping("/{category-id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Delete category successfully")
                .status(HttpStatus.OK)
                .build());
    }
}
