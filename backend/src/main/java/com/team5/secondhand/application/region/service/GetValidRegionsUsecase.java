package com.team5.secondhand.application.region.service;

import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.application.region.exception.NotValidRegionException;

import java.util.List;
import java.util.Map;

public interface GetValidRegionsUsecase {
    Map<Long, Region> getRegions(List<Long> ids) throws NotValidRegionException;

    Region getRegion(Long id) throws NotValidRegionException;
}
