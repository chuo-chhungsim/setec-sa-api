package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.request.CourseRequest;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import org.springframework.data.domain.Sort;

public interface CourseService {
    PayloadResponse<Course> getAllCourses(Integer page, Integer size, String sortBy, Sort.Direction direction);

    CourseDTO createCourse(CourseRequest request);
}
