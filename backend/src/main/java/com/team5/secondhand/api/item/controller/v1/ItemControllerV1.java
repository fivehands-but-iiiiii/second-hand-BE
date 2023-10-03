package com.team5.secondhand.api.item.controller.v1;

import com.team5.secondhand.api.item.controller.v1.dto.response.CategoryList;
import com.team5.secondhand.api.item.controller.v1.dto.response.ItemDetail;
import com.team5.secondhand.api.item.controller.v1.dto.response.ItemList;
import com.team5.secondhand.api.item.controller.v1.dto.response.MyItemList;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.ItemImage;
import com.team5.secondhand.api.item.controller.v1.dto.request.MyItemsRequest;
import com.team5.secondhand.api.item.controller.v1.dto.request.ItemsOffsetRequest;
import com.team5.secondhand.api.item.controller.v1.dto.request.ItemPost;
import com.team5.secondhand.api.item.controller.v1.dto.request.ItemStatusUpdate;
import com.team5.secondhand.api.item.controller.v1.dto.request.ItemUpdateRequest;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.service.ItemPostService;
import com.team5.secondhand.api.item.service.ItemReadService;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.exception.UnauthorizedException;
import com.team5.secondhand.api.member.service.MemberService;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.GetValidRegionsUsecase;
import com.team5.secondhand.api.wishlist.service.WishlistService;
import com.team5.secondhand.global.aws.dto.request.ItemImageUpload;
import com.team5.secondhand.global.aws.dto.response.ImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import com.team5.secondhand.global.aws.service.usecase.ItemDetailImageUpload;
import com.team5.secondhand.global.aws.service.usecase.ItemThumbnailImageUpload;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemControllerV1 {

    private final ItemReadService itemReadService;
    private final ItemPostService itemPostService;
    private final GetValidRegionsUsecase getValidRegions;
    private final ItemDetailImageUpload detailImageUpload;
    private final ItemThumbnailImageUpload thumbnailImageUpload;
    private final MemberService memberService;
    private final WishlistService wishlistService;

    @Operation(
            summary = "특정 동네 판매중인 상품 목록",
            tags = "Items",
            description = "사용자는 자신의 동네의 상품 목록을 볼 수 있다."
    )
    @GetMapping
    public GenericResponse<ItemList> getItemList(ItemsOffsetRequest itemSlice, @RequestAttribute(required = false) MemberDetails loginMember) throws NotValidRegionException {
        Region regions = getValidRegions.getRegion(itemSlice.getRegionId());
        ItemList itemList = itemReadService.getItemList(itemSlice, regions, loginMember);
        return GenericResponse.send("상품 목록이 조회되었습니다.", itemList);
    }

    @Operation(
            summary = "내가 등록한 상품 목록",
            tags = "Items",
            description = "사용자는 자신이 판매 중인/완료한 상품 목록을 볼 수 있다"
    )
    @GetMapping("/mine")
    public GenericResponse<MyItemList> getItemMyList(MyItemsRequest itemSlice, @RequestAttribute MemberDetails loginMember) throws UnauthorizedException {
        if (loginMember.isEmpty()) {
            throw new UnauthorizedException("로그인이 필요한 기능입니다.");
        }

        return GenericResponse.send("판매 내역이 조회되었습니다.", itemReadService.getMyItemList(itemSlice, loginMember));
    }

    @Operation(
            summary = "상품 상세 이미지 업로드",
            tags = "Items",
            description = "사용자는 상품 이미지를 추가로 첨부할 수 있다."
    )
    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public GenericResponse<ImageInfo> uploadItemImage(@ModelAttribute ItemImageUpload file) throws ImageHostException {
        ImageInfo imageInfo = detailImageUpload.uploadItemDetailImage(file.getItemImages());
        return GenericResponse.send("Image uploaded Successfully", imageInfo);
    }

    @Operation(
            summary = "특정 동네 상품 판매글 등록",
            tags = "Items",
            description = "사용자는 새로운 상품을 등록할 수 있다."
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public GenericResponse<Long> postItem(@RequestAttribute MemberDetails loginMember, ItemPost itemPost) throws ExistMemberIdException, NotValidRegionException, ImageHostException, AuthenticationException {
        if (loginMember == null) {
            throw new AuthenticationException("로그인이 필요한 기능입니다.");
        }

        Member seller = memberService.findById(loginMember.getId());
        Region region = getValidRegions.getRegion(itemPost.getRegion());
        List<ItemImage> itemImages = detailImageUpload.uploadItemDetailImages(itemPost.getImages());

        Item item = itemPost.toEntity(itemImages);
        String thumbnailUrl = thumbnailImageUpload.uploadItemThumbnailImage(item);

        Long id = itemPostService.postItem(item, seller, region, thumbnailUrl);

        return GenericResponse.send("상품 등록이 완료되었습니다.", id);
    }

    @Operation(
            summary = "특정 동네 상품 판매글 수정",
            tags = "Items",
            description = "사용자는 상품 정보를 수정할 수 있다."
    )
    @PutMapping("/{id}")
    public GenericResponse<Long> updateItem(@PathVariable Long id, @RequestAttribute MemberDetails loginMember, @RequestBody ItemUpdateRequest itemPost) throws ExistMemberIdException, NotValidRegionException, ExistItemException, ExistItemException, UnauthorizedException {
        Member seller = memberService.findById(loginMember.getId());
        Region region = getValidRegions.getRegion(itemPost.getRegion());

        itemPostService.updateItem(id, itemPost, seller);

        return GenericResponse.send("상품 수정이 완료되었습니다.", id);
    }

    @Operation(
            summary = "상품 글 상세 조회",
            tags = "Items",
            description = "사용자는 상품의 상세 정보를 볼 수 있다."
    )
    @GetMapping("/{id}")
    public GenericResponse<ItemDetail> getItem(@PathVariable Long id, @RequestAttribute MemberDetails loginMember) throws ExistItemException {
        Boolean isLike = wishlistService.isMemberLiked(id, loginMember);
        ItemDetail item = itemReadService.viewAItem(id, loginMember, isLike);

        return GenericResponse.send("상품 상세정보를 볼 수 있습니다.", item);
    }


    @Operation(
            summary = "상품 상태 수정",
            tags = "Items",
            description = "판매자는 상품 판매 상태만 별도로 수정할 수 있다."
    )
    @PatchMapping("/{id}/status")
    public GenericResponse<Boolean> updateItemStatus(@PathVariable Long id, @RequestAttribute MemberDetails loginMember, @RequestBody ItemStatusUpdate request) throws AuthenticationException {
        if (itemReadService.isValidSeller(id, loginMember.getId())) {
            throw new AuthenticationException("글 작성자가 아닙니다.");
        }

        boolean result = itemPostService.updateItemStatus(id, request.getStatus());
        return GenericResponse.send("상품 판매글 상태가 업데이트 되었습니다.", result);
    }

    @Operation(
            summary = "상품 상태 삭제",
            tags = "Items",
            description = "판매자는 상품을 삭제할 수 있다."
    )
    @DeleteMapping("/{id}")
    public GenericResponse<Long> deleteId(@PathVariable Long id, @RequestAttribute MemberDetails loginMember) throws AuthenticationException {
        if (itemReadService.isValidSeller(id, loginMember.getId())) {
            throw new AuthenticationException("글 작성자가 아닙니다.");
        }

        itemPostService.deleteById(id);
        return GenericResponse.send("상품 판매글 상태가 삭제 되었습니다.", id);
    }


    @Operation(
            summary = "판매중인 아이템의 카테고리 목록 보기",
            tags = "Items",
            description = "사용자는 자신의 동네의 판매중인 카테고리 목록을 볼 수 있다."
    )
    @GetMapping("/categories")
    public GenericResponse<CategoryList> getCategoryList(@RequestParam Long regionId) {
        CategoryList categoryList = itemReadService.getCategoryList(regionId);

        return GenericResponse.send("카테고리 목록입니다.", categoryList);
    }
}
