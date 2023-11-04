package com.team5.secondhand.application.item.controller.v2;

import com.team5.secondhand.application.item.controller.v2.dto.ItemsRequest;
import com.team5.secondhand.application.item.controller.v2.dto.ItemsResponse;
import com.team5.secondhand.application.member.dto.response.MemberDetails;
import com.team5.secondhand.application.region.exception.NotValidRegionException;
import com.team5.secondhand.global.model.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v2/items")
@RequiredArgsConstructor
public class ItemControllerV2 {

    private final ItemFacade itemFacade;

    @Operation(
            summary = "특정 동네 판매중인 상품 목록",
            tags = "Items",
            description = "사용자는 자신의 동네의 상품 목록을 볼 수 있다."
    )
    @GetMapping
    public GenericResponse<ItemsResponse> getItemList(ItemsRequest itemsRequest, @RequestAttribute(required = false) MemberDetails loginMember) throws NotValidRegionException {
        ItemsResponse allFilteredItems = itemFacade.findAllFilteredItems(itemsRequest, loginMember);
        return GenericResponse.send("상품 목록이 조회되었습니다.", allFilteredItems);
    }
}
