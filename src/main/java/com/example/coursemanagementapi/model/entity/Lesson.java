package com.example.coursemanagementapi.model.entity;

import com.example.coursemanagementapi.model.dto.LessonDTO;
import com.example.coursemanagementapi.model.dto.ProgressDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    private String title;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "content_text")
    private String contentText;

    private double duration;

    private int position;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", unique = true)
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progress> progresses = new ArrayList<>();

    public LessonDTO toLessonDTO(){
        return LessonDTO.builder()
                .lessonId(lessonId)
                .title(title)
                .videoUrl(videoUrl)
                .contentText(contentText)
                .duration(duration)
                .position(position)
                .build();
    }
}
