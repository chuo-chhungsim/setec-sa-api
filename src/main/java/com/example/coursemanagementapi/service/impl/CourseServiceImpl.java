package com.example.coursemanagementapi.service.impl;

import com.example.coursemanagementapi.exception.BadRequestException;
import com.example.coursemanagementapi.exception.ItemNotFoundException;
import com.example.coursemanagementapi.exception.UnauthorizedException;
import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.entity.Category;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.entity.Lesson;
import com.example.coursemanagementapi.model.request.CourseRequest;
import com.example.coursemanagementapi.model.request.CourseUpdateRequest;
import com.example.coursemanagementapi.model.response.PaginationResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.repository.CategoryRepository;
import com.example.coursemanagementapi.repository.CourseRepository;
import com.example.coursemanagementapi.repository.LessonRepository;
import com.example.coursemanagementapi.service.CourseService;
import com.example.coursemanagementapi.utils.RoleValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final LessonRepository lessonRepository;
    private final RoleValidation roleValidation;

    @Override
    public PayloadResponse<CourseDTO> getAllCourses(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        Page<Course> coursePage = courseRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(direction, sortBy))
        );
        List<CourseDTO> courseDTOS = coursePage.getContent().stream()
                .map(Course::toCourseDTO)
                .toList();

        return PayloadResponse.<CourseDTO>builder()
                .items(courseDTOS)
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

        return course.toCourseDTO();
    }

    @Override
    public CourseDTO getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(()-> new BadRequestException("Course not found.")).toCourseDTO();
    }

    @Override
    public CourseDTO updateCourse(Long courseId, CourseUpdateRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ItemNotFoundException("Course not found."));

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean isAdmin = roleValidation.isAdmin();
        boolean isOwner = course.getUser() != null && currentEmail.equalsIgnoreCase(course.getUser().getEmail());

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You are not authorized to update this course.");
        }

        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setIsActive(request.getIsActive());
        course.setCourseStatus(request.getCourseStatus());

        Category  category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found: " + request.getCategoryId()));
        course.setCategory(category);

        courseRepository.save(course);

        return course.toCourseDTO();
    }

    @Override
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new BadRequestException("Course not found."));

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = roleValidation.isAdmin();
        boolean isOwner = course.getUser() != null && currentEmail.equalsIgnoreCase(course.getUser().getEmail());
        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You are not authorized to delete this course.");
        }


        courseRepository.delete(course);
    }


}
