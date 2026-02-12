package com.example.coursemanagementapi.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRequest {

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format. Please provide a valid email address.")
    @Schema(description = "User account email", example = "example@example.com")
    private String email;


    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "User account password", example = "123")
    private String password;

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }
    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
}