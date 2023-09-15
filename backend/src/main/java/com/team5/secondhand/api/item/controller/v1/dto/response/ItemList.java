package com.team5.secondhand.api.item.controller.v1.dto.response;

import com.team5.secondhand.api.item.controller.dto.ItemSummary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ItemList {
    private final int number;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<ItemSummary> items;

    public static ItemList getSlice(int number, boolean hasPrevious, boolean hasNext, List<ItemSummary> contents){
        return new ItemList(number, hasPrevious, hasNext, contents);
    }
}
