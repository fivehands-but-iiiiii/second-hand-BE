package com.team5.secondhand.api.region.dto;

import com.team5.secondhand.api.region.domain.Region;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegionSummary {
    private final Long id;
    private final String city;
    private final String county;
    private final String district;

    public static RegionSummary fromRegion (Region region) {
        return new RegionSummary(region.getId(), region.getCity(), region.getCounty(), region.getDistrict());
    }
}
