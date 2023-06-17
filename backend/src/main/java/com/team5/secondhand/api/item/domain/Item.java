package com.team5.secondhand.api.item.domain;

import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.model.BaseTimeEntity;
import com.team5.secondhand.api.region.domain.Region;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(min = 1, max = 45)
    private String title;
    private Integer price;
    @NotNull
    private Long category;
    @NotNull
    @Size(max = 300)
    private String thumbnailUrl;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToOne
    @JoinColumn(name = "item_counts_id")
    private ItemCounts count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_contents_id")
    private ItemContents contents;

    @Builder
    private Item(Long id, String title, Integer price, Long category, String thumbnailUrl, Status status, Member seller, Region region, ItemCounts count, ItemContents contents) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.status = status;
        this.seller = seller;
        this.region = region;
        this.count = count;
        this.contents = contents;
    }

    public static Item create(String title, Integer price, Long category, String thumbnailUrl, Member seller, Region region, String contents, List<ItemDetailImage> images) {
        return Item.builder()
                .title(title)
                .price(price)
                .category(category)
                .thumbnailUrl(thumbnailUrl)
                .status(Status.ON_SALE)
                .seller(seller)
                .region(region)
                .count(ItemCounts.createRelated())
                .contents(ItemContents.createdRelated(contents, images))
                .build();
    }
}
