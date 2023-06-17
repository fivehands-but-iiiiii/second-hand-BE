package com.team5.secondhand.api.item.domain;

import com.team5.secondhand.api.item.util.ImageUrlConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Table(name = "item_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemContents {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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

    public static ItemContents createdRelated(String contents, List<ItemDetailImage> images) {
        return ItemContents.builder()
                .contents(contents)
                .detailImageUrl(images)
                .build();
    }
}
