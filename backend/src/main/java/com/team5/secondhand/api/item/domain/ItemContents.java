package com.team5.secondhand.api.item.domain;

import com.team5.secondhand.api.item.controller.v1.dto.request.RequestedItemImages;
import com.team5.secondhand.api.item.util.ImageUrlConverter;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE item_contents SET is_deleted = true WHERE id = ?")
public class ItemContents {

    @Id
    @Min(1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 1)
    private String contents;
    @Size(min = 1, max = 10)
    @Convert(converter = ImageUrlConverter.class)
    private List<ItemImage> detailImageUrl = new ArrayList<>();
    private Boolean isDeleted = false;

    @Builder
    protected ItemContents(Long id, String contents, List<ItemImage> detailImageUrl, Boolean isDeleted) {
        this.id = id;
        this.contents = contents;
        this.detailImageUrl = detailImageUrl;
        this.isDeleted = isDeleted;
    }

    public static ItemContents of(String contents, List<ItemImage> images) {
        if (images==null) {
            images = Collections.EMPTY_LIST;
        }

        return ItemContents.builder()
                .contents(contents)
                .detailImageUrl(images)
                .isDeleted(false)
                .build();
    }

    public ItemContents update(String contents, List<RequestedItemImages> images) {
        if (images==null) {
            images = Collections.EMPTY_LIST;
        }

        return ItemContents.builder()
                .id(this.id)
                .contents(contents)
                .detailImageUrl(images.stream().map(RequestedItemImages::toEntity).collect(Collectors.toList()))
                .isDeleted(this.isDeleted)
                .build();
    }

    public ItemImage getFirstImage() {
        return detailImageUrl.isEmpty() ? ItemImage.from("") : detailImageUrl.get(0);
    }
}
