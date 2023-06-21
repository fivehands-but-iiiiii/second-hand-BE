package com.team5.secondhand.api.region.service;

import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.repository.RegionRepository;
import com.team5.secondhand.api.region.util.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegionService implements GetValidRegionsUsecase {

    private final RegionRepository regionRepository;
    private final AddressMapper addressMapper;

    public boolean isValidRegion(List<Long> regions) {
        return regionRepository.existsAllByIdIn(regions);
    }

    @Override
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

    public List<Region> findRegionByAddress (String address) {
        String key = addressMapper.mapAddress(address);
        return regionRepository.findAllByAddress(key);
    }
}
