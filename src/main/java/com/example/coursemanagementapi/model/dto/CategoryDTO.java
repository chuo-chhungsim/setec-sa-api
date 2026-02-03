package com.example.coursemanagementapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CategoryDTO {
    private Long categoryId;
    private String name;

    private List<CourseDTO> courses;
}
