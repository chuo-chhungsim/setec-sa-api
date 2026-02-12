package com.example.coursemanagementapi.model.dto;

import com.example.coursemanagementapi.model.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseDTO {
    private Long courseId;
    private String courseName;
    private String description;
    private Boolean isActive;
    private CourseStatus courseStatus;
    private LessonDTO lesson;
}