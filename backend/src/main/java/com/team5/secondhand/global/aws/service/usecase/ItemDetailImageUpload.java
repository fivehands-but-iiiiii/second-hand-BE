package com.team5.secondhand.global.aws.service.usecase;

import com.team5.secondhand.api.item.domain.ItemImage;
import com.team5.secondhand.global.aws.dto.response.ImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemDetailImageUpload {
    List<ItemImage> uploadItemDetailImages(List<MultipartFile> request) throws ImageHostException;

    ImageInfo uploadItemDetailImage(MultipartFile itemImages) throws ImageHostException;
}
