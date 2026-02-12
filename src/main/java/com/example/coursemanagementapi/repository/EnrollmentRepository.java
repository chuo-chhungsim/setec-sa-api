package com.example.coursemanagementapi.repository;

import com.example.coursemanagementapi.model.entity.Enrollments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollments, Long> {
    Optional<Enrollments> findByAppUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
    
    List<Enrollments> findByAppUser_UserId(Long userId);
    
    List<Enrollments> findByCourse_CourseId(Long courseId);
    
    boolean existsByAppUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
}
