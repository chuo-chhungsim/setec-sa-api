package com.example.coursemanagementapi.repository;

import com.example.coursemanagementapi.model.dto.ProgressDTO;
import com.example.coursemanagementapi.model.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress,Long> {
    List<Progress> findByUserUserIdAndLessonCourseCourseId(Long userId, Long courseId);
}
