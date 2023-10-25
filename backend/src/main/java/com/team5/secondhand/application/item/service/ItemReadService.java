package com.team5.secondhand.application.item.service;

import com.team5.secondhand.application.item.controller.dto.ItemSummary;
import com.team5.secondhand.application.item.controller.v1.dto.request.ItemFilteredSlice;
import com.team5.secondhand.application.item.controller.v1.dto.request.MyItemFilteredSlice;
import com.team5.secondhand.application.item.controller.v1.dto.response.*;
import com.team5.secondhand.application.item.controller.v2.dto.ItemsRequest;
import com.team5.secondhand.application.item.controller.v2.dto.ItemsResponse;
import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import com.team5.secondhand.application.item.exception.ExistItemException;
import com.team5.secondhand.application.item.repository.ItemRepository;
import com.team5.secondhand.application.member.dto.response.MemberDetails;
import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.application.wishlist.service.CheckMemberLikedUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
    public ItemList getItemList(ItemFilteredSlice request, Region region, MemberDetails loginMember) {
        Pageable pageable = PageRequest.of(request.getPage() , PAGE_SIZE, Sort.by("id").descending());

        Slice<Item> pageResult = itemRepository.findAllByBasedRegion(request.getCategoryId(), request.getSellerId(), Status.isSales(request.getIsSales()), region, pageable);
        List<Item> itemEntities = pageResult.getContent();

        List<ItemSummary> items = new ArrayList<>();
        if (!loginMember.isEmpty()) {
            items = getItemSummariesWithIsLike(loginMember, region, itemEntities);
        }

        if (loginMember.isEmpty()) {
            items = getItemSummaries(region, itemEntities);
        }

        return ItemList.getSlice(pageResult.getNumber(), pageResult.hasPrevious(), pageResult.hasNext(), items);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "items", key = "#itemsRequest.last + '-' + #region.id")
    public ItemsResponse getItemList(ItemsRequest itemsRequest, Region region, MemberDetails loginMember) {
        Pageable pageable = PageRequest.ofSize(PAGE_SIZE);
        Slice<Item> pageResult = itemRepository.findAllByIdAndRegion(itemsRequest.getLast(), itemsRequest.getCategoryId(), itemsRequest.getSellerId(), Status.isSales(itemsRequest.getIsSales()), region, pageable);

        List<Item> itemEntities = pageResult.getContent();
        List<ItemSummary> items = new ArrayList<>();
        if (!loginMember.isEmpty()) {
            items = getItemSummariesWithIsLike(loginMember, region, itemEntities);
        }

        if (loginMember.isEmpty()) {
            items = getItemSummaries(region, itemEntities);
        }

        return ItemsResponse.getSlice(items.get(items.size()-1).getId(), pageResult.hasPrevious(), pageResult.hasNext(), items);
    }

    @Cacheable(value = "myItem", key = "#loginMember.id")
    @Transactional(readOnly = true)
    public MyItemList getMyItemList(MyItemFilteredSlice request, MemberDetails loginMember) {
        Pageable pageable = PageRequest.of(request.getPage() , PAGE_SIZE, Sort.by("id").descending());

        Slice<Item> pageResult = itemRepository.findAllByBasedRegion(null, loginMember.getId(), Status.isSales(request.getIsSales()), null, pageable);
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
    public ItemDetail viewAItem(Long id, MemberDetails member, Boolean isLike) throws ExistItemException {
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

    @Transactional(readOnly = true)
    public boolean isValidSeller(Long id, long memberId) {
        Item item = itemRepository.findById(id).orElseThrow();
        return item.isSeller(memberId);
    }
}
