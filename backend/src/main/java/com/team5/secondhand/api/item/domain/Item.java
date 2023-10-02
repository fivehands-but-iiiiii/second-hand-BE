package com.team5.secondhand.api.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.global.model.UpdatedTimeEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE item SET is_deleted = true WHERE id = ?")
public class Item extends UpdatedTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int price;
    private Long category;
    private String thumbnailUrl;
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
    private Boolean isDeleted = Boolean.FALSE;

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
                .isDeleted(this.isDeleted)
                .build();
    }

    @JsonIgnore
    public ItemImage getFirstDetailImage() {
        return contents.getFirstImage();
    }

    public Item updateThumbnail(String thumbnail) {
        this.thumbnailUrl = thumbnail;
        return this;
    }

    public Item assignOwnership(Member seller, Region region) {
        this.seller = seller;
        this.region = region;
        return this;
    }

    public boolean isSeller(Member member) {
        return this.seller.equals(member);
    }

    public boolean isSeller(long memberId) {
        return this.seller.equals(memberId);
    }
}
