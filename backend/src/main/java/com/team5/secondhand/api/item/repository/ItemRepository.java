package com.team5.secondhand.api.item.repository;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.region.domain.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemSliceRepository {
    Optional<Item> findById(Long id);

    Slice<Item> findAllByRegion(Region region, Pageable pageable);

    int countAllByRegion(Region region);
}
