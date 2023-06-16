package com.team5.secondhand.api.region.service;

import com.team5.secondhand.api.model.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;

import java.util.List;
import java.util.Map;

public interface GetValidRegionsUsecase {
    Map<Long, Region> getRegions(List<Long> ids) throws NotValidRegionException;
}
