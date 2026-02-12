package com.example.coursemanagementapi.model.dto;

import com.example.coursemanagementapi.model.enums.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EnrollmentDTO {
    private Long enrollmentId;
    private Long userId;
    private Long courseId;
    private String userName;
    private String courseName;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
    private LocalDateTime enrollmentDate;
    
    private Long viewCount;
    private Long joinCount;
    private EnrollmentStatus enrollmentStatus;
    private String notes;
}
