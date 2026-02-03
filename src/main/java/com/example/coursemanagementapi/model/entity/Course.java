package com.example.coursemanagementapi.model.entity;

import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "courses")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name")
    private String courseName;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "course_img")
    private String courseImg;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_status")
    private CourseStatus courseStatus;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private AppUser user;

    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    private Lesson lesson;

    public CourseDTO toCourseDTO() {
        return CourseDTO.builder()
                .courseId(courseId)
                .courseName(courseName)
                .description(description)
                .isActive(isActive)
                .courseStatus(courseStatus)
                .lesson(lesson == null ? null : lesson.toLessonDTO())
                .build();
    }
}
