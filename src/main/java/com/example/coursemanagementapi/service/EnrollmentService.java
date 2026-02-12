package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.dto.EnrollmentDTO;
import com.example.coursemanagementapi.model.entity.Enrollments;
import com.example.coursemanagementapi.model.request.EnrollmentRequest;
import com.example.coursemanagementapi.model.response.PayloadResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EnrollmentService {
    PayloadResponse<EnrollmentDTO> getAllEnrollments(Integer page, Integer size, String sortBy, Sort.Direction direction);
    
    EnrollmentDTO getEnrollmentById(Long enrollmentId);
    
    List<EnrollmentDTO> getEnrollmentsByUserId(Long userId);
    
    List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId);
    
    EnrollmentDTO createEnrollment(EnrollmentRequest request);
    
    EnrollmentDTO updateEnrollment(Long enrollmentId, EnrollmentRequest request);
    
    void deleteEnrollment(Long enrollmentId);
    
    EnrollmentDTO incrementViewCount(Long enrollmentId);
    
    EnrollmentDTO incrementJoinCount(Long enrollmentId);
}
