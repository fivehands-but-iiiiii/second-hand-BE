package com.team5.secondhand.global.aws.service.usecase;

import com.team5.secondhand.global.aws.exception.ImageHostException;

public interface ItemThumbnailImageUpload {
    String uploadItemThumbnailImage(String imageUrl) throws ImageHostException;
}
