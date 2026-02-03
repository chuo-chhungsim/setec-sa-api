package com.example.coursemanagementapi.model.entity;

import com.example.coursemanagementapi.model.dto.ProgressDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "lesson_progress")
public class Progress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long progressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", unique = true)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private AppUser user;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "last_watch_position")
    private Integer lastWatchPosition;

    @Column(name = "percent")
    private Double percent;

    public ProgressDTO toProgressDTO() {
        return ProgressDTO.builder()
                .progressId(progressId)
                .lesson(lesson.toLessonDTO())
                .currentUser(user.toAppUserDTOWithoutTimestamps())
                .isCompleted(isCompleted)
                .lastWatchPosition(lastWatchPosition)
                .percent(percent)
                .build();
    }
}
