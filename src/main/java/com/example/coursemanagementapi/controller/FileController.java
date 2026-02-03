package com.example.coursemanagementapi.controller;

import com.example.coursemanagementapi.model.entity.FileMetadata;
import com.example.coursemanagementapi.model.response.ApiResponse;
import com.example.coursemanagementapi.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "File management for Profile user")
public class FileController {
    private final FileService fileService;

    @Operation(
            summary = "Upload a file",
            description = """
                    ### Purpose
                    Upload an image file to the server for storage and retrieval (primarily for profile images).
                    
                    ### Access
                    Authenticated users - all roles can access.
                    
                    ### Notes
                    - The file is stored securely and metadata (name, size, type, URL) is returned
                    - Validation rules:
                        - File cannot be null or empty
                        - Maximum file size: 10 MB
                        - Allowed file types: image/jpeg, image/jpg, image/png
                    - File is automatically renamed with UUID for security
                    - Returns file URL for direct access
                    """
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetadata>> uploadFile(@RequestParam MultipartFile file) {
        FileMetadata fileMetadata = fileService.uploadFile(file);
        ApiResponse<FileMetadata> apiResponse = ApiResponse.<FileMetadata>builder()
                .message("Upload file successfully!")
                .status(HttpStatus.CREATED)
                .payload(fileMetadata)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(
            summary = "Preview a file by name (Public Access)",
            description = """
                    ### Purpose
                    Retrieve a file by its stored name and return it as a binary stream.
                    
                    ### Access
                    Public endpoint - no authentication required.
                    
                    ### Notes
                    - This endpoint is publicly accessible for direct URL sharing (e.g., profile images)
                    - File is returned as a binary stream with appropriate content type
                    - Supports direct URL embedding in HTML or other applications
                    """
    )
    @GetMapping("/preview-file/{file-name}")
    public ResponseEntity<?> getFileByFileName(@PathVariable("file-name") String fileName) throws IOException {
        InputStream inputStream = fileService.getFileByFileName(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(inputStream.readAllBytes());
    }
}
