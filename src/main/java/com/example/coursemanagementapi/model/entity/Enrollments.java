package com.example.coursemanagementapi.model.entity;

import com.example.coursemanagementapi.model.enums.EnrollmentStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Enrollments extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime  enrollmentDate;

    private Long viewCount;

    private Long joinCount;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus enrollmentStatus;

    private String notes;

}
