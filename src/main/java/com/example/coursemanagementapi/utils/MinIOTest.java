package com.example.coursemanagementapi.utils;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinIOTest {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @PostConstruct
    public void testMinIO() {
        try {
            log.info("Starting MinIO connection test...");

            // Test 1: Check if bucket exists
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (bucketExists) {
                System.out.println("✅ MinIO bucket working!");
            }

        } catch (io.minio.errors.ErrorResponseException e) {
            log.error("❌ MinIO error response: {}", e.getMessage());
            log.error("Error code: {}", e.errorResponse().code());
            e.printStackTrace();
        } catch (io.minio.errors.InsufficientDataException e) {
            log.error("❌ MinIO insufficient data error: {}", e.getMessage());
            e.printStackTrace();
        } catch (io.minio.errors.InternalException e) {
            log.error("❌ MinIO internal error: {}", e.getMessage());
            e.printStackTrace();
        } catch (io.minio.errors.InvalidResponseException e) {
            log.error("❌ MinIO invalid response: {}", e.getMessage());
            e.printStackTrace();
        } catch (io.minio.errors.ServerException e) {
            log.error("❌ MinIO server error: {}", e.getMessage());
            e.printStackTrace();
        } catch (io.minio.errors.XmlParserException e) {
            log.error("❌ MinIO XML parser error: {}", e.getMessage());
            e.printStackTrace();
        } catch (java.io.IOException e) {
            log.error("❌ MinIO IO error: {}", e.getMessage());
            e.printStackTrace();
        } catch (java.security.NoSuchAlgorithmException e) {
            log.error("❌ MinIO algorithm error: {}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.error("❌ MinIO test failed: {}", e.getMessage());
            log.error("Exception type: {}", e.getClass().getName());
            e.printStackTrace();
        }
    }
}








