package com.example.coursemanagementapi.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LessonRequest {
    private Long courseId;
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Video URL cannot be blank")
    private String videoUrl;

    @NotBlank(message = "Content text cannot be blank")
    private String contentText;

    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration must be positive")
    private Double duration;

    @NotNull(message = "Position cannot be null")
    @PositiveOrZero(message = "Position must be zero or positive")
    private Integer position;

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }
    public void setContentText(String contentText) {
        this.contentText = contentText == null ? null : contentText.trim();
    }
}
