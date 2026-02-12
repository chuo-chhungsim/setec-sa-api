package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.EnrollmentDTO;
import com.example.coursemanagementapi.model.request.EnrollmentRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enrollments")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Enrollment Management", description = "Course enrollment management operations")
public class EnrollmentController extends BaseController {
    private final EnrollmentService enrollmentService;

    @Operation(
            summary = "Get all enrollments",
            description = """
                    ### Purpose
                    Retrieve a paginated list of all course enrollments.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Returns paginated results with sorting options
                    - Default pagination: page 1, size 10
                    - Default sorting: by enrollmentDate in descending order
                    - Supports custom sorting by any enrollment field
                    """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<EnrollmentDTO>>> getAllEnrollments(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "enrollmentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        PayloadResponse<EnrollmentDTO> payloadResponse = enrollmentService.getAllEnrollments(page, size, sortBy, direction);
        return ok(payloadResponse, "Get all enrollments successfully");
    }

    @Operation(
            summary = "Get enrollment by ID",
            description = """
                    ### Purpose
                    Retrieve detailed information about a specific enrollment.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Enrollment ID must exist
                    - Returns complete enrollment information including user and course details
                    - Returns 404 if enrollment not found
                    """
    )
    @GetMapping("/{enrollment-id}")
    public ResponseEntity<ApiResponse<EnrollmentDTO>> getEnrollmentById(@PathVariable("enrollment-id") Long enrollmentId) {
        EnrollmentDTO enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        return ok(enrollment, "Get enrollment successfully");
    }

    @Operation(
            summary = "Get enrollments by user ID",
            description = """
                    ### Purpose
                    Retrieve all enrollments for a specific user.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - User ID must exist
                    - Returns list of all courses the user is enrolled in
                    - Includes enrollment status, view count, and join count
                    """
    )
    @GetMapping("/user/{user-id}")
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getEnrollmentsByUserId(@PathVariable("user-id") Long userId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByUserId(userId);
        return ok(enrollments, "Get enrollments by user ID successfully");
    }

    @Operation(
            summary = "Get enrollments by course ID",
            description = """
                    ### Purpose
                    Retrieve all enrollments for a specific course.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Course ID must exist
                    - Returns list of all users enrolled in the course
                    - Useful for viewing course enrollment statistics
                    """
    )
    @GetMapping("/course/{course-id}")
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getEnrollmentsByCourseId(@PathVariable("course-id") Long courseId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ok(enrollments, "Get enrollments by course ID successfully");
    }

    @Operation(
            summary = "Create a new enrollment",
            description = """
                    ### Purpose
                    Enroll a user in a course.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - User ID and Course ID are required
                    - User cannot be enrolled in the same course twice
                    - Default enrollment status is LISTED if not specified
                    - Enrollment date is automatically set to current timestamp
                    - View count and join count are initialized to 0
                    - Returns the created enrollment with generated ID
                    """
    )
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentDTO>> createEnrollment(@RequestBody @Valid EnrollmentRequest request) {
        EnrollmentDTO enrollment = enrollmentService.createEnrollment(request);
        return created(enrollment, "Create enrollment successfully");
    }

    @Operation(
            summary = "Update an enrollment",
            description = """
                    ### Purpose
                    Update an existing enrollment's information.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Enrollment ID must exist
                    - All fields are optional (only provided fields will be updated)
                    - User ID and Course ID must exist if provided
                    - Cannot create duplicate enrollment if changing user/course
                    - Returns the updated enrollment information
                    """
    )
    @PutMapping("/{enrollment-id}")
    public ResponseEntity<ApiResponse<EnrollmentDTO>> updateEnrollment(
            @PathVariable("enrollment-id") Long enrollmentId,
            @RequestBody @Valid EnrollmentRequest request
    ) {
        EnrollmentDTO enrollment = enrollmentService.updateEnrollment(enrollmentId, request);
        return ok(enrollment, "Update enrollment successfully");
    }

    @Operation(
            summary = "Delete an enrollment",
            description = """
                    ### Purpose
                    Remove a user's enrollment from a course.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Enrollment ID must exist
                    - This operation cannot be undone
                    - Returns success message upon deletion
                    """
    )
    @DeleteMapping("/{enrollment-id}")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable("enrollment-id") Long enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
        return ok(null, "Delete enrollment successfully");
    }

    @Operation(
            summary = "Increment view count",
            description = """
                    ### Purpose
                    Increment the view count for an enrollment (track course views).
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Enrollment ID must exist
                    - Increments the view count by 1
                    - Useful for tracking how many times a user has viewed a course
                    - Returns updated enrollment with new view count
                    """
    )
    @PostMapping("/{enrollment-id}/increment-view")
    public ResponseEntity<ApiResponse<EnrollmentDTO>> incrementViewCount(@PathVariable("enrollment-id") Long enrollmentId) {
        EnrollmentDTO enrollment = enrollmentService.incrementViewCount(enrollmentId);
        return ok(enrollment, "View count incremented successfully");
    }

    @Operation(
            summary = "Increment join count",
            description = """
                    ### Purpose
                    Increment the join count for an enrollment (track course participation).
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Enrollment ID must exist
                    - Increments the join count by 1
                    - Useful for tracking how many times a user has actively joined/participated in a course
                    - Returns updated enrollment with new join count
                    """
    )
    @PostMapping("/{enrollment-id}/increment-join")
    public ResponseEntity<ApiResponse<EnrollmentDTO>> incrementJoinCount(@PathVariable("enrollment-id") Long enrollmentId) {
        EnrollmentDTO enrollment = enrollmentService.incrementJoinCount(enrollmentId);
        return ok(enrollment, "Join count incremented successfully");
    }
}
