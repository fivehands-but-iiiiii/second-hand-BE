package com.team5.secondhand.api.region.controller;

import com.team5.secondhand.api.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

}
