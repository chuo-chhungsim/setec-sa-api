package com.example.coursemanagementapi.service.impl;

import com.example.coursemanagementapi.exception.BadRequestException;
import com.example.coursemanagementapi.model.dto.CategoryDTO;
import com.example.coursemanagementapi.model.entity.Category;
import com.example.coursemanagementapi.model.request.CategoryRequest;
import com.example.coursemanagementapi.model.response.PaginationResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.repository.CategoryRepository;
import com.example.coursemanagementapi.service.CategoryService;
import com.example.coursemanagementapi.utils.RoleValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RoleValidation roleValidation;
    @Override
    public PayloadResponse<Category> getAllCategories(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        Page<Category> categoryPage = categoryRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(direction, sortBy))
        );
        return PayloadResponse.<Category>builder()
                .items(categoryPage.getContent())
                .pagination(PaginationResponse.fromPage(categoryPage, page, size))
                .build();
    }

    @Override
    public CategoryDTO createCategory(CategoryRequest request) {
        if (request == null){
            throw new BadRequestException("Request cannot be null");
        }
        if(categoryRepository.existsByCategoryName(request.getCategoryName())){
            throw new BadRequestException("This Category Name already exists");
        }

        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .build();
        categoryRepository.save(category);
        return category.toCategoryDTO();
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Category not found"));

        if (request == null){
            throw new BadRequestException("Request cannot be null");
        }
        if(categoryRepository.existsByCategoryName(request.getCategoryName())){
            throw new BadRequestException("This Category Name already exists");
        }
        roleValidation.validateAdmin();

        category.setCategoryName(request.getCategoryName());
        categoryRepository.save(category);
        return category.toCategoryDTO();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Category not found"));
        roleValidation.validateAdmin();
        categoryRepository.delete(category);
    }
}
