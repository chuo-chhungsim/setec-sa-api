package com.example.coursemanagementapi.model.entity;

import com.example.coursemanagementapi.model.dto.CategoryDTO;
import com.example.coursemanagementapi.model.dto.CourseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;


    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Course> courses;

    public CategoryDTO toCategoryDTO() {
        return CategoryDTO.builder()
                .categoryId(this.categoryId)
                .name(this.categoryName)
                .courses(
                        this.courses == null
                                ? List.of()
                                : this.courses.stream()
                                .map(c -> CourseDTO.builder()
                                        .courseId(c.getCourseId())
                                        .courseName(c.getCourseName())
                                        .description(c.getDescription())
                                        .isActive(c.getIsActive())
                                        .courseStatus(c.getCourseStatus())
                                        .build())
                                .toList()
                )
                .build();
    }
}
