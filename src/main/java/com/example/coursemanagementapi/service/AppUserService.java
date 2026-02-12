package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.dto.AppUserDTO;
import com.example.coursemanagementapi.model.request.AppUserRequest;
import com.example.coursemanagementapi.model.request.AuthRequest;
import com.example.coursemanagementapi.model.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserService {
    UserDetails loadUserByUsername(String email);
    
    AuthResponse login(@Valid AuthRequest request);

    AppUserDTO register(@Valid AppUserRequest request);
    
    AppUserDTO getUserProfile();
}
