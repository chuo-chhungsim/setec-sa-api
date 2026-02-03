package com.example.coursemanagementapi.service.impl;

import com.example.coursemanagementapi.exception.ItemNotFoundException;
import com.example.coursemanagementapi.exception.UserNotFoundException;
import com.example.coursemanagementapi.model.dto.ProgressDTO;
import com.example.coursemanagementapi.model.entity.AppUser;
import com.example.coursemanagementapi.model.entity.Lesson;
import com.example.coursemanagementapi.model.entity.Progress;
import com.example.coursemanagementapi.model.request.ProgressRequest;
import com.example.coursemanagementapi.repository.AppUserRepository;
import com.example.coursemanagementapi.repository.LessonRepository;
import com.example.coursemanagementapi.repository.ProgressRepository;
import com.example.coursemanagementapi.service.ProgressService;
import com.example.coursemanagementapi.utils.RoleValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;
    private final AppUserRepository appUserRepository;
    private final RoleValidation roleValidation;
    private final LessonRepository lessonRepository;

    @Override
    public List<ProgressDTO> getProgressByUserIdAndCourseId(Long userId, Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found"));

        if (!roleValidation.isAdmin()) {
            userId = currentUser.getUserId();
        }

        return progressRepository.findByUserUserIdAndLessonCourseCourseId(userId, courseId).stream().map(Progress::toProgressDTO).toList();
    }

    @Override
    public ProgressDTO updateProgress(ProgressRequest request, Long progressId) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(()-> new ItemNotFoundException("Lesson not found"));

        Progress progress = Progress
                .builder()

                .build();

        return null;
    }
}
