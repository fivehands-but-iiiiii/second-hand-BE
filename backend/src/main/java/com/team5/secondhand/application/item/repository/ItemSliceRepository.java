package com.team5.secondhand.application.item.repository;

import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import com.team5.secondhand.application.region.domain.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ItemSliceRepository {

    Slice<Item> findAllByBasedRegion(Long categoryId, Long sellerId, List<Status> sales, Region region, Pageable pageable);

    Slice<Item> findAllByIdAndRegion(Long last, Long categoryId, Long sellerId, List<Status> sales, Region region, Pageable pageable);
}
