package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.LessonDTO;
import com.example.coursemanagementapi.model.entity.Lesson;
import com.example.coursemanagementapi.model.request.LessonRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.LessonService;
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
@RequestMapping("/api/v1/lessons")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Lesson Management", description = "Course lesson management operations")
public class LessonController extends BaseController {
    private final LessonService lessonService;

    @Operation(
            summary = "Get all lessons",
            description = """
                    ### Purpose
                    Retrieve a paginated list of all lessons.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Returns paginated results with sorting options
                    - Default pagination: page 1, size 10
                    - Default sorting: by createdAt in ascending order
                    - Supports custom sorting by any lesson field
                    """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<LessonDTO>>> getAllLessons(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        PayloadResponse<LessonDTO> payloadResponse = lessonService.getAllLessons(page, size, sortBy, direction);
        return ok(payloadResponse, "Get all lessons successfully");
    }

    @Operation(
            summary = "Get lesson by ID",
            description = """
                    ### Purpose
                    Retrieve detailed information about a specific lesson.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Lesson ID must exist
                    - Returns complete lesson information
                    - Returns 404 if lesson not found
                    """
    )
    @GetMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<LessonDTO>> getLessonById(@PathVariable("lesson-id") Long lessonId) {
        LessonDTO lesson = lessonService.getLessonById(lessonId);
        return ok(lesson, "Get lesson successfully");
    }

    @Operation(
            summary = "Create a new lesson",
            description = """
                    ### Purpose
                    Create a new lesson in the system.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Title, video URL, content text, duration, and position are required
                    - Duration must be positive
                    - Position must be zero or positive
                    - Can optionally assign to a course (OneToOne relationship)
                    - Returns the created lesson with generated ID
                    """
    )
    @PostMapping
    public ResponseEntity<ApiResponse<LessonDTO>> createLesson(@RequestBody @Valid LessonRequest request) {
        LessonDTO lesson = lessonService.createLesson(request);
        return created(lesson, "Create lesson successfully");
    }

    @Operation(
            summary = "Update a lesson",
            description = """
                    ### Purpose
                    Update an existing lesson's information.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Lesson ID must exist
                    - All fields are optional (only provided fields will be updated)
                    - Course ID must exist if provided
                    - Returns the updated lesson information
                    """
    )
    @PutMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<LessonDTO>> updateLesson(
            @PathVariable("lesson-id") Long lessonId,
            @RequestBody @Valid LessonRequest request
    ) {
        LessonDTO lesson = lessonService.updateLesson(lessonId, request);
        return ok(lesson, "Update lesson successfully");
    }

    @Operation(
            summary = "Delete a lesson",
            description = """
                    ### Purpose
                    Delete a lesson from the system.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Lesson ID must exist
                    - This operation cannot be undone
                    - Returns success message upon deletion
                    """
    )
    @DeleteMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable("lesson-id") Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ok(null, "Delete lesson successfully");
    }
}
