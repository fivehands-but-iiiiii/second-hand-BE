package com.team5.secondhand.api.item.repository;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.item.service.dto.ItemListFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ItemSliceRepository {

    Slice<Item> findAllByFilterUsingOffset(ItemListFilter filter, Pageable pageable);

    Slice<Item> findAllByFilterUsingCursor(Long last, ItemListFilter filter, Pageable pageable);
}
