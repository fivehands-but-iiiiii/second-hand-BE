package com.team5.secondhand.api.item.domain;

import com.team5.secondhand.api.item.controller.v1.dto.request.RequestedItemImages;
import com.team5.secondhand.api.item.util.ImageUrlConverter;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE item_contents SET is_deleted = true WHERE id = ?")
public class ItemContents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contents;
    @Convert(converter = ImageUrlConverter.class)
    private List<ItemImage> detailImageUrl = new ArrayList<>();
    private Boolean isDeleted;

    @Builder
    protected ItemContents(Long id, String contents, List<ItemImage> detailImageUrl, Boolean isDeleted) {
        this.id = id;
        this.contents = contents;
        this.detailImageUrl = detailImageUrl;
        this.isDeleted = isDeleted;
    }

    public static ItemContents of(String contents, List<ItemImage> images) {
        return ItemContents.builder()
                .contents(contents)
                .detailImageUrl(images)
                .isDeleted(false)
                .build();
    }

    public ItemContents update(String contents, List<RequestedItemImages> itemImages) {
        this.contents = contents;
        this.detailImageUrl = itemImages.stream().map(RequestedItemImages::toEntity).collect(Collectors.toList());
        return this;
    }

    public ItemImage getFirstDetailImage() {
        return detailImageUrl.isEmpty() ? null : detailImageUrl.get(0);
    }
}
