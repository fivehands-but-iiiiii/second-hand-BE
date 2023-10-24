package com.team5.secondhand.application.wishlist.service;

import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.repository.ItemRepository;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.dto.response.MemberDetails;
import com.team5.secondhand.application.wishlist.domain.Wishlist;
import com.team5.secondhand.application.wishlist.dto.response.CategoryList;
import com.team5.secondhand.application.wishlist.dto.response.WishItem;
import com.team5.secondhand.application.wishlist.dto.response.WishItemList;
import com.team5.secondhand.application.wishlist.exception.ExistWishlistException;
import com.team5.secondhand.application.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService implements CheckMemberLikedUsecase {
    private final WishlistRepository wishlistRepository;
    private final ItemRepository itemRepository;
    private final int FILTER_SIZE = 10;

    @Transactional
    public Long likeItem(Member member, Item item) throws ExistWishlistException {
        if (wishlistRepository.existsByMemberIdAndItemId(member.getId(), item.getId())) {
            throw new ExistWishlistException("이미 좋아요를 눌렀습니다.");
        } else {
            Wishlist wishlist = Wishlist.create(member, item);
            wishlistRepository.save(wishlist);
            itemRepository.updateLikes(item.getCount().getId());

            return wishlist.getId();
        }
    }

    @Transactional
    public void unlikeItem(Member member, Item item) {
        Wishlist wishlist = wishlistRepository.findByMemberAndItem(member, item).orElseThrow(() -> new EntityExistsException("좋아요하지 않은 아이템입니다."));
        wishlistRepository.delete(wishlist);
    }

    @Transactional(readOnly = true)
    public CategoryList getCategories(Long memberId) {
        List<Long> categoryList = wishlistRepository.findByDistinctCategoryByMember(memberId);

        return CategoryList.of(categoryList);
    }

    @Transactional
    public WishItemList getWishlist(Long memberId, int page, Long category) {
        PageRequest pageRequest = PageRequest.of(page, FILTER_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Wishlist> wishlistSlice = wishlistRepository.findAllByItemCategoryAndMemberIdOrderById(pageRequest, category, memberId);
        List<WishItem> wishItems = wishlistSlice.getContent().stream()
                .map(w -> WishItem.of(w.getItem()))
                .collect(Collectors.toList());

        return WishItemList.of(wishItems, page, wishlistSlice.hasNext(), wishlistSlice.hasPrevious());
    }

    @Override
    @Transactional
    public Boolean isMemberLiked(Long itemId, MemberDetails member) {
        if (!member.isEmpty()) {
            return wishlistRepository.existsByMemberIdAndItemId(member.getId(), itemId);
        }
        return false;
    }

    @Override
    @Transactional
    public List<Boolean> isMemberLiked(List<Long> itemId, Long memberId) {
        List<Long> itemIds = wishlistRepository.findAllItemIdByMemberId(memberId);
        return itemId.stream().map(itemIds::contains).collect(Collectors.toList());
    }
}
