package com.team5.secondhand.api.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

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
            checkFileSize(file);
            checkFileType(file);
            CompletableFuture<String> upload = imageUploader.upload(file, Directory.MEMBER_PROFILE_ORIGIN);
            imageUrl = upload.get().replace("/origin", "");
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new ImageHostException("회원 사진 업로드에 실패하였습니다.");
        }

        return ProfileImageInfo.createNewImageInfo(file.getName(), imageUrl);
    }

    @Override
    public ImageInfo uploadItemDetailImage(MultipartFile file) throws ImageHostException {
        String imageUrl = "";

        try {
            checkFileSize(file);
            checkFileType(file);
            CompletableFuture<String> upload = imageUploader.upload(file, Directory.ITEM_DETAIL);
            imageUrl = upload.get();
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new ImageHostException("물품 사진 업로드에 실패하였습니다.");
        }

        return ImageInfo.create(imageUrl);
    }

    @Override
    public CompletableFuture<String> uploadItemThumbnailImage(MultipartFile file) throws ImageHostException {
        CompletableFuture<String> upload;
        try {
            upload = imageUploader.upload(file, Directory.ITEM_THUMBNAIL_ORIGIN);
        } catch (AmazonS3Exception | IOException e) {
            throw new ImageHostException("물품 썸네일 업로드에 실패하였습니다.");
        }
        return upload;
    }

    @Override
    public List<ItemDetailImage> uploadItemDetailImages(List<MultipartFile> request) throws ImageHostException {
        checkFilesCount(request);

        List<ItemDetailImage> images = new ArrayList<>();
        List<CompletableFuture<String>> uploadFutures = new ArrayList<>();

        for (MultipartFile multipartFile : request) {
            CompletableFuture<String> upload = null;
            try {
                upload = imageUploader.upload(multipartFile, Directory.ITEM_DETAIL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            uploadFutures.add(upload);
        }

        uploadFutures.forEach(upload -> images.add(ItemDetailImage.create(
        upload.exceptionally(e -> {
                    log.error("이미지 업로드에 실패하였습니다.", e);
                    throw new CompletionException(e);
                })
                .join())));

        return images;
    }


    private void checkFilesCount(List<MultipartFile> files) throws ImageHostException {
        if (files.size() < properties.getMinFileCount() || files.size() > properties.getMaxFileCount()) {
            throw new ImageHostException(String.format("이미지 첨부는 %d개 이상 %d개 이하로 해야합니다.", properties.getMinFileCount(), properties.getMaxFileCount()));
        }
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

}
