package com.team5.secondhand.application.item.service;

import com.team5.secondhand.application.item.controller.v1.dto.request.ItemPostWithUrl;
import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import com.team5.secondhand.application.item.exception.ExistItemException;
import com.team5.secondhand.application.item.repository.ItemRepository;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.exception.UnauthorizedException;
import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.global.config.CacheKey;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheKey.ITEM)
public class ItemPostService {

    private final ItemRepository itemRepository;

    @Transactional
    @CacheEvict(allEntries = true)
    public Long postItem(Item item, Member seller, Region region, String thumbnailUrl) {
        item.updateThumbnail(thumbnailUrl);
        itemRepository.save(item.owned(seller, region));
        return item.getId();
    }

    @Transactional
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

    @CacheEvict(allEntries = true)
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}
