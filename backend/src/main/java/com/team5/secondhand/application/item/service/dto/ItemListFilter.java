package com.team5.secondhand.application.item.service.dto;

import com.team5.secondhand.application.item.domain.Status;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemListFilter {

    private Long categoryId;
    private Long sellerId;
    private List<Status> sales;
    private Long regionId;

    public static ItemListFilter of (Long categoryId, Long sellerId, Boolean isOnSales, Long regionId) {
        return ItemListFilter.builder()
                .categoryId(categoryId)
                .sellerId(sellerId)
                .sales(Status.isOnSales(isOnSales))
                .regionId(regionId)
                .build();
    }
}
