package com.example.coursemanagementapi.service.impl;


import com.example.coursemanagementapi.exception.BadRequestException;
import com.example.coursemanagementapi.exception.UnauthorizedException;
import com.example.coursemanagementapi.jwt.JwtService;
import com.example.coursemanagementapi.model.dto.AppUserDTO;
import com.example.coursemanagementapi.model.entity.AppUser;
import com.example.coursemanagementapi.model.enums.Role;
import com.example.coursemanagementapi.model.enums.Status;
import com.example.coursemanagementapi.model.request.AppUserRequest;
import com.example.coursemanagementapi.model.request.AuthRequest;
import com.example.coursemanagementapi.model.response.AuthResponse;
import com.example.coursemanagementapi.repository.AppUserRepository;
import com.example.coursemanagementapi.service.AppUserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public AppUserDTO register(AppUserRequest request) {

        if (request == null) {
            throw new BadRequestException("Request Cant be null.");
        }

        if (request.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email can not be empty.");
        }
        if (request.getFullName().trim().isEmpty()) {
            throw new BadRequestException("Full name cant be empty.");
        }
        if (request.getFullName().trim().length() < 5) {
            throw new BadRequestException("Full name must be at least 5 characters long.");
        }
        if (request.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password can not be empty.");
        }

        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email " + request.getEmail() + " already exists.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userInfoJson = null;
        if (request.getUserInfo() != null && !request.getUserInfo().isEmpty()) {
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("userInfo", request.getUserInfo());
            userInfoJson = objectMapper.convertValue(userInfoMap, JsonNode.class);
        }

        // Validate role
        if (request.getRoles() == null) {
            throw new BadRequestException("Role cannot be null.");
        }

        AppUser newUser = new AppUser();
        newUser.setEmail(request.getEmail());
        newUser.setFullName(request.getFullName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setUserStatus(Status.ACTIVE);
        newUser.setRoles(List.of(request.getRoles()));
        newUser.setUserInfo(userInfoJson);

        AppUser savedUser = appUserRepository.save(newUser);
        log.info("User saved: {}", savedUser);
        return savedUser.toAppUserDTOWithoutTimestamps();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        AppUser appUser = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password. Please check your credentials and try again."));
        if (!passwordEncoder.matches(request.getPassword(), appUser.getPassword())) {
            throw new UnauthorizedException("Invalid email or password. Please check your credentials and try again.");
        }
        String token = jwtService.generateToken(appUser);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
