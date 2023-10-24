package com.team5.secondhand.api.s3.service.usecase;

import com.team5.secondhand.api.s3.exception.ImageHostException;
import com.team5.secondhand.application.item.domain.ItemImage;
import com.team5.secondhand.api.s3.dto.response.ImageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemDetailImageUpload {
    List<ItemImage> uploadItemDetailImages(List<MultipartFile> request) throws ImageHostException;

    ImageInfo uploadItemDetailImage(MultipartFile itemImages) throws ImageHostException;
}
