package com.team5.secondhand.api.item.service;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.dto.response.ItemList;
import com.team5.secondhand.api.item.dto.response.ItemSummary;
import com.team5.secondhand.api.item.repository.ItemRepository;
import com.team5.secondhand.api.region.domain.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final int PAGE_SIZE = 10;

    private final ItemRepository itemRepository;

    public ItemList getItemList(int page, Region region) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());

        Page<Item> pageResult = itemRepository.findAllByRegion(region, pageable);
        List<ItemSummary> items = pageResult.getContent().stream()
                .map(i -> ItemSummary.of(i, false))
                .collect(Collectors.toList());

        return ItemList.getPage(pageResult.getTotalPages(), items);
    }
}
