package com.team5.secondhand.global.resource.repository;

import com.team5.secondhand.global.resource.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
}
