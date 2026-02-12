package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.CategoryDTO;
import com.example.coursemanagementapi.model.entity.Category;
import com.example.coursemanagementapi.model.request.CategoryRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category Management", description = "Course category management operations")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Get all categories",
            description = """
                    ### Purpose
                    Retrieve a paginated list of all course categories.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Returns paginated results with sorting options
                    - Default pagination: page 1, size 10
                    - Default sorting: by createdAt in ascending order
                    - Supports custom sorting by any category field
                    """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<CategoryDTO>>> getAllCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        PayloadResponse<CategoryDTO> payloadResponse = categoryService.getAllCategories(page, size, sortBy, direction);
        return ok(payloadResponse, "Get all categories successfully");
    }
    
    @Operation(
            summary = "Create a new category",
            description = """
                    ### Purpose
                    Create a new course category in the system.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Category name must be unique
                    - Category name cannot be blank
                    - Returns the created category with generated ID
                    """
    )
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryDTO createdCategory = categoryService.createCategory(request);
        return created(createdCategory, "Create category successfully");
    }
    
    @Operation(
            summary = "Update a category",
            description = """
                    ### Purpose
                    Update an existing category's information.
                    
                    ### Access
                    Authenticated users - Admin role required.
                    
                    ### Notes
                    - Category ID must exist
                    - Category name must be unique (if changed)
                    - Only Admin users can update categories
                    - Returns the updated category information
                    """
    )
    @PutMapping("/{category-id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable("category-id") Long categoryId, @RequestBody @Valid CategoryRequest request) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, request);
        return ok(updatedCategory, "Update category successfully");
    }
    
    @Operation(
            summary = "Delete a category",
            description = """
                    ### Purpose
                    Delete a category from the system.
                    
                    ### Access
                    Authenticated users - Admin role required.
                    
                    ### Notes
                    - Category ID must exist
                    - Only Admin users can delete categories
                    - This operation cannot be undone
                    - Returns success message upon deletion
                    """
    )
    @DeleteMapping("/{category-id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ok(null, "Delete category successfully");
    }
}
