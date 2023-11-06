package com.team5.secondhand.api.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team5.secondhand.api.image.dto.response.ImageInfo;
import com.team5.secondhand.api.image.dto.response.ProfileImageInfo;
import com.team5.secondhand.api.image.exception.ImageHostException;
import com.team5.secondhand.api.image.exception.NotValidImageTypeException;
import com.team5.secondhand.api.image.exception.TooLargeImageException;
import com.team5.secondhand.api.image.service.usecase.ItemDetailImageUpload;
import com.team5.secondhand.api.image.service.usecase.ItemThumbnailImageUpload;
import com.team5.secondhand.api.image.service.usecase.ProfileUpload;
import com.team5.secondhand.application.item.domain.ItemDetailImage;
import com.team5.secondhand.api.image.domain.Directory;
import com.team5.secondhand.api.image.domain.Type;
import com.team5.secondhand.global.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.amazonaws.services.s3.internal.Constants.MB;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageHostService implements ProfileUpload, ItemDetailImageUpload, ItemThumbnailImageUpload {

    private final AwsProperties properties;
    private final ImageUploader imageUploader;

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
    public String uploadItemThumbnailImage(MultipartFile file) throws ImageHostException {
        String url = "";
        try {
            url = upload(file, Directory.ITEM_THUMBNAIL_ORIGIN);
        } catch (AmazonS3Exception | IOException e) {
            throw new ImageHostException("물품 썸네일 업로드에 실패하였습니다.");
        }
        return url;
    }

    @Override
    public List<ItemDetailImage> uploadItemDetailImages(List<MultipartFile> request) throws ImageHostException {
        checkFilesSize(request);

        List<ItemDetailImage> images = new ArrayList<>();

        for (MultipartFile multipartFile : request) {
            ImageInfo imageInfo = uploadItemDetailImage(multipartFile);
            images.add(ItemDetailImage.create(imageInfo.getImageUrl()));
        }

        return images;
    }

    private void checkFilesSize(List<MultipartFile> files) throws ImageHostException {
        if (files.size() < properties.getMinFileCount() || files.size() > properties.getMaxFileCount()) {
            throw new ImageHostException(String.format("이미지 첨부는 %d개 이상 %d개 이하로 해야합니다.", properties.getMinFileCount(), properties.getMaxFileCount()));
        }
    }

    private ObjectMetadata getMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }

    private void checkFileType(MultipartFile file) throws NotValidImageTypeException {
        if (Type.isValidType(file.getName())) {
            throw new NotValidImageTypeException("잘못된 확장자입니다.");
        }
    }

    private void checkFileSize(MultipartFile file) throws TooLargeImageException {
        if (file.getSize() > (properties.getMaxSize()*MB)) {
            throw new TooLargeImageException(String.format("사진 용량이 %s MB를 초과해 업로드에 실패하였습니다.", properties.getMaxSize()));
        }
    }

    private String generateKey(String originFileKey, String prefix) {
        return String.format("%s%s-%s", prefix, UUID.randomUUID(), originFileKey);
    }

    private String getKey(String url) {
        if (url.contains(Directory.ITEM_DETAIL.getPrefix())) {
            return url.split("amazonaws.com/")[1];
        }
        return url;
    }
}
