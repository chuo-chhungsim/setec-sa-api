package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.request.CourseRequest;
import com.example.coursemanagementapi.model.request.CourseUpdateRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<PayloadResponse<CourseDTO>>> getAllCourses(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        PayloadResponse<CourseDTO> payloadResponse = courseService.getAllCourses(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.<PayloadResponse<CourseDTO>>builder()
                .message("Get all courses successfully")
                .status(HttpStatus.OK)
                .payload(payloadResponse)
                .build());
    }

    @GetMapping({"/{course-id}"})
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseById(@PathVariable("course-id") Long courseId){
        CourseDTO course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(ApiResponse.<CourseDTO>builder()
                .message("Get course by id successfully")
                .status(HttpStatus.OK)
                .payload(course)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody @Valid CourseRequest request) {
        CourseDTO course = courseService.createCourse(request);
        return ResponseEntity.ok(ApiResponse.<CourseDTO>builder()
                .message("Create course successfully")
                .status(HttpStatus.CREATED)
                .payload(course)
                .build());
    }

    @PutMapping({"/{course-id}"})
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(@PathVariable("course-id") Long courseId, @RequestBody @Valid CourseUpdateRequest request) {
        CourseDTO course = courseService.updateCourse(courseId, request);
        return ResponseEntity.ok(ApiResponse.<CourseDTO>builder()
                .message("Update course successfully")
                .status(HttpStatus.OK)
                .payload(course)
                .build());
    }

    @DeleteMapping({"/{course-id}"})
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable("course-id") Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Delete course successfully")
                .status(HttpStatus.OK)
                .build());
    }

}
