package com.team5.secondhand.application.resource.controller;

import com.team5.secondhand.application.resource.dto.response.CategoryList;
import com.team5.secondhand.application.resource.service.ResourceService;
import com.team5.secondhand.global.dto.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @GetMapping("/categories")
    public GenericResponse<CategoryList> getCategories() {
        CategoryList categories = resourceService.getCategories();

        return GenericResponse.send("카테고리 정보입니다.", categories);
    }
}
