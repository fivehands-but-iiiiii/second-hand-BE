package com.team5.secondhand.api.item.controller;

import com.team5.secondhand.api.item.dto.response.ItemList;
import com.team5.secondhand.api.item.service.ItemService;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.GetValidRegionsUsecase;
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

    @GetMapping
    public ItemList getItemList(@RequestParam int page, @RequestParam long regionIndex) throws NotValidRegionException {
        Map<Long, Region> regions = getValidRegions.getRegions(List.of(regionIndex));
        return itemService.getItemList(page, regions.get(regionIndex));
    }
}
