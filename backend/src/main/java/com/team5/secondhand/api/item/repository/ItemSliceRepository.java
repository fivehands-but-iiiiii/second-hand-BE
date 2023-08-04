package com.team5.secondhand.api.item.repository;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.region.domain.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ItemSliceRepository {

    Slice<Item> findAllByBasedRegion(Long categoryId, Long sellerId, List<Status> sales, Region region, Pageable pageable);
}
