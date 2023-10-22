package com.team5.secondhand.application.item.service;

import com.team5.secondhand.application.item.controller.v1.dto.request.ItemPostWithUrl;
import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import com.team5.secondhand.application.item.exception.ExistItemException;
import com.team5.secondhand.application.item.repository.ItemRepository;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.exception.UnauthorizedException;
import com.team5.secondhand.application.region.domain.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;

@Service
@RequiredArgsConstructor
public class ItemPostService {

    private final ItemRepository itemRepository;

    /**
     * 중고거래 물품을 등록하는 서비스입니다.
     * @param item 중고거래 물품
     * @param seller 판매자
     * @param region 판매 지역
     * @param thumbnailUrl 섬네일 사진 URL
     * @return 등록된 중고거래 물품의 primary key
     */
    @Transactional
    public Long postItem(Item item, Member seller, Region region, String thumbnailUrl) {
        Item updatedItem = item.updateThumbnail(thumbnailUrl)
                .assignOwnership(seller, region);
        return itemRepository.save(updatedItem).getId();
    }

    /**
     * 중고거래 물품 거래 글을 수정합니다.
     * @param id 중고거래물품 primary key
     * @param request 업데이트할 내용이 있는 DTO
     * @param seller 중고거래 물품 거래 글 게시자
     * @throws ExistItemException
     * @throws UnauthorizedException
     */
    @Transactional
    public void updateItem(Long id, ItemPostWithUrl request, Member seller) throws ExistItemException, UnauthorizedException {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ExistItemException("없는 아이템입니다."));

        if (!item.isSeller(seller.getId())) {
            throw new UnauthorizedException("본인의 글만 수정할 수 있습니다.");
        }

        Item updatedItem = item.update(request.toEntity());
        itemRepository.save(updatedItem);
    }

    /**
     * 중고거래 물품 글의 판매상태를 변경합니다.
     * @param id 중고거래 물품 글의 primary key
     * @param status 변경하고 싶은 판매상태
     * @return 실행 결과
     */
    @Transactional
    public boolean updateItemStatus(Long id, Status status, Long memberId) throws AuthenticationException {
        if (!itemRepository.existsByIdAndSellerId(id, memberId)) {
            throw new AuthenticationException("글 작성자가 아닙니다.");
        }

        return itemRepository.updateStatus(id, status) == 1;
    }

    /**
     * 중고거래 물품 글을 삭제합니다.
     * @param id
     */
    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    public void deleteById(Long id, Long memberId) throws AuthenticationException {
        if (!itemRepository.existsByIdAndSellerId(id, memberId)) {
            throw new AuthenticationException("글 작성자가 아닙니다.");
        }

        itemRepository.deleteById(id);
    }
}
