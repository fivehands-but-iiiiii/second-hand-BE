package com.team5.secondhand.api.item.service;

import com.team5.secondhand.api.item.controller.v1.dto.request.ItemPostWithUrl;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.repository.ItemRepository;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.exception.UnauthorizedException;
import com.team5.secondhand.api.region.domain.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemPostService {

    private final ItemRepository itemRepository;

    @Transactional
    @CachePut(value = "myItem", key = "#seller.id")
    public Long postItem(Item item, Member seller, Region region, String thumbnailUrl) {
        item.updateThumbnail(thumbnailUrl);
        itemRepository.save(item.owned(seller, region));
        return item.getId();
    }

    @Transactional
    @CachePut(value = "aItem", key = "#id")
    public void updateItem(Long id, ItemPostWithUrl itemPost, Member seller) throws ExistItemException, UnauthorizedException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistItemException("없는 아이템입니다."));

        if (!item.isSeller(seller.getId())) {
            throw new UnauthorizedException("본인의 글만 수정할 수 있습니다.");
        }

        Item newItem = item.updatePost(itemPost, itemPost.getImages().get(0).getUrl());
        itemRepository.save(newItem);
    }

    @Transactional
    public boolean updateItemStatus(Long id, Status status) {
        return itemRepository.updateStatus(id, status) == 1;
    }

    @CacheEvict(value = "menu", allEntries = true)
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}
