package com.team5.secondhand.application.item.controller.v2;

import com.team5.secondhand.application.item.controller.v2.dto.ItemsRequest;
import com.team5.secondhand.application.item.controller.v2.dto.ItemsResponse;
import com.team5.secondhand.application.item.service.ItemReadService;
import com.team5.secondhand.application.member.dto.response.MemberDetails;
import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.application.region.exception.NotValidRegionException;
import com.team5.secondhand.application.region.service.GetValidRegionsUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemFacade {

    private final ItemReadService itemReadService;
    private final GetValidRegionsUsecase getValidRegionsUsecase;

    public ItemsResponse findAllFilteredItems(ItemsRequest itemsRequest, MemberDetails loginMember) throws NotValidRegionException {
        Region region = getValidRegionsUsecase.getRegion(itemsRequest.getRegionId());
        return itemReadService.getItemList(itemsRequest, region, loginMember);
    }
}
