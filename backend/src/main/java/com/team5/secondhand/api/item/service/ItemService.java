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
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.wishlist.service.CheckMemberLikedUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final int PAGE_SIZE = 10;

    private final ItemRepository itemRepository;
    private final CheckMemberLikedUsecase checkMemberLiked;

    @Transactional(readOnly = true)
    public ItemList getItemList(ItemFilteredSlice request, Region region, MemberDetails loginMember) {
        Pageable pageable = PageRequest.of(request.getPage() , PAGE_SIZE, Sort.by("id").descending());

        Slice<Item> pageResult = itemRepository.findAllByBasedRegion(request.getCategoryId(), request.getSellerId(), Status.isSales(request.getIsSales()), region, pageable);
        List<Item> itemEntities = pageResult.getContent();

        List<ItemSummary> items = new ArrayList<>();
        if (loginMember != null) {
            items = getItemSummariesWithIsLike(loginMember, itemEntities);
        }

        if (loginMember == null) {
            items = itemEntities.stream().map(e -> ItemSummary.of(e, false)).collect(Collectors.toList());
        }

        return ItemList.getSlice(pageResult.getNumber(), pageResult.hasPrevious(), pageResult.hasNext(), items);
    }

    private List<ItemSummary> getItemSummariesWithIsLike(MemberDetails loginMember, List<Item> itemEntities) {
        List<ItemSummary> items = new ArrayList<>();
        List<Long> itemId = itemEntities.stream().map(Item::getId).collect(Collectors.toList());
        List<Boolean> memberLiked = checkMemberLiked.isMemberLiked(itemId, loginMember.getId());

        for (int i = 0; i < itemId.size(); i++) {
            items.add(ItemSummary.of(itemEntities.get(i), memberLiked.get(i)));
        }
        return items;
    }

    public Long postItem(Item item, Member seller, Region region, String thumbnailUrl) {
        item.updateThumbnail(thumbnailUrl);
        itemRepository.save(item.owned(seller, region));
        return item.getId();
    }

    public void updateItem(Long id, ItemPostWithUrl itemPost, String thumbanilUrl) throws ExistItemException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
        Item newItem = item.updatePost(itemPost, thumbanilUrl);
        itemRepository.save(newItem);
    }

    // @Cacheable(value = "itemCache")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public ItemDetail viewAItem(Long id, Long memberId, Boolean isLike) throws ExistItemException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
        itemRepository.updateHits(item.getCount().getId());
        return ItemDetail.of(item, item.isSeller(memberId), isLike);
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
