package com.team5.secondhand.global.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team5.secondhand.global.aws.domain.Directory;
import com.team5.secondhand.global.aws.domain.Type;
import com.team5.secondhand.global.aws.dto.response.ImageInfo;
import com.team5.secondhand.global.aws.dto.response.ProfileImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import com.team5.secondhand.global.aws.exception.NotValidImageTypeException;
import com.team5.secondhand.global.aws.exception.TooLargeImageException;
import com.team5.secondhand.global.aws.service.usecase.ItemDetailImageUpload;
import com.team5.secondhand.global.aws.service.usecase.ItemThumbnailImageUpload;
import com.team5.secondhand.global.aws.service.usecase.ProfileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageHostService implements ProfileUpload, ItemDetailImageUpload, ItemThumbnailImageUpload {

    @Value("${aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String generateKey(String originFileKey, String prefix) {
        return String.format("%s%s-%s", prefix, UUID.randomUUID(), originFileKey);
    }

    public String upload(MultipartFile file, Directory directory) throws IOException, TooLargeImageException, NotValidImageTypeException {

        if (file.getSize() > (1e+8*2)) {
            throw new TooLargeImageException("사진 용량이 10MB를 초과해 업로드에 실패하였습니다.");
        }

        if (Type.isValidType(file.getName())) {
            throw new NotValidImageTypeException("잘못된 확장자입니다.");
        }

        String newFileKey = generateKey(file.getOriginalFilename(), directory.getPrefix());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        amazonS3.putObject(new PutObjectRequest(bucket, newFileKey, file.getInputStream(), objectMetadata));

        return amazonS3.getUrl(bucket, newFileKey).toString();
    }

    @Override
    public ProfileImageInfo uploadMemberProfileImage(MultipartFile file) throws ImageHostException {
        String imageUrl = "";

        try {
            String upload = upload(file, Directory.MEMBER_PROFILE_ORIGIN);
            imageUrl = upload.replace("/origin", "");
        } catch (IOException e) {
            throw new ImageHostException("회원 사진 업로드에 실패하였습니다.");
        }

        return ProfileImageInfo.createNewImageInfo(file.getName(), imageUrl);
    }

    @Override
    public ImageInfo uploadItemDetailImage(MultipartFile file) throws ImageHostException {
        String imageUrl = "";

        try {
            imageUrl = upload(file, Directory.ITEM_DETAIL);
        } catch (IOException e) {
            throw new ImageHostException("물품 사진 업로드에 실패하였습니다.");
        }

        return ImageInfo.create(imageUrl);
    }

    @Override
    public ImageInfo uploadItemThumbnailImage(String key) throws ImageHostException {
        String newKey = key.replace(Directory.ITEM_DETAIL.getPrefix(), Directory.ITEM_THUMBNAIL_ORIGIN.getPrefix());

        amazonS3.copyObject(bucket, key, bucket, newKey);
        URL url = amazonS3.getUrl(bucket, newKey);

        return ImageInfo.create(url.toString().replace("/origin", ""));
    }
}
