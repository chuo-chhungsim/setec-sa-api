package com.example.coursemanagementapi.service.impl;

import com.example.coursemanagementapi.exception.BadRequestException;
import com.example.coursemanagementapi.exception.ItemNotFoundException;
import com.example.coursemanagementapi.model.dto.LessonDTO;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.entity.Lesson;
import com.example.coursemanagementapi.model.request.LessonRequest;
import com.example.coursemanagementapi.model.response.PaginationResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.repository.CourseRepository;
import com.example.coursemanagementapi.repository.LessonRepository;
import com.example.coursemanagementapi.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public PayloadResponse<LessonDTO> getAllLessons(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        Page<Lesson> lessonPage = lessonRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(direction, sortBy))
        );
        List<LessonDTO> lessonDTOS = lessonPage.getContent().stream()
                .map(Lesson::toLessonDTO)
                .toList();

        return PayloadResponse.<LessonDTO>builder()
                .items(lessonDTOS)
                .pagination(PaginationResponse.fromPage(lessonPage, page, size))
                .build();
    }

    @Override
    public LessonDTO getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ItemNotFoundException("Lesson not found with id: " + lessonId));
        return lesson.toLessonDTO();
    }

    @Override
    @Transactional
    public LessonDTO createLesson(LessonRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }

        // Validate course if provided
        Course course = null;
        if (request.getCourseId() != null) {
            course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ItemNotFoundException("Course not found with id: " + request.getCourseId()));
            
            // Check if course already has a lesson (OneToOne relationship)
            if (course.getLesson() != null) {
                throw new BadRequestException("Course already has a lesson assigned");
            }
        }

        Lesson lesson = Lesson.builder()
                .title(request.getTitle() != null ? request.getTitle().trim() : null)
                .videoUrl(request.getVideoUrl())
                .contentText(request.getContentText())
                .duration(request.getDuration() != null ? request.getDuration() : 0.0)
                .position(request.getPosition() != null ? request.getPosition() : 0)
                .course(course)
                .build();

        lesson = lessonRepository.save(lesson);
        log.info("Lesson created: {}", lesson.getLessonId());
        
        return lesson.toLessonDTO();
    }

    @Override
    @Transactional
    public LessonDTO updateLesson(Long lessonId, LessonRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ItemNotFoundException("Lesson not found with id: " + lessonId));

        // Update fields
        if (request.getTitle() != null) {
            lesson.setTitle(request.getTitle().trim());
        }
        if (request.getVideoUrl() != null) {
            lesson.setVideoUrl(request.getVideoUrl());
        }
        if (request.getContentText() != null) {
            lesson.setContentText(request.getContentText());
        }
        if (request.getDuration() != null) {
            lesson.setDuration(request.getDuration());
        }
        if (request.getPosition() != null) {
            lesson.setPosition(request.getPosition());
        }

        // Update course relationship if provided
        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ItemNotFoundException("Course not found with id: " + request.getCourseId()));
            
            // Check if the new course already has a lesson (unless it's the same lesson)
            if (course.getLesson() != null && !course.getLesson().getLessonId().equals(lessonId)) {
                throw new BadRequestException("Course already has a lesson assigned");
            }
            
            lesson.setCourse(course);
        }

        lesson = lessonRepository.save(lesson);
        log.info("Lesson updated: {}", lesson.getLessonId());
        
        return lesson.toLessonDTO();
    }

    @Override
    @Transactional
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ItemNotFoundException("Lesson not found with id: " + lessonId));
        
        lessonRepository.delete(lesson);
        log.info("Lesson deleted: {}", lessonId);
    }
}
