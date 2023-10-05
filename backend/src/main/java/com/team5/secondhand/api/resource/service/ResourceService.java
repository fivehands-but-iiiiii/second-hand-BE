package com.team5.secondhand.api.resource.service;

import com.team5.secondhand.api.resource.repository.CategoryRepository;
import com.team5.secondhand.api.resource.domain.Category;
import com.team5.secondhand.api.resource.dto.response.CategoryDetail;
import com.team5.secondhand.api.resource.dto.response.CategoryList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public CategoryList getCategories() {
        List<Category> category = categoryRepository.findAll();
        List<CategoryDetail> categoryDetails = category.stream().map(CategoryDetail::of).collect(Collectors.toList());

        return CategoryList.of(categoryDetails);
    }
}
