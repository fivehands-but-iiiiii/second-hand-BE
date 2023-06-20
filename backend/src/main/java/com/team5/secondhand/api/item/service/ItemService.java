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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final int PAGE_SIZE = 10;

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public ItemList getItemList(int page, Region region) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());

        Page<Item> pageResult = itemRepository.findAllByRegion(region, pageable);

        int number = 1;

        //TODO 해당 로직 ItemSummary로 회원의 관심 여부와 함께 편입시킬 것
        List<ItemSummary> items = new ArrayList<>();

        for (Item item : pageResult.getContent()) {
            items.add(ItemSummary.of(item, number++ % 3 == 0));
        }

        return ItemList.getPage(pageResult.getTotalPages(), items);
    }
}
