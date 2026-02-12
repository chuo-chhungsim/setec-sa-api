package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> ok(T payload, String message) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .status(HttpStatus.OK)
                        .message(message)
                        .payload(payload)
                        .build()
        );
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(T payload, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<T>builder()
                                .status(HttpStatus.CREATED)
                                .message(message)
                                .payload(payload)
                                .build()
                );
    }
}
