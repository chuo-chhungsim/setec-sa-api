package com.example.coursemanagementapi.service.impl;

import com.example.coursemanagementapi.exception.BadRequestException;
import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.entity.Category;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.entity.Lesson;
import com.example.coursemanagementapi.model.request.CourseRequest;
import com.example.coursemanagementapi.model.response.PaginationResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.repository.CategoryRepository;
import com.example.coursemanagementapi.repository.CourseRepository;
import com.example.coursemanagementapi.repository.LessonRepository;
import com.example.coursemanagementapi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final LessonRepository lessonRepository;

    @Override
    public PayloadResponse<Course> getAllCourses(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        Page<Course> coursePage = courseRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(direction, sortBy))
        );
        return PayloadResponse.<Course>builder()
                .items(coursePage.getContent())
                .pagination(PaginationResponse.fromPage(coursePage, page, size))
                .build();
    }

    @Override
    public CourseDTO createCourse(CourseRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found: " + request.getCategoryId()));

        Course course = Course.builder()
                .courseName(request.getCourseName() == null ? null : request.getCourseName().trim())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .courseStatus(request.getCourseStatus())
                .category(category)
                .build();

        // Save course first so it has an id (needed for FK course_id)
        course = courseRepository.save(course);

        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new BadRequestException("Lesson not found: " + request.getLessonId()));

            if (lesson.getCourse() != null) {
                throw new BadRequestException("Lesson already assigned to a course");
            }

            lesson.setCourse(course);        // owning side
            lessonRepository.save(lesson);

            // optional: keep both sides consistent if Course has lesson field
            course.setLesson(lesson);
        }

        return course.toCourseDTO();
    }


}
