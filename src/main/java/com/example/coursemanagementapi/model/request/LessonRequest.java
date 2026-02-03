package com.example.coursemanagementapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String videoUrl;

    private String contentText;

    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration must be positive")
    private Double duration;

    @NotNull(message = "Position cannot be null")
    @PositiveOrZero(message = "Position must be zero or positive")
    private Integer position;

    private Long courseId;
}
