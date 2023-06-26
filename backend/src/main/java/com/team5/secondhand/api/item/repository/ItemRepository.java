package com.team5.secondhand.api.item.repository;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.region.domain.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemSliceRepository {
    Optional<Item> findById(Long id);

    Slice<Item> findAllByRegion(Region region, Pageable pageable);

    int countAllByRegion(Region region);

    @Modifying
    @Query("SELECT DISTINCT category from Item where region.id = :regionId")
    List<Long> countCategoryByRegion(Long regionId);

    @Modifying
    @Query("update Item i set i.status = :status where i.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") Status status);
}
