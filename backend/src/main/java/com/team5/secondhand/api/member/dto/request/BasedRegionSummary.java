package com.team5.secondhand.api.member.dto.request;

import com.team5.secondhand.api.member.domain.BasedRegion;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasedRegionSummary {
    private Long id;
    private boolean onFocus;

    public static BasedRegionSummary of(BasedRegion basedRegion) {
        return BasedRegionSummary.builder()
                .id(basedRegion.getRegion().getId())
                .onFocus(basedRegion.isRepresented())
                .build();
    }
}
