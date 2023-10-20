package com.team5.secondhand.api.region.service;

import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegionService implements ValidationRegionsUsecase {

    private final RegionRepository regionRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Region> getRegions(List<Long> ids) throws NotValidRegionException {
        Map<Long, Region> regions = new HashMap<>();
        for (Region region : regionRepository.findAllById(ids)) {
            regions.put(region.getId(), region);
        }

        if (ids.size() != regions.size()) {
            throw new NotValidRegionException("해당하는 지역이 없습니다.");
        }

        return regions;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "region", key = "#id")
    public Region getRegion(Long id) throws NotValidRegionException {
        return regionRepository.findById(id).orElseThrow(() -> new NotValidRegionException("해당하는 지역이 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Region> findRegionByAddress (String address) {
        return regionRepository.findAllByAddress(address);
    }
}
