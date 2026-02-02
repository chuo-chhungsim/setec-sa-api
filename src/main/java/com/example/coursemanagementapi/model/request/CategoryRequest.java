package com.example.coursemanagementapi.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    public String getName() {
        return name == null ? null : name.trim();
    }
}
