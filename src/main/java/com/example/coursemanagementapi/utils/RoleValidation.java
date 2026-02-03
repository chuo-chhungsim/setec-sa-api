package com.example.coursemanagementapi.utils;

import com.example.coursemanagementapi.exception.UnauthorizedException;
import com.example.coursemanagementapi.exception.UserNotFoundException;
import com.example.coursemanagementapi.model.entity.AppUser;
import com.example.coursemanagementapi.model.enums.Role;
import com.example.coursemanagementapi.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleValidation {
    private final AppUserRepository appUserRepository;

    public boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }

    public boolean isTeacher() {
        return hasRole(Role.TEACHER);
    }

    private boolean hasRole(Role role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.name()));
    }


    public void validateAdmin() {
        String currentUserEmail =
                SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser currentUser = appUserRepository.findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User " + currentUserEmail + " not found"));

        if (!currentUser.getRoles().contains("ADMIN")) {
            throw new UnauthorizedException("Admin role required.");
        }
    }

    public void validateTeacher() {
        String currentUserEmail =
                SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User " + currentUserEmail + " not found"));
        if (!currentUser.getRoles().contains("TEACHER")) {
            throw new UnauthorizedException("Teacher role required.");
        }
    }

    public void validateStudent() {
        String currentUserEmail =
                SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User " + currentUserEmail + " not found"));
        if (!currentUser.getRoles().contains("STUDENT")) {
            throw new UnauthorizedException("Student role required.");
        }
    }
}
