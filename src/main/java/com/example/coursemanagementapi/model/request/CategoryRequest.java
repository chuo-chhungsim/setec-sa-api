package com.example.coursemanagementapi.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryRequest {
    @NotBlank(message = "Name cannot be blank")
    private String categoryName;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName ==  null ? null : categoryName.trim();
    }
}
