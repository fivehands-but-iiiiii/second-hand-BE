package com.team5.secondhand.api.item.domain;

import com.team5.secondhand.api.item.dto.request.ItemPost;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.model.BaseTimeEntity;
import com.team5.secondhand.api.region.domain.Region;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 1, max = 45)
    private String title;
    private int price;
    @NotNull
    private Long category;
    @NotNull
    @Size(max = 300)
    private String thumbnailUrl;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_counts_id")
    private ItemCounts count;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public static Item create(ItemPost newItem, String thumbanilUrl, Member seller, Region region) {
        return Item.builder()
                .title(newItem.getTitle())
                .price(newItem.getPrice())
                .category(newItem.getCategory())
                .thumbnailUrl(thumbanilUrl)
                .status(Status.ON_SALE).seller(seller)
                .region(region).count(ItemCounts.createRelated())
                .contents(ItemContents.createdRelated(newItem.getContents(), newItem.getImages()))
                .build();

    }

    public Item updatePost(ItemPost itemPost, String thumbanilUrl) {
        this.title = itemPost.getTitle();
        this.category = itemPost.getCategory();
        this.price = itemPost.getPrice();
        this.thumbnailUrl = thumbanilUrl;
        this.contents = contents.update(itemPost.getContents(), itemPost.getImages());

        return this;
    }
}