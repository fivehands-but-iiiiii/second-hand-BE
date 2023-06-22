package com.team5.secondhand.api.item.service;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.dto.request.ItemPost;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.item.dto.request.ItemFilteredSlice;
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

    public Long postItem(ItemPost itemPost, String thumbanilUrl, Member member, Region region) {
        Item item = Item.create(itemPost, thumbanilUrl, member, region);
        itemRepository.save(item);

        return item.getId();
    }

    public void updateItem(Long id, ItemPost itemPost, String thumbanilUrl) throws ExistItemException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));
        Item newItem = item.updatePost(itemPost, thumbanilUrl);
        itemRepository.save(newItem);
    }
}
