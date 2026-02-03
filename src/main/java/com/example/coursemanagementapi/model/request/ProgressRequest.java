package com.example.coursemanagementapi.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressRequest {
    private Long lessonId;
    private Boolean isCompleted;
    private Integer lastWatchPosition;
    private Double percentageWatched;
}
