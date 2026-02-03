package com.example.coursemanagementapi.repository;

import com.example.coursemanagementapi.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
}
