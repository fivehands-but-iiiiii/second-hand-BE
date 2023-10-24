package com.team5.secondhand.application.item.controller.v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.application.item.controller.dto.ItemSummary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ItemsResponse implements Serializable {
    private final Long last;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<ItemSummary> items;

    @JsonIgnore
    public static ItemsResponse getSlice(Long last, boolean hasPrevious, boolean hasNext, List<ItemSummary> contents){
        return new ItemsResponse(last, hasPrevious, hasNext, contents);
    }
}
