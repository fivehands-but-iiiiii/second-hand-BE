package com.team5.secondhand.api.wishlist.controller;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.service.ItemService;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.service.MemberService;
import com.team5.secondhand.api.wishlist.dto.request.WishlistItem;
import com.team5.secondhand.api.wishlist.dto.request.WishlistFilter;
import com.team5.secondhand.api.wishlist.dto.response.CategoryList;
import com.team5.secondhand.api.wishlist.dto.response.WishItemList;
import com.team5.secondhand.api.wishlist.exception.ExistWishlistException;
import com.team5.secondhand.api.wishlist.service.WishlistService;
import com.team5.secondhand.global.dto.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;
    private final MemberService memberService;
    private final ItemService itemService;

    @PostMapping("/like")
    public GenericResponse<Long> likeItem(@RequestAttribute MemberDetails loginMember, @RequestBody WishlistItem wishListItem) throws ExistMemberIdException, ExistItemException, ExistWishlistException {
        //id 해당하는 item 있는지 확인
        Member member = memberService.findById(loginMember.getId());
        Item item = itemService.findById(wishListItem.getItemId());

        Long id = wishlistService.likeItem(member, item);
        return GenericResponse.send("관심목록에 등록되었습니다.", id);
    }

    @DeleteMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponse<String> unlikeItem(@RequestAttribute MemberDetails loginMember, @RequestParam Long itemId) throws ExistMemberIdException, ExistItemException {
        Member member = memberService.findById(loginMember.getId());
        Item item = itemService.findById(itemId);

        wishlistService.unlikeItem(member, item);

        return GenericResponse.send("좋아요 등록이 취소되었습니다.", null);
    }

    @GetMapping("/categories")
    public GenericResponse<CategoryList> getCategories(@RequestAttribute MemberDetails loginMember) throws ExistMemberIdException {
        CategoryList categories = wishlistService.getCategories(loginMember.getId());

        return GenericResponse.send("카테고리 목록입니다.", categories);
    }

    @GetMapping()
    public GenericResponse<WishItemList> getWishlist(@RequestAttribute MemberDetails loginMember, WishlistFilter wishlistFilter) {
        WishItemList wishlist = wishlistService.getWishlist(loginMember.getId(), wishlistFilter.getPage(), wishlistFilter.getCategory());

        return GenericResponse.send("관심목록 리스트 조회에 성공하였습니다.", wishlist);
    }
}
