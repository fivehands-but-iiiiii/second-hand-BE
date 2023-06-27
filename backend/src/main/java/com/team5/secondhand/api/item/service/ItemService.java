package com.team5.secondhand.api.item.service;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.dto.request.ItemPostWithUrl;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.item.dto.request.ItemFilteredSlice;
import com.team5.secondhand.api.item.dto.response.CategoryList;
import com.team5.secondhand.api.item.dto.response.ItemDetail;
import com.team5.secondhand.api.item.dto.response.ItemList;
import com.team5.secondhand.api.item.dto.response.ItemSummary;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.repository.ItemRepository;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.region.domain.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
    public ItemList getItemList(ItemFilteredSlice request, Region region) {
        Pageable pageable = PageRequest.of(request.getPage() , PAGE_SIZE, Sort.by("id").descending());

        Slice<Item> pageResult = itemRepository.findAllByBasedRegion(request.getCategoryId(), request.getSellerId(), Status.isSales(request.getIsSales()), region, pageable);

        int number = 1;

        //TODO 해당 로직 ItemSummary로 회원의 관심 여부와 함께 편입시킬 것
        List<ItemSummary> items = new ArrayList<>();

        for (Item item : pageResult.getContent()) {
            items.add(ItemSummary.of(item, number++ % 3 == 0));
        }

        return ItemList.getSlice(pageResult.getNumber(), pageResult.hasPrevious(), pageResult.hasNext(), items);
    }

    public Long postItem(Item item, Member seller, Region region) {
        itemRepository.save(item.owned(seller, region));
        return item.getId();
    }

    public void updateItem(Long id, ItemPostWithUrl itemPost, String thumbanilUrl) throws ExistItemException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
        Item newItem = item.updatePost(itemPost, thumbanilUrl);
        itemRepository.save(newItem);
    }

    @Transactional(readOnly = true)
    public ItemDetail getItem(Long id, Long memberId) throws ExistItemException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
        ItemDetail itemDetail;
        if (item.getSeller().getId().equals(memberId)) {
            itemDetail = ItemDetail.of(item, true);
        } else {
            itemDetail = ItemDetail.of(item, false);
        }

        return itemDetail;
    }

    public CategoryList getCategoryList(Long regionId) {
        List<Long> categories = itemRepository.countCategoryByRegion(regionId);
        return CategoryList.of(categories);
    }

    @Transactional
    public boolean updateItemStatus(Long id, Status status) {
        return itemRepository.updateStatus(id, status) == 1;
    }

    public boolean isValidSeller(Long id, long memberId) {
        Item item = itemRepository.findById(id).orElseThrow();
        return item.isSeller(memberId);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public Item findById(Long itemId) throws ExistItemException {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
        return item;
    }
}
