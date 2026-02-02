package com.example.coursemanagementapi.model.entity;

import com.example.coursemanagementapi.model.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;

    public CategoryDTO toCategoryDTO() {
        return CategoryDTO.builder()
                .categoryId(this.categoryId)
                .name(this.categoryName)
                .build();
    }
}
