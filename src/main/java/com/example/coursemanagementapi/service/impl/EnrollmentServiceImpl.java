package com.example.coursemanagementapi.service.impl;

import com.example.coursemanagementapi.exception.BadRequestException;
import com.example.coursemanagementapi.exception.ItemNotFoundException;
import com.example.coursemanagementapi.model.dto.EnrollmentDTO;
import com.example.coursemanagementapi.model.entity.AppUser;
import com.example.coursemanagementapi.model.entity.Course;
import com.example.coursemanagementapi.model.entity.Enrollments;
import com.example.coursemanagementapi.model.enums.EnrollmentStatus;
import com.example.coursemanagementapi.model.request.EnrollmentRequest;
import com.example.coursemanagementapi.model.response.PaginationResponse;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import com.example.coursemanagementapi.repository.AppUserRepository;
import com.example.coursemanagementapi.repository.CourseRepository;
import com.example.coursemanagementapi.repository.EnrollmentRepository;
import com.example.coursemanagementapi.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final AppUserRepository appUserRepository;
    private final CourseRepository courseRepository;

    @Override
    public PayloadResponse<EnrollmentDTO> getAllEnrollments(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        Page<Enrollments> enrollmentPage = enrollmentRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(direction, sortBy))
        );
        
        List<EnrollmentDTO> enrollmentDTOs = enrollmentPage.getContent().stream()
                .map(this::toEnrollmentDTO)
                .collect(Collectors.toList());
        
        return PayloadResponse.<EnrollmentDTO>builder()
                .items(enrollmentDTOs)
                .pagination(PaginationResponse.fromPage(enrollmentPage, page, size))
                .build();
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long enrollmentId) {
        Enrollments enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ItemNotFoundException("Enrollment not found with id: " + enrollmentId));
        return toEnrollmentDTO(enrollment);
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByUserId(Long userId) {
        List<Enrollments> enrollments = enrollmentRepository.findByAppUser_UserId(userId);
        return enrollments.stream()
                .map(this::toEnrollmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId) {
        List<Enrollments> enrollments = enrollmentRepository.findByCourse_CourseId(courseId);
        return enrollments.stream()
                .map(this::toEnrollmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentDTO createEnrollment(EnrollmentRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }

        // Check if enrollment already exists
        if (enrollmentRepository.existsByAppUser_UserIdAndCourse_CourseId(
                request.getUserId(), request.getCourseId())) {
            throw new BadRequestException("User is already enrolled in this course");
        }

        // Validate user exists
        AppUser appUser = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + request.getUserId()));

        // Validate course exists
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ItemNotFoundException("Course not found with id: " + request.getCourseId()));

        Enrollments enrollment = Enrollments.builder()
                .appUser(appUser)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .viewCount(0L)
                .joinCount(0L)
                .enrollmentStatus(request.getEnrollmentStatus() != null 
                        ? request.getEnrollmentStatus() 
                        : EnrollmentStatus.LISTED)
                .notes(request.getNotes())
                .build();

        enrollment = enrollmentRepository.save(enrollment);
        log.info("Enrollment created: {}", enrollment.getEnrollmentId());
        
        return toEnrollmentDTO(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentDTO updateEnrollment(Long enrollmentId, EnrollmentRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }

        Enrollments enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ItemNotFoundException("Enrollment not found with id: " + enrollmentId));

        // Update enrollment status if provided
        if (request.getEnrollmentStatus() != null) {
            enrollment.setEnrollmentStatus(request.getEnrollmentStatus());
        }

        // Update notes if provided
        if (request.getNotes() != null) {
            enrollment.setNotes(request.getNotes());
        }

        // Update user if provided
        if (request.getUserId() != null && !request.getUserId().equals(enrollment.getAppUser().getUserId())) {
            AppUser appUser = appUserRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + request.getUserId()));
            enrollment.setAppUser(appUser);
        }

        // Update course if provided
        if (request.getCourseId() != null && !request.getCourseId().equals(enrollment.getCourse().getCourseId())) {
            // Check if new enrollment already exists
            if (enrollmentRepository.existsByAppUser_UserIdAndCourse_CourseId(
                    enrollment.getAppUser().getUserId(), request.getCourseId())) {
                throw new BadRequestException("User is already enrolled in this course");
            }
            
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ItemNotFoundException("Course not found with id: " + request.getCourseId()));
            enrollment.setCourse(course);
        }

        enrollment = enrollmentRepository.save(enrollment);
        log.info("Enrollment updated: {}", enrollment.getEnrollmentId());
        
        return toEnrollmentDTO(enrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(Long enrollmentId) {
        Enrollments enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ItemNotFoundException("Enrollment not found with id: " + enrollmentId));
        
        enrollmentRepository.delete(enrollment);
        log.info("Enrollment deleted: {}", enrollmentId);
    }

    @Override
    @Transactional
    public EnrollmentDTO incrementViewCount(Long enrollmentId) {
        Enrollments enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ItemNotFoundException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setViewCount(enrollment.getViewCount() != null ? enrollment.getViewCount() + 1 : 1L);
        enrollment = enrollmentRepository.save(enrollment);
        
        return toEnrollmentDTO(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentDTO incrementJoinCount(Long enrollmentId) {
        Enrollments enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ItemNotFoundException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setJoinCount(enrollment.getJoinCount() != null ? enrollment.getJoinCount() + 1 : 1L);
        enrollment = enrollmentRepository.save(enrollment);
        
        return toEnrollmentDTO(enrollment);
    }

    private EnrollmentDTO toEnrollmentDTO(Enrollments enrollment) {
        return EnrollmentDTO.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .userId(enrollment.getAppUser() != null ? enrollment.getAppUser().getUserId() : null)
                .courseId(enrollment.getCourse() != null ? enrollment.getCourse().getCourseId() : null)
                .userName(enrollment.getAppUser() != null ? enrollment.getAppUser().getFullName() : null)
                .courseName(enrollment.getCourse() != null ? enrollment.getCourse().getCourseName() : null)
                .enrollmentDate(enrollment.getEnrollmentDate())
                .viewCount(enrollment.getViewCount())
                .joinCount(enrollment.getJoinCount())
                .enrollmentStatus(enrollment.getEnrollmentStatus())
                .notes(enrollment.getNotes())
                .build();
    }
}
