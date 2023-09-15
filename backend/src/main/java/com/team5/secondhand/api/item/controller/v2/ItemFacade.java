package com.team5.secondhand.api.item.controller.v2;

import com.team5.secondhand.api.item.controller.v2.dto.FilteredItems;
import com.team5.secondhand.api.item.service.ItemReadService;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.GetValidRegionsUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemFacade {

    private final ItemReadService itemReadService;
    private final GetValidRegionsUsecase getValidRegionsUsecase;

    public FilteredItems.Response findAllFilteredItems(FilteredItems.Request request, MemberDetails loginMember) throws NotValidRegionException {
        Region region = getValidRegionsUsecase.getRegion(request.getRegionId());
        return itemReadService.getItemList(request, region, loginMember);
    }
}
