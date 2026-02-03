package com.example.coursemanagementapi.model.dto;

import com.example.coursemanagementapi.model.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long courseId;
    private String courseName;
    private String description;
    private Boolean isActive;
    private CourseStatus courseStatus;
    private LessonDTO lesson;
}