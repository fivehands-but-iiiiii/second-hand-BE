package com.team5.secondhand.api.image.service.usecase;

import com.team5.secondhand.api.image.exception.ImageHostException;
import com.team5.secondhand.application.item.domain.Item;

public interface ItemThumbnailImageUpload {
    String uploadItemThumbnailImage(Item item) throws ImageHostException;
}
