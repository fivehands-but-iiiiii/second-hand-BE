package com.team5.secondhand.global.resource.service;

import com.team5.secondhand.global.resource.domain.Category;
import com.team5.secondhand.global.resource.dto.response.CategoryDetail;
import com.team5.secondhand.global.resource.dto.response.CategoryList;
import com.team5.secondhand.global.resource.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final CategoryRepository categoryRepository;
    public CategoryList getCategories() {
        List<Category> category = categoryRepository.findAll();
        List<CategoryDetail> categoryDetails = category.stream().map(CategoryDetail::of).collect(Collectors.toList());

        return CategoryList.of(categoryDetails);
    }
}
