package com.team5.secondhand.api.item.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.dto.RegionSummary;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder
@RequiredArgsConstructor
public class ItemSummary implements Serializable {
    private final Long id;
    private final String title;
    private final String thumbnailUrl;
    private final RegionSummary region;
    private final Instant createdAt;
    private final Integer price;
    private final Status status;
    private final Long hits;
    private final Long chatCount;
    private final Long likeCount;
    private final Boolean isLike;

    @JsonIgnore
    public static ItemSummary of(Region region, Item item, Boolean isLike) {
        return ItemSummary.builder()
                .id(item.getId())
                .title(item.getTitle())
                .thumbnailUrl(item.getThumbnailUrl())
                .region(RegionSummary.fromRegion(region))
                .createdAt(item.getCreatedAt())
                .price(item.getPrice())
                .status(item.getStatus())
                .hits(item.getCount().getHits())
                .chatCount(item.getCount().getChatCounts())
                .likeCount(item.getCount().getLikeCounts())
                .isLike(isLike)
                .build();
    }
}
