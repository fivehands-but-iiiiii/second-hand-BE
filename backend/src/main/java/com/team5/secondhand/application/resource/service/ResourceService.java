package com.team5.secondhand.application.resource.service;

import com.team5.secondhand.application.resource.domain.Category;
import com.team5.secondhand.application.resource.dto.response.CategoryDetail;
import com.team5.secondhand.application.resource.dto.response.CategoryList;
import com.team5.secondhand.application.resource.repository.CategoryRepository;
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
