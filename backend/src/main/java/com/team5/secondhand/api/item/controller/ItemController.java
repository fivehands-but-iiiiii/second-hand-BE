package com.team5.secondhand.api.item.controller;

import com.team5.secondhand.api.item.dto.request.ItemFilteredSlice;
import com.team5.secondhand.api.item.dto.response.ItemList;
import com.team5.secondhand.api.item.service.ItemService;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.GetValidRegionsUsecase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final GetValidRegionsUsecase getValidRegions;

    @Operation(
            summary = "특정 동네 판매중인 상품 목록",
            tags = "Items",
            description = "사용자는 자신의 동네의 상품 목록을 볼 수 있다."
    )
    @GetMapping
    public ItemList getItemList(ItemFilteredSlice itemSlice) throws NotValidRegionException {
        Map<Long, Region> regions = getValidRegions.getRegions(List.of(itemSlice.getRegionId()));
        //TODO Category 유효성 검사
        return itemService.getItemList(itemSlice, regions.get(itemSlice.getPage()));
    }

}
