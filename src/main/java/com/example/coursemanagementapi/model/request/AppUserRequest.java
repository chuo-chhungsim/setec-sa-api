package com.example.coursemanagementapi.model.request;

import com.example.coursemanagementapi.model.enums.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppUserRequest {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role roles;

    private Map<String, Object> userInfo;

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }
    
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
    
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
    
    // Custom setter to handle case-insensitive enum deserialization
    public void setRoles(String roleString) {
        if (roleString == null || roleString.trim().isEmpty()) {
            this.roles = null;
            return;
        }
        try {
            // Try to match case-insensitively
            String upperRole = roleString.trim().toUpperCase();
            this.roles = Role.valueOf(upperRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleString + ". Must be one of: ADMIN, TEACHER, STUDENT");
        }
    }
    
    // Also allow setting directly as Role enum
    public void setRoles(Role role) {
        this.roles = role;
    }
}
