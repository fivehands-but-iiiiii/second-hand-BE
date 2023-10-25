package com.team5.secondhand.application.item.domain;

import com.team5.secondhand.application.item.controller.v1.dto.request.ItemImage;
import com.team5.secondhand.application.item.util.ImageUrlConverter;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @ToString @Validated
@Table(name = "item_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE item_contents SET is_deleted = true WHERE id = ?")
public class ItemContents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contents;

    @Size(min = 1, max = 10)
    @Convert(converter = ImageUrlConverter.class)
    private List<ItemDetailImage> detailImageUrl = new ArrayList<>();

    private Boolean isDeleted;

    @Builder
    public ItemContents(Long id, String contents, List<ItemDetailImage> detailImageUrl, Boolean isDeleted) {
        this.id = id;
        this.contents = contents;
        this.detailImageUrl = detailImageUrl;
        this.isDeleted = isDeleted;
    }

    public static ItemContents createdRelated(String contents, List<ItemDetailImage> images) {
        return ItemContents.builder()
                .contents(contents)
                .detailImageUrl(images)
                .isDeleted(false)
                .build();
    }

    public ItemContents update(String contents, List<ItemImage> itemImages) {
        this.contents = contents;
        this.detailImageUrl = itemImages.stream().map(ItemImage::toEntity).collect(Collectors.toList());

        return this;
    }

    public ItemDetailImage getFirstDetailImage() {
        return detailImageUrl.get(0);
    }
}
