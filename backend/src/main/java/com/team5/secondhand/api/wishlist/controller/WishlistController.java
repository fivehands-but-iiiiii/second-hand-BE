package com.team5.secondhand.api.wishlist.controller;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.service.ItemReadService;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.service.MemberService;
import com.team5.secondhand.api.wishlist.dto.request.WishlistFilter;
import com.team5.secondhand.api.wishlist.dto.request.WishlistItem;
import com.team5.secondhand.api.wishlist.dto.response.CategoryList;
import com.team5.secondhand.api.wishlist.dto.response.WishItemList;
import com.team5.secondhand.api.wishlist.exception.ExistWishlistException;
import com.team5.secondhand.api.wishlist.service.WishlistService;
import com.team5.secondhand.global.model.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    private final ItemReadService itemService;

    @Operation(
            summary = "좋아요 누르기",
            tags = "WishList",
            description = "사용자는 상품을 관심상품으로 등록할 수 있다."
    )
    @PostMapping("/like")
    public GenericResponse<Long> likeItem(@RequestAttribute MemberDetails loginMember, @RequestBody WishlistItem wishListItem) throws ExistMemberIdException, ExistItemException, ExistWishlistException {
        Member member = memberService.findById(loginMember.getId());
        Item item = itemService.findById(wishListItem.getItemId());

        Long id = wishlistService.likeItem(member, item);
        return GenericResponse.send("관심목록에 등록되었습니다.", id);
    }

    @Operation(
            summary = "좋아요 취소",
            tags = "WishList",
            description = "사용자는 관심상품을 관심상품 목록에서 삭제할 수 있다."
    )
    @DeleteMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponse<String> unlikeItem(@RequestAttribute MemberDetails loginMember, @RequestParam Long itemId) throws ExistMemberIdException, ExistItemException {
        Member member = memberService.findById(loginMember.getId());
        Item item = itemService.findById(itemId);

        wishlistService.unlikeItem(member, item);
        //TODO item like 숫자 -1 해주기

        return GenericResponse.send("좋아요 등록이 취소되었습니다.", null);
    }

    @Operation(
            summary = "관심상품의 카테고리 목록 보기 ",
            tags = "WishList",
            description = "사용자는 관심상품으로 등록한 글의 카테고리 목록을 볼 수 있다."
    )
    @GetMapping("/categories")
    public GenericResponse<CategoryList> getCategories(@RequestAttribute MemberDetails loginMember) throws ExistMemberIdException {
        CategoryList categories = wishlistService.getCategories(loginMember.getId());

        return GenericResponse.send("카테고리 목록입니다.", categories);
    }

    @Operation(
            summary = "관심상품 목록 보기",
            tags = "WishList",
            description = "사용자는 관심상품으로 등록한 상품의 목록을 볼 수 있다."
    )
    @GetMapping()
    public GenericResponse<WishItemList> getWishlist(@RequestAttribute MemberDetails loginMember, WishlistFilter wishlistFilter) {
        WishItemList wishlist = wishlistService.getWishlist(loginMember.getId(), wishlistFilter.getPage(), wishlistFilter.getCategory());

        return GenericResponse.send("관심목록 리스트 조회에 성공하였습니다.", wishlist);
    }
}
