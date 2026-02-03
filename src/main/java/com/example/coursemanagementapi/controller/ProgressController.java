package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.ProgressDTO;
import com.example.coursemanagementapi.model.request.ProgressRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.service.ProgressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/progress")
@SecurityRequirement(name = "bearerAuth")
public class ProgressController {
    private final ProgressService progressService;

    @PostMapping("user-progress")
    public ResponseEntity<ApiResponse<List<ProgressDTO>>> getProgressByUserIdAndCourseId(
            Long userId,
            Long courseId
    ) {

        List<ProgressDTO> progress =
                progressService.getProgressByUserIdAndCourseId(userId, courseId);

        return ResponseEntity.ok(ApiResponse.<List<ProgressDTO>>builder()
                .status(HttpStatus.OK)
                .message("Get progress by user id and course id successfully")
                .payload(progress)
                .build());
    }

    @PostMapping("update-progress/{progress-id}")
    public ResponseEntity<ApiResponse<ProgressDTO>> updateProgress(ProgressRequest request, @PathVariable("progress-id") Long progressId) {
        ProgressDTO progress = progressService.updateProgress(request, progressId);
        return ResponseEntity.ok(ApiResponse.<ProgressDTO>builder()
                .status(HttpStatus.OK)
                .message("Update progress successfully")
                .payload(progress)
                .build());

    }


}
