package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.dto.LessonDTO;
import com.example.coursemanagementapi.model.entity.Lesson;
import com.example.coursemanagementapi.model.request.LessonRequest;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import org.springframework.data.domain.Sort;

public interface LessonService {
    PayloadResponse<Lesson> getAllLessons(Integer page, Integer size, String sortBy, Sort.Direction direction);
    
    LessonDTO getLessonById(Long lessonId);
    
    LessonDTO createLesson(LessonRequest request);
    
    LessonDTO updateLesson(Long lessonId, LessonRequest request);
    
    void deleteLesson(Long lessonId);
}
