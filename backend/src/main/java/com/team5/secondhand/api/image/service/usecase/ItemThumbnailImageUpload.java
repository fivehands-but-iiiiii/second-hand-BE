package com.team5.secondhand.api.image.service.usecase;

import com.team5.secondhand.api.image.exception.ImageHostException;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ItemThumbnailImageUpload {
    CompletableFuture<String> uploadItemThumbnailImage(MultipartFile url) throws ImageHostException;
}
