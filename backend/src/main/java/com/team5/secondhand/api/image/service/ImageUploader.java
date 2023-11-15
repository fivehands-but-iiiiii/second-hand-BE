package com.team5.secondhand.api.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team5.secondhand.api.image.domain.Directory;
import com.team5.secondhand.api.image.exception.NotValidImageTypeException;
import com.team5.secondhand.api.image.exception.TooLargeImageException;
import com.team5.secondhand.global.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploader {

    private final AwsProperties properties;
    private final AmazonS3 amazonS3;

    @Async("imageUploadExecutor")
    public CompletableFuture<String> upload(MultipartFile file, Directory directory) throws IOException {
        log.debug("Thread upload work start: {}, image: {}", Thread.currentThread().getThreadGroup().getName()+ " " + Thread.currentThread().getId(), file.getOriginalFilename());
        CompletableFuture<String> future = new CompletableFuture<>();

        String newFileKey = generateKey(file.getOriginalFilename(), directory.getPrefix());
        amazonS3.putObject(new PutObjectRequest(properties.getBucket(), newFileKey, file.getInputStream(), getMetadata(file)));
        log.debug("Thread work end: {}, image: {}", Thread.currentThread().getId(), file.getOriginalFilename());
        future.complete(amazonS3.getUrl(properties.getBucket(), newFileKey).toString());
        return future;
    }

    private String generateKey(String originFileKey, String prefix) {
        return String.format("%s%s-%s", prefix, UUID.randomUUID(), originFileKey);
    }

    private ObjectMetadata getMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }
}
