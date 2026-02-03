package com.example.coursemanagementapi.model.dto;

import com.example.coursemanagementapi.model.entity.Lesson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgressDTO {
    private Long progressId;
    private LessonDTO lesson;
    private Boolean isCompleted;
    private Integer lastWatchPosition;
    private double percent;
    private AppUserDTO currentUser;
}