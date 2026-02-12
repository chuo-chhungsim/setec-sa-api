package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.ProgressDTO;
import com.example.coursemanagementapi.model.request.ProgressRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/progress")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Progress Management", description = "Course progress tracking operations")
public class ProgressController extends BaseController {
    private final ProgressService progressService;

    @Operation(
            summary = "Get user progress by user ID and course ID",
            description = """
                    ### Purpose
                    Retrieve progress tracking information for a specific user in a specific course.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - User ID and Course ID are required as query parameters
                    - Returns list of progress records showing lesson completion status
                    - Includes information about:
                        - Which lessons are completed
                        - Last watch position for each lesson
                        - Completion percentage
                    - Useful for displaying course progress dashboard
                    """
    )
    @PostMapping("user-progress")
    public ResponseEntity<ApiResponse<List<ProgressDTO>>> getProgressByUserIdAndCourseId(
            @RequestParam Long userId,
            @RequestParam Long courseId
    ) {
        List<ProgressDTO> progress = progressService.getProgressByUserIdAndCourseId(userId, courseId);
        return ok(progress, "Get progress by user id and course id successfully");
    }

    @Operation(
            summary = "Update progress",
            description = """
                    ### Purpose
                    Update the progress tracking information for a specific lesson.
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - Progress ID must exist
                    - Lesson ID, completion status, and watch position can be updated
                    - Percentage watched is calculated automatically
                    - Useful for tracking user's learning progress through lessons
                    - Returns updated progress information
                    """
    )
    @PostMapping("update-progress/{progress-id}")
    public ResponseEntity<ApiResponse<ProgressDTO>> updateProgress(
            @RequestBody ProgressRequest request, 
            @PathVariable("progress-id") Long progressId
    ) {
        ProgressDTO progress = progressService.updateProgress(request, progressId);
        return ok(progress, "Update progress successfully");
    }

}
