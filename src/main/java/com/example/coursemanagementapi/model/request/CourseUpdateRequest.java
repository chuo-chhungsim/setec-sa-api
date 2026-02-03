package com.example.coursemanagementapi.model.request;

import com.example.coursemanagementapi.model.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseUpdateRequest {
    private Long categoryId;

    @NotBlank(message = "Course name cannot be blank")
    private String courseName;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Is active cannot be null")
    private Boolean isActive;
    @NotNull(message = "Course status cannot be null")
    private CourseStatus courseStatus;

    public String getCourseName() {
        return courseName == null ? null : courseName.trim();
    }
    public String getDescription() {
        return description == null ? null : description.trim();
    }
}