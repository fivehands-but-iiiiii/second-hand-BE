package com.team5.secondhand.application.member.dto.request;

import com.team5.secondhand.application.member.domain.BasedRegion;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasedRegionSummary {
    private Long id;
    private String district;
    private boolean onFocus;

    public static BasedRegionSummary of(BasedRegion basedRegion) {
        return BasedRegionSummary.builder()
                .id(basedRegion.getRegion().getId())
                .district(basedRegion.getRegion().getDistrict())
                .onFocus(basedRegion.isRepresented())
                .build();
    }
}
