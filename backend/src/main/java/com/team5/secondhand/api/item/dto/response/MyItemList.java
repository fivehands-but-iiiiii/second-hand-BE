package com.team5.secondhand.api.item.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyItemList {
    private final int number;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<MyItemSummary> items;

    public static MyItemList getSlice(int number, boolean hasPrevious, boolean hasNext, List<MyItemSummary> contents){
        return new MyItemList(number, hasPrevious, hasNext, contents);
    }
}