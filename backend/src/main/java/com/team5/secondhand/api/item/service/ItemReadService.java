package com.team5.secondhand.api.item.service;

import com.team5.secondhand.api.item.controller.dto.ItemSummary;
import com.team5.secondhand.api.item.controller.v1.dto.request.ItemsOffsetRequest;
import com.team5.secondhand.api.item.controller.v1.dto.response.*;
import com.team5.secondhand.api.item.controller.v2.dto.ItemsCursorRequest;
import com.team5.secondhand.api.item.controller.v2.dto.ItemsResponse;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.controller.v1.dto.request.MyItemsRequest;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.repository.ItemRepository;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.wishlist.service.CheckMemberLikedUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemReadService {

    private final int PAGE_SIZE = 10;

    private final ItemRepository itemRepository;
    private final CheckMemberLikedUsecase checkMemberLiked;

    @Transactional(readOnly = true)
    public ItemList getItemList(ItemsOffsetRequest request, Region region, MemberDetails loginMember) {
        Pageable pageable = PageRequest.of(request.getPage() , PAGE_SIZE, Sort.by("id").descending());

        Slice<Item> pageResult = itemRepository.findAllByFilterUsingOffset(request.toFilter(), pageable);

        List<ItemSummary> items = new ArrayList<>();
        if (!loginMember.isEmpty()) {
            items = getItemSummariesWithIsLike(loginMember, region, pageResult.getContent());
        }

        if (loginMember.isEmpty()) {
            items = getItemSummaries(region, pageResult.getContent());
        }

        return ItemList.getSlice(pageResult.getNumber(), pageResult.hasPrevious(), pageResult.hasNext(), items);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "items", key = "#request.last + '-' + #region.id")
    public ItemsResponse getItemList(ItemsCursorRequest request, Region region, MemberDetails loginMember) {
        Pageable pageable = PageRequest.ofSize(PAGE_SIZE);
        Slice<Item> pageResult = itemRepository.findAllByFilterUsingCursor(request.getLast(), request.toFilter(), pageable);

        List<ItemSummary> items = new ArrayList<>();
        if (!loginMember.isEmpty()) {
            items = getItemSummariesWithIsLike(loginMember, region, pageResult.getContent());
        }

        if (loginMember.isEmpty()) {
            items = getItemSummaries(region, pageResult.getContent());
        }

        return ItemsResponse.getSlice(items.get(items.size()-1).getId(), pageResult.hasPrevious(), pageResult.hasNext(), items);
    }

    @Transactional(readOnly = true)
    public MyItemList getMyItemList(MyItemsRequest request, MemberDetails loginMember) {
        Pageable pageable = PageRequest.of(request.getPage() , PAGE_SIZE, Sort.by("id").descending());

        Slice<Item> pageResult = itemRepository.findAllByFilterUsingOffset(request.toFilter(loginMember), pageable);
        List<Item> itemEntities = pageResult.getContent();
        List<MyItemSummary> items = getMyItemSummaries(itemEntities);

        return MyItemList.getSlice(pageResult.getNumber(), pageResult.hasPrevious(), pageResult.hasNext(), items);
    }

    private List<ItemSummary> getItemSummaries(Region region, List<Item> itemEntities) {
        return itemEntities.stream().map(e -> ItemSummary.of(region, e, false)).collect(Collectors.toList());
    }

    private List<MyItemSummary> getMyItemSummaries(List<Item> itemEntities) {
        return itemEntities.stream().map(MyItemSummary::of).collect(Collectors.toList());
    }

    private List<ItemSummary> getItemSummariesWithIsLike(MemberDetails loginMember, Region region, List<Item> itemEntities) {
        List<ItemSummary> items = new ArrayList<>();
        List<Long> itemId = itemEntities.stream().map(Item::getId).collect(Collectors.toList());
        List<Boolean> memberLiked = checkMemberLiked.isMemberLiked(itemId, loginMember.getId());

        for (int i = 0; i < itemId.size(); i++) {
            items.add(ItemSummary.of(region, itemEntities.get(i), memberLiked.get(i)));
        }
        return items;
    }

    /**
     * java docs
     * @param id
     * @param member
     * @param isLike
     * @return
     * @throws ExistItemException
     */
    @Transactional
    @Cacheable(value = "aItem", key = "id")
    public ItemDetail viewAItem(long id, MemberDetails member, Boolean isLike) throws ExistItemException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
        itemRepository.updateHits(item.getCount().getId());

        boolean isSeller = false;
        if (!member.isEmpty()) {
            isSeller = item.isSeller(member.getId());
        }
        return ItemDetail.of(item, isSeller, isLike);
    }

    @Transactional(readOnly = true)
    public CategoryList getCategoryList(Long regionId) {
        List<Long> categories = itemRepository.countCategoryByRegion(regionId);
        return CategoryList.of(categories);
    }

    public Item findById(Long itemId) throws ExistItemException {
        return itemRepository.findById(itemId).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
    }
}
