package com.team5.secondhand.api.resource.repository;

import com.team5.secondhand.api.resource.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
}
