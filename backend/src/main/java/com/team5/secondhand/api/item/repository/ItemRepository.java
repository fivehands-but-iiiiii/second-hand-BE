package com.team5.secondhand.api.item.repository;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.region.domain.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemSliceRepository {

    Slice<Item> findAllByRegion(Region region, Pageable pageable);

    Optional<Item> getTopByRegion(Region region);

    int countAllByRegion(Region region);

    boolean existsByIdAndSellerId(Long id, Long sellerId);

    @Modifying
    @Query("SELECT DISTINCT category from Item where region.id = :regionId")
    List<Long> countCategoryByRegion(@Param("regionId") Long regionId);

    @Modifying
    @Query("update Item i set i.status = :status where i.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") Status status);

    @Modifying
    @Query("update ItemCounts ic set ic.hits = ic.hits +1 where ic.id = :id")
    void updateHits(@Param("id") Long countsId);

    @Modifying
    @Query("update ItemCounts ic set ic.likeCounts = ic.likeCounts +1 where ic.id = :id")
    void updateLikes(@Param("id") Long countsId);

}
