package com.example.coursemanagementapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "Course Management",
        version = "v1",
        description = "V1 Beta"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@SpringBootApplication
public class CourseManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseManagementApiApplication.class, args);
        System.out.println("✅ Server connection test successful!");
    }

}
