package com.team5.secondhand.api.item.controller.v2.dto;

import com.team5.secondhand.api.item.controller.dto.ItemSummary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class FilteredItems {

    @Getter
    @RequiredArgsConstructor
    public static class Request {
        private final Long last;
        private final Long sellerId;
        private final Long regionId;
        private final Boolean isSales;
        private final Long categoryId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private final Long last;
        private final boolean hasPrevious;
        private final boolean hasNext;
        private final List<ItemSummary> items;

        public static Response getSlice(Long last, boolean hasPrevious, boolean hasNext, List<ItemSummary> contents){
            return new Response(last, hasPrevious, hasNext, contents);
        }
    }
}
