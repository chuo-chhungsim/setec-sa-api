package com.example.coursemanagementapi.model.request;

import com.example.coursemanagementapi.model.enums.EnrollmentStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EnrollmentRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;
    
    private EnrollmentStatus enrollmentStatus;
    
    private String notes;
}
