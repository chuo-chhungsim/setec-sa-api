package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.request.CourseRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<Course>>> getAllCourses(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        PayloadResponse<Course> payloadResponse = courseService.getAllCourses(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.<PayloadResponse<Course>>builder()
                .message("Get all courses successfully")
                .status(HttpStatus.OK)
                .payload(payloadResponse)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody CourseRequest request) {
        CourseDTO course = courseService.createCourse(request);
        return ResponseEntity.ok(ApiResponse.<CourseDTO>builder()
                .message("Create course successfully")
                .status(HttpStatus.CREATED)
                .payload(course)
                .build());
    }
}
