package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.AppUserDTO;
import com.example.coursemanagementapi.model.request.AppUserRequest;
import com.example.coursemanagementapi.model.request.AuthRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.AuthResponse;
import com.example.coursemanagementapi.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(@RequestBody @Valid AuthRequest request) {
        AuthResponse login = appUserService.login(request);
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .message("Login successful")
                .payload(login)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AppUserDTO>> register(@RequestBody @Valid AppUserRequest request){
         AppUserDTO user = appUserService.register(request);
         ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder()
                .message("Register successful")
                .payload(user)
                .status(HttpStatus.CREATED)
                .build();
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}