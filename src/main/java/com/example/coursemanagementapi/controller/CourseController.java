package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.CourseDTO;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.request.CourseRequest;
import com.example.coursemanagementapi.model.request.CourseUpdateRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Course Management", description = "Course management operations")
public class CourseController extends BaseController {
    private final CourseService courseService;

    @Operation(
            summary = "Get all courses",
            description = """
                    ### Purpose
                    Retrieve a paginated list of all courses.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Returns paginated results with sorting options
                    - Default pagination: page 1, size 10
                    - Default sorting: by createdAt in ascending order
                    - Supports custom sorting by any course field
                    - Includes course details and associated lesson information
                    """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<CourseDTO>>> getAllCourses(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        PayloadResponse<CourseDTO> payloadResponse = courseService.getAllCourses(page, size, sortBy, direction);
        return ok(payloadResponse, "Get all courses successfully");
    }

    @Operation(
            summary = "Get course by ID",
            description = """
                    ### Purpose
                    Retrieve detailed information about a specific course.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Course ID must exist
                    - Returns complete course information including lesson details
                    - Returns 404 if course not found
                    """
    )
    @GetMapping({"/{course-id}"})
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseById(@PathVariable("course-id") Long courseId){
        CourseDTO course = courseService.getCourseById(courseId);
        return ok(course, "Get course by id successfully");
    }

    @Operation(
            summary = "Create a new course",
            description = """
                    ### Purpose
                    Create a new course in the system.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Category ID must exist
                    - Course name and description are required
                    - Can optionally assign a lesson to the course
                    - Returns the created course with generated ID
                    """
    )
    @PostMapping
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody @Valid CourseRequest request) {
        CourseDTO course = courseService.createCourse(request);
        return created(course, "Create course successfully");
    }

    @Operation(
            summary = "Update a course",
            description = """
                    ### Purpose
                    Update an existing course's information.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Course ID must exist
                    - All fields in request are required
                    - Category ID must exist if provided
                    - Returns the updated course information
                    """
    )
    @PutMapping({"/{course-id}"})
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(@PathVariable("course-id") Long courseId, @RequestBody @Valid CourseUpdateRequest request) {
        CourseDTO course = courseService.updateCourse(courseId, request);
        return ok(course, "Update course successfully");
    }

    @Operation(
            summary = "Delete a course",
            description = """
                    ### Purpose
                    Delete a course from the system.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Course ID must exist
                    - This operation cannot be undone
                    - Returns success message upon deletion
                    """
    )
    @DeleteMapping({"/{course-id}"})
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable("course-id") Long courseId) {
        courseService.deleteCourse(courseId);
        return ok(null, "Delete course successfully");
    }

}
