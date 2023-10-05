package com.team5.secondhand.api.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.QItemCounts;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.item.service.dto.ItemListFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.team5.secondhand.api.item.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemSliceRepositoryImpl implements ItemSliceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Item> findAllByFilterUsingOffset(ItemListFilter filter, Pageable pageable) {
        int pageSize = pageable.getPageSize()+1;

        List<Item> fetch = jpaQueryFactory.selectFrom(item)
                .where(
                        eqRegion(filter.getRegionId()),
                        eqCategory(filter.getCategoryId()),
                        inSales(filter.getSales()),
                        eqSeller(filter.getSellerId()),
                        eqRegion(region.getId()),
                        eqCategory(categoryId),
                        inSales(sales),
                        eqSeller(sellerId),
                        item.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageSize)
                .orderBy(item.id.desc())
                .fetch();
        return new SliceImpl<>(getContents(fetch, pageSize-1), pageable, hasNext(fetch, pageSize-1));
    }

    @Override
    public Slice<Item> findAllByFilterUsingCursor(Long last, ItemListFilter filter, Pageable pageable) {
        int pageSize = pageable.getPageSize()+1;

        List<Item> fetch = jpaQueryFactory.selectFrom(item)
                .fetchJoin()
                    .on(item.count.eq(QItemCounts.itemCounts))
                .where(
                        eqLast(last),
                        eqRegion(filter.getRegionId()),
                        eqCategory(filter.getCategoryId()),
                        inSales(filter.getSales()),
                        eqSeller(filter.getSellerId()),
                        item.isDeleted.eq(false)
                )
                .limit(pageSize)
                .orderBy(item.id.desc())
                .fetch();
        return new SliceImpl<>(getContents(fetch, pageSize-1), pageable, hasNext(fetch, pageSize-1));
    }

    private boolean hasNext(List<Item> fetch, int pageSize) {
        return fetch.size() > pageSize;
    }

    private List<Item> getContents(List<Item> fetch, int pageSize) {
        return fetch.subList(0, Math.min(fetch.size(), pageSize));
    }

    private BooleanExpression eqLast(Long last) {
        if (last == null) {
            return null;
        }

        return item.id.lt(last);
    }

    private BooleanExpression eqSeller(Long sellerId) {
        if (sellerId == null) {
            return null;
        }
        return item.seller.id.eq(sellerId);
    }

    private BooleanExpression inSales(List<Status> sales) {
        if (sales == null || sales.isEmpty()) {
            return null;
        }
        return item.status.in(sales);
    }

    private BooleanExpression eqCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return item.category.eq(categoryId);
    }

    private BooleanExpression eqRegion(Long regionId) {
        if (regionId == null) {
            return null;
        }
        return item.region.id.eq(regionId);
    }
}
