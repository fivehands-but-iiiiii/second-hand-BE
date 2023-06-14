package com.team5.secondhand.global.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team5.secondhand.global.aws.domain.Directory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageHostService {

    @Value("${aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String generateKey(String originFileKey, String prefix) {
        return String.format("%s%s-%s", prefix, UUID.randomUUID(), originFileKey);
    }

    public String upload(MultipartFile file, Directory directory) throws IOException {
        String newFileKey = generateKey(file.getOriginalFilename(), directory.getPrefix());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucket, newFileKey, file.getInputStream(), objectMetadata));

        return amazonS3.getUrl(bucket, newFileKey).toString();
    }
}
