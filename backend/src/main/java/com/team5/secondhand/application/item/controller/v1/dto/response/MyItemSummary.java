package com.team5.secondhand.application.item.controller.v1.dto.response;

import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import com.team5.secondhand.application.region.dto.RegionSummary;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder
@RequiredArgsConstructor
public class MyItemSummary implements Serializable {
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

    public static MyItemSummary of(Item item) {
        return MyItemSummary.builder()
                .id(item.getId())
                .title(item.getTitle())
                .thumbnailUrl(item.getThumbnailUrl())
                .region(RegionSummary.fromRegion(item.getRegion()))
                .createdAt(item.getCreatedAt())
                .price(item.getPrice())
                .status(item.getStatus())
                .hits(item.getCount().getHits())
                .chatCount(item.getCount().getChatCounts())
                .likeCount(item.getCount().getLikeCounts())
                .build();
    }
}
