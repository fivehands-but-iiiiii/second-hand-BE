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
import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.ItemDetailImage;
import com.team5.secondhand.api.image.domain.Directory;
import com.team5.secondhand.api.image.domain.Type;
import com.team5.secondhand.global.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.amazonaws.services.s3.internal.Constants.MB;

@Service
@RequiredArgsConstructor
public class ImageHostService implements ProfileUpload, ItemDetailImageUpload, ItemThumbnailImageUpload {

    private final AwsProperties properties;
    private final AmazonS3 amazonS3;

    private String generateKey(String originFileKey, String prefix) {
        return String.format("%s%s-%s", prefix, UUID.randomUUID(), originFileKey);
    }

    public String upload(MultipartFile file, Directory directory) throws IOException, TooLargeImageException, NotValidImageTypeException {

        if (file.getSize() > (30*MB)) {
            throw new TooLargeImageException("사진 용량이 30MB를 초과해 업로드에 실패하였습니다.");
        }

        if (Type.isValidType(file.getName())) {
            throw new NotValidImageTypeException("잘못된 확장자입니다.");
        }

        String newFileKey = generateKey(file.getOriginalFilename(), directory.getPrefix());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        amazonS3.putObject(new PutObjectRequest(properties.getBucket(), newFileKey, file.getInputStream(), objectMetadata));

        return amazonS3.getUrl(properties.getBucket(), newFileKey).toString();
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
    public String uploadItemThumbnailImage(Item item) throws ImageHostException {
        String url = item.getFirstDetailImage().getUrl();
        String key = getKey(url);
        String newKey = key.replace(Directory.ITEM_DETAIL.getPrefix(), Directory.ITEM_THUMBNAIL_ORIGIN.getPrefix());
        try {
            amazonS3.copyObject(properties.getBucket(), key, properties.getBucket(), newKey);
            return amazonS3.getUrl(properties.getBucket(), newKey).toString().replace("/origin", "");
        } catch (AmazonS3Exception e) {
            return url;
        }
    }

    private String getKey(String url) {
        if (url.contains(Directory.ITEM_DETAIL.getPrefix())) {
            return url.split("amazonaws.com/")[1];
        }
        return url;
    }

    @Override
    public List<ItemDetailImage> uploadItemDetailImages(List<MultipartFile> request) throws ImageHostException {
        if (request.size() < 1 || request.size() > 10) {
            throw new ImageHostException("이미지 첨부는 1개 이상 10개 이하로 해야합니다.");
        }

        List<ItemDetailImage> images = new ArrayList<>();

        for (MultipartFile multipartFile : request) {
            ImageInfo imageInfo = uploadItemDetailImage(multipartFile);
            images.add(ItemDetailImage.create(imageInfo.getImageUrl()));
        }

        return images;
    }
}
