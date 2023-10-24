package com.team5.secondhand.application.member.dto.request;

import com.team5.secondhand.application.member.domain.BasedRegion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasedRegionSummary {
    private Long id;
    private String district;
    private boolean onFocus;

    @Builder
    protected BasedRegionSummary(Long id, String district, boolean onFocus) {
        this.id = id;
        this.district = district;
        this.onFocus = onFocus;
    }

    public static BasedRegionSummary of(BasedRegion basedRegion) {
        return BasedRegionSummary.builder()
                .id(basedRegion.getRegion().getId())
                .district(basedRegion.getRegion().getDistrict())
                .onFocus(basedRegion.isRepresented())
                .build();
    }
}
