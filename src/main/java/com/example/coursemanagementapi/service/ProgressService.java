package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.dto.ProgressDTO;
import com.example.coursemanagementapi.model.request.ProgressRequest;

import java.util.List;

public interface ProgressService {
    List<ProgressDTO> getProgressByUserIdAndCourseId(Long userId, Long courseId);

    ProgressDTO updateProgress(ProgressRequest request, Long progressId);
}
