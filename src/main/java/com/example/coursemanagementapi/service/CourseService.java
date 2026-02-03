package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.request.CourseRequest;
import com.example.coursemanagementapi.model.request.CourseUpdateRequest;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import org.springframework.data.domain.Sort;

public interface CourseService {
    PayloadResponse<CourseDTO> getAllCourses(Integer page, Integer size, String sortBy, Sort.Direction direction);

    CourseDTO createCourse(CourseRequest request);

    CourseDTO getCourseById(Long courseId);

    CourseDTO updateCourse(Long courseId, CourseUpdateRequest request);

    void deleteCourse(Long courseId);
}
