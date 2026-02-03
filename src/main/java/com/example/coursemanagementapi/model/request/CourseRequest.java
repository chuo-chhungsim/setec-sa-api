package com.example.coursemanagementapi.model.request;

import com.example.coursemanagementapi.model.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {
    private String courseName;
    private String description;
    private Boolean isActive;
    private CourseStatus courseStatus;
    private Long categoryId;
    private Long lessonId;
}
