package com.team5.secondhand.api.item.controller.v2;

import com.team5.secondhand.api.item.controller.v2.dto.ItemsRequest;
import com.team5.secondhand.api.item.controller.v2.dto.ItemsResponse;
import com.team5.secondhand.api.item.service.ItemReadService;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.ValidationRegionsUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemFacade {

    private final ItemReadService itemReadService;
    private final ValidationRegionsUsecase validationRegionsUsecase;

    public ItemsResponse findAllFilteredItems(ItemsRequest itemsRequest, MemberDetails loginMember) throws NotValidRegionException {
        Region region = validationRegionsUsecase.getRegion(itemsRequest.getRegionId());
        return itemReadService.getItemList(itemsRequest, region, loginMember);
    }
}
