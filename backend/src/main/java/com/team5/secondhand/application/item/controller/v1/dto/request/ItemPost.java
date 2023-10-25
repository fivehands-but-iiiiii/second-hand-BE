package com.team5.secondhand.application.item.controller.v1.dto.request;

import com.team5.secondhand.application.item.domain.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ItemPost {
    @NotNull
    private final String title;
    @Size(max = 2000000)
    private final String contents;
    @NotNull
    private final Long category;
    @NotNull
    private final Long region;
    @Size(min = 0)
    private final int price;
    @NotNull
    @Size(min = 1, max = 10)
    private final List<MultipartFile> images;

    public Item toEntity(List<ItemDetailImage> images) {
        return Item.builder()
                .title(title)
                .price(price)
                .category(category)
                .status(Status.ON_SALE)
                .count(ItemCounts.createRelated())
                .contents(ItemContents.of(contents, images))
                .isDeleted(false)
                .build();
    }
}
