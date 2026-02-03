package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.LessonDTO;
import com.example.coursemanagementapi.model.entity.Lesson;
import com.example.coursemanagementapi.model.request.LessonRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.service.LessonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lessons")
@SecurityRequirement(name = "bearerAuth")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<LessonDTO>>> getAllLessons(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        PayloadResponse<LessonDTO> payloadResponse = lessonService.getAllLessons(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.<PayloadResponse<LessonDTO>>builder()
                .message("Get all lessons successfully")
                .status(HttpStatus.OK)
                .payload(payloadResponse)
                .build());
    }

    @GetMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<LessonDTO>> getLessonById(@PathVariable("lesson-id") Long lessonId) {
        LessonDTO lesson = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(ApiResponse.<LessonDTO>builder()
                .message("Get lesson successfully")
                .status(HttpStatus.OK)
                .payload(lesson)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LessonDTO>> createLesson(@RequestBody @Valid LessonRequest request) {
        LessonDTO lesson = lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<LessonDTO>builder()
                        .message("Create lesson successfully")
                        .status(HttpStatus.CREATED)
                        .payload(lesson)
                        .build());
    }

    @PutMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<LessonDTO>> updateLesson(
            @PathVariable("lesson-id") Long lessonId,
            @RequestBody @Valid LessonRequest request
    ) {
        LessonDTO lesson = lessonService.updateLesson(lessonId, request);
        return ResponseEntity.ok(ApiResponse.<LessonDTO>builder()
                .message("Update lesson successfully")
                .status(HttpStatus.OK)
                .payload(lesson)
                .build());
    }

    @DeleteMapping("/{lesson-id}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable("lesson-id") Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Delete lesson successfully")
                .status(HttpStatus.OK)
                .payload(null)
                .build());
    }
}
