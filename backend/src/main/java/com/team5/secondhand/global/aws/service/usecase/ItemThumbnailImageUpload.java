package com.team5.secondhand.global.aws.service.usecase;

import com.team5.secondhand.global.aws.dto.response.ImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import org.springframework.web.multipart.MultipartFile;

public interface ItemThumbnailImageUpload {
    ImageInfo uploadItemThumbnailImage(String imageUrl) throws ImageHostException;
}
