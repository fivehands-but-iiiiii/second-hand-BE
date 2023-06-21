package com.team5.secondhand.api.item.domain;

import com.team5.secondhand.api.item.dto.request.ItemImage;
import com.team5.secondhand.api.item.util.ImageUrlConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@ToString
@Table(name = "item_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemContents {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @Size(min = 1, max = 10)
    @Convert(converter = ImageUrlConverter.class)
    private List<ItemDetailImage> detailImageUrl = new ArrayList<>();

    @Builder
    private ItemContents(Long id, String contents, List<ItemDetailImage> detailImageUrl) {
        this.id = id;
        this.contents = contents;
        this.detailImageUrl = detailImageUrl;
    }

    public static ItemContents createdRelated(String contents, List<ItemImage> itemImages) {
        List<ItemDetailImage> images = itemImages.stream().map(ItemDetailImage::of).collect(Collectors.toList());
        return ItemContents.builder()
                .contents(contents)
                .detailImageUrl(images)
                .build();
    }
}
