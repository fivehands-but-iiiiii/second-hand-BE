package com.team5.secondhand.application.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.application.item.controller.v1.dto.request.ItemPostWithUrl;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.global.model.UpdatedTimeEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE item SET is_deleted = true WHERE id = ?")
@Access(AccessType.FIELD)
public class Item extends UpdatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 1, max = 45)
    private String title;
    private int price;
    @NotNull
    private Long category;
    @Size(max = 300)
    private String thumbnailUrl;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_counts_id")
    private ItemCounts count;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_contents_id")
    private ItemContents contents;
    private Boolean isDeleted;

    @Builder
    public Item(Long id, String title, int price, Long category, String thumbnailUrl, Status status, Member seller, Region region, ItemCounts count, ItemContents contents, Boolean isDeleted) {
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
        this.isDeleted = isDeleted;
    }

    public Item update(Item itemPost) {
        return Item.builder()
                .id(this.getId())
                .title(itemPost.getTitle())
                .price(itemPost.getPrice())
                .category(itemPost.getCategory())
                .thumbnailUrl(itemPost.getThumbnailUrl())
                .status(this.status)
                .seller(this.seller)
                .region(this.region)
                .count(this.count)
                .contents(itemPost.getContents())
                .isDeleted(Boolean.FALSE)
                .build();
    }

    public Item updatePost(ItemPostWithUrl itemPost, String thumbanilUrl) {
        this.title = itemPost.getTitle();
        this.category = itemPost.getCategory();
        this.price = itemPost.getPrice();
        this.thumbnailUrl = thumbanilUrl;
        this.contents = contents.update(itemPost.getContents(), itemPost.getImages());

        return this;
    }

    @JsonIgnore
    public ItemImage getFirstDetailImage() {
        return contents.getFirstImage();
    }

    public Item sellerInfo(Member member, Region region) {
        this.seller = member;
        this.region = region;
        return this;
    }

    public Item updateThumbnail(String thumbanilUrl) {
        this.thumbnailUrl = thumbanilUrl;
        return this;
    }

    public boolean isSeller(long memberId) {
        return this.seller.equals(memberId);
    }

    public Item assignOwnership(Member seller, Region region) {
        this.seller = seller;
        this.region = region;
        return this;
    }

    public boolean isSameSellerAndBuyer(Member buyer) {
        return seller.equals(buyer);
    }
}
