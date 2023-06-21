package com.team5.secondhand.api.region.controller;

import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/regions")
    public List<Region> getRegions (String keyword) {
        return regionService.findRegionByAddress(keyword);
    }
}
