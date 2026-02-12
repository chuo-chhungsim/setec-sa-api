package com.example.coursemanagementapi.model.request;

import com.example.coursemanagementapi.model.enums.CourseStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseRequest {
    private Long categoryId;
    private String courseName;
    private String description;
    private Boolean isActive;
    private CourseStatus courseStatus;

    public String getCourseName() {
        return courseName == null ? null : courseName.trim();
    }
    public String getDescription() {
        return description == null ? null : description.trim();
    }
}
