package com.example.coursemanagementapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDTO {
    private Long lessonId;
    private String title;
    private String videoUrl;
    private String contentText;
    private Double duration;
    private int position;
}
