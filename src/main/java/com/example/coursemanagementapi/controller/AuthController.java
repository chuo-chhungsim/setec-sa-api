package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.dto.AppUserDTO;
import com.example.coursemanagementapi.model.request.AppUserRequest;
import com.example.coursemanagementapi.model.request.AuthRequest;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.model.response.AuthResponse;
import com.example.coursemanagementapi.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and profile management")
public class AuthController extends BaseController {
    private final AppUserService appUserService;

    @Operation(
            summary = "User login",
            description = """
                    ### Purpose
                    Authenticate a user with email and password to receive a JWT token.
                    
                    ### Access
                    Public endpoint - no authentication required.
                    
                    ### Notes
                    - Returns a JWT token upon successful authentication
                    - Token must be included in Authorization header for protected endpoints
                    - Token format: Bearer {token}
                    - Invalid credentials will return 401 Unauthorized
                    """
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(@RequestBody @Valid AuthRequest request) {
        AuthResponse login = appUserService.login(request);
        return ok(login, "Login successful");
    }

    @Operation(
            summary = "User registration",
            description = """
                    ### Purpose
                    Register a new user account in the system.
                    
                    ### Access
                    Public endpoint - no authentication required.
                    
                    ### Notes
                    - Creates a new user account with the provided information
                    - Email must be unique
                    - Password will be encrypted before storage
                    - Role must be one of: ADMIN, TEACHER, STUDENT
                    - Returns the created user profile without sensitive information
                    """
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AppUserDTO>> register(@RequestBody @Valid AppUserRequest request){
         AppUserDTO user = appUserService.register(request);
         return created(user, "Register successful");
    }

    @Operation(
            summary = "Get current user profile",
            description = """
                    ### Purpose
                    Retrieve the profile information of the currently authenticated user.
                    
                    ### Access
                    Authenticated users - all roles can access their own profile.
                    
                    ### Notes
                    - Returns complete user profile information
                    - User is identified from the JWT token in the Authorization header
                    - Includes user details such as:
                        - User ID, full name, email
                        - User status and roles
                        - User info (phone, address, department, etc.)
                    - Does not include password or sensitive security information
                    - Returns 401 Unauthorized if token is missing or invalid
                    """
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<AppUserDTO>> getUserProfile() {
        AppUserDTO user = appUserService.getUserProfile();
        return ok(user, "Get user profile successfully");
    }
}