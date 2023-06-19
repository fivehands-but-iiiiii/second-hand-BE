package com.team5.secondhand.api.item.repository;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.region.domain.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByRegion(Region region, Pageable pageable);
    int countAllByRegion(Region region);
}
