package com.team5.secondhand.api.image.service.usecase;

import com.team5.secondhand.api.image.dto.response.ImageInfo;
import com.team5.secondhand.api.image.exception.ImageHostException;
import com.team5.secondhand.application.item.domain.ItemDetailImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemDetailImageUpload {
    List<ItemDetailImage> uploadItemDetailImages(List<MultipartFile> request) throws ImageHostException;

    ImageInfo uploadItemDetailImage(MultipartFile itemImages) throws ImageHostException;
}
