package com.team5.secondhand.api.item.controller;

import com.team5.secondhand.api.item.dto.request.ItemPost;
import com.team5.secondhand.api.item.dto.request.ItemFilteredSlice;
import com.team5.secondhand.api.item.dto.response.ItemList;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.service.ItemService;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.service.MemberService;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.GetValidRegionsUsecase;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final GetValidRegionsUsecase getValidRegions;
    private final MemberService memberService;

    @Operation(
            summary = "특정 동네 판매중인 상품 목록",
            tags = "Items",
            description = "사용자는 자신의 동네의 상품 목록을 볼 수 있다."
    )
    @GetMapping
    public ItemList getItemList(ItemFilteredSlice itemSlice) throws NotValidRegionException {
        Map<Long, Region> regions = getValidRegions.getRegions(List.of(itemSlice.getRegionId()));
        //TODO Category 유효성 검사
        return itemService.getItemList(itemSlice, regions.get(itemSlice.getPage()));
    }

    @PostMapping
    public GenericResponse<Long> postItem(@RequestAttribute MemberDetails loginMember, @RequestBody ItemPost itemPost) throws ExistMemberIdException, NotValidRegionException {
        Member seller = memberService.findByid(loginMember.getId());
        Region region = getValidRegions.getRegion(itemPost.getRegion());
        //TODO: order 1 -> 썸네일 이미지 서비스
        Long id = itemService.postItem(itemPost, seller, region);

        return GenericResponse.send("상품 등록이 완료되었습니다.", id);
    }

    @PutMapping("/{id}")
    public GenericResponse<Long> updateItem(@PathVariable Long id, @RequestAttribute MemberDetails loginMember, @RequestBody ItemPost itemPost) throws ExistMemberIdException, NotValidRegionException, ExistItemException, ExistItemException {
        //의문: seller, region이 기존이랑 달라져도 되나?
        Member seller = memberService.findByid(loginMember.getId());
        Region region = getValidRegions.getRegion(itemPost.getRegion());
        //TODO: order 1 썸네일 이미지 서비스
        itemService.updateItem(id, itemPost);

        return GenericResponse.send("상품 수정이 완료되었습니다.", id);
    }
}
