package com.team5.secondhand.api.item.controller;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.dto.request.ItemFilteredSlice;
import com.team5.secondhand.api.item.dto.request.ItemImage;
import com.team5.secondhand.api.item.dto.request.ItemPost;
import com.team5.secondhand.api.item.dto.request.ItemStatusUpdate;
import com.team5.secondhand.api.item.dto.response.ItemDetail;
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
import com.team5.secondhand.global.aws.dto.request.ItemImageUpload;
import com.team5.secondhand.global.aws.dto.response.ImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import com.team5.secondhand.global.aws.service.usecase.ItemDetailImageUpload;
import com.team5.secondhand.global.aws.service.usecase.ItemThumbnailImageUpload;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final GetValidRegionsUsecase getValidRegions;
    private final ItemDetailImageUpload detailImageUpload;
    private final ItemThumbnailImageUpload thumbnailImageUpload;
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

    @Operation(
            summary = "상품 상세 이미지 업로드",
            tags = "Items",
            description = "사용자는 상품 이미지를 추가로 첨부할 수 있다."
    )
    @PostMapping(value = "/image", consumes = {"multipart/form-data"})
    public GenericResponse<ImageInfo> uploadItemImage(@ModelAttribute ItemImageUpload file) throws ImageHostException {
        ImageInfo imageInfo = detailImageUpload.uploadItemDetailImage(file.getItemImages());
        return GenericResponse.send("Image uploaded Successfully", imageInfo);
    }

    @Operation(
            summary = "특정 동네 상품 판매글 등록",
            tags = "Items",
            description = "사용자는 새로운 상품을 등록할 수 있다."
    )
    @PostMapping
    public GenericResponse<Long> postItem(@RequestAttribute("loginMember") MemberDetails loginMember, @RequestBody ItemPost itemPost) throws ExistMemberIdException, NotValidRegionException, ImageHostException {
        Member seller = memberService.findByid(loginMember.getId());
        Region region = getValidRegions.getRegion(itemPost.getRegion());

        Item item = itemPost.toEntity();
        String firstImageUrl = item.getFirstDetailImage().getUrl();

        String thumbnailUrl = thumbnailImageUpload.uploadItemThumbnailImage(firstImageUrl);
        Long id = itemService.postItem(item, thumbnailUrl, seller, region);

        return GenericResponse.send("상품 등록이 완료되었습니다.", id);
    }

    @Operation(
            summary = "특정 동네 상품 판매글 수정",
            tags = "Items",
            description = "사용자는 상품 정보를 수정할 수 있다."
    )
    @PutMapping("/{id}")
    public GenericResponse<Long> updateItem(@PathVariable Long id, @RequestAttribute MemberDetails loginMember, @RequestBody ItemPost itemPost) throws ExistMemberIdException, NotValidRegionException, ExistItemException, ExistItemException {
        //의문: seller, region이 기존이랑 달라져도 되나?
        Member seller = memberService.findByid(loginMember.getId());
        Region region = getValidRegions.getRegion(itemPost.getRegion());
        //TODO: order 1 썸네일 이미지 서비스
        String thumbanilUrl = itemPost.getImages().stream().filter(i -> i.getOrder() == 1).map(ItemImage::getUrl).toString();
        itemService.updateItem(id, itemPost, thumbanilUrl);

        return GenericResponse.send("상품 수정이 완료되었습니다.", id);
    }

    @Operation(
            summary = "상품 글 상세조회",
            tags = "Items",
            description = "사용자는 상품의 상세 정보를 볼 수 있다."
    )
    @GetMapping("/{id}")
    public GenericResponse<ItemDetail> getItem(@PathVariable Long id, @RequestAttribute MemberDetails loginMember) throws ExistMemberIdException, ExistItemException {
        ItemDetail item = itemService.getItem(id, loginMember.getId());

        return GenericResponse.send("상품 상세정보를 볼 수 있습니다.", item);
    }


    @Operation(
            summary = "상품 상태 수정",
            tags = "Items",
            description = "판매자는 상품 판매 상태만 별도로 수정할 수 있다."
    )
    @PatchMapping("/{id}/status")
    public GenericResponse<Boolean> updateItemStatus(@PathVariable Long id, @RequestAttribute MemberDetails loginMember, @RequestBody ItemStatusUpdate request) throws AuthenticationException {
        if (!itemService.isValidSeller(id, loginMember.getId())) {
            throw new AuthenticationException("글 작성자가 아닙니다.");
        }

        boolean result = itemService.updateItemStatus(id, request.getStatus());
        return GenericResponse.send("상품 판매글 상태가 업데이트 되었습니다.", result);
    }

    @Operation(
            summary = "상품 상태 삭제",
            tags = "Items",
            description = "판매자는 상품을 삭제할 수 있다."
    )
    @DeleteMapping("/{id}")
    public GenericResponse<Long> deleteId(@PathVariable Long id, @RequestAttribute MemberDetails loginMember) throws AuthenticationException {
        if (!itemService.isValidSeller(id, loginMember.getId())) {
            throw new AuthenticationException("글 작성자가 아닙니다.");
        }

        itemService.deleteById(id);
        return GenericResponse.send("상품 판매글 상태가 삭제 되었습니다.", id);
    }

}
