package com.team5.secondhand.api.s3.service.usecase;

import com.team5.secondhand.api.s3.exception.ImageHostException;
import com.team5.secondhand.application.item.domain.Item;

public interface ItemThumbnailImageUpload {
    String uploadItemThumbnailImage(Item item) throws ImageHostException;
}
