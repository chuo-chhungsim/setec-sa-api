package com.example.coursemanagementapi.service;

import com.example.coursemanagementapi.model.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
    FileMetadata uploadFile(MultipartFile file);

    InputStream getFileByFileName(String fileName);
}
