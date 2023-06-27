package com.team5.secondhand.api.wishlist.service;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.wishlist.domain.Wishlist;
import com.team5.secondhand.api.wishlist.exception.ExistWishlistException;
import com.team5.secondhand.api.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    public Long likeItem(Member member, Item item) throws ExistWishlistException {
        if (wishlistRepository.existsByMemberAndItem(member, item)) {
            throw new ExistWishlistException("이미 좋아요를 눌렀습니다.");
        } else {
            Wishlist wishlist = Wishlist.create(member, item);
            wishlistRepository.save(wishlist);

            return wishlist.getId();
        }
    }

    public void unlikeItem(Member member, Item item) {
        Wishlist wishlist = wishlistRepository.findByMemberAndItem(member, item).orElseThrow(() -> new EntityExistsException("좋아요하지 않은 아이템입니다."));
        wishlistRepository.delete(wishlist);
    }
}
