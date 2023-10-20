package com.team5.secondhand.api.region.service;

import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;

import java.util.List;
import java.util.Map;

public interface ValidationRegionsUsecase {
    Map<Long, Region> getRegions(List<Long> ids) throws NotValidRegionException;

    Region getRegion(Long id) throws NotValidRegionException;
}
