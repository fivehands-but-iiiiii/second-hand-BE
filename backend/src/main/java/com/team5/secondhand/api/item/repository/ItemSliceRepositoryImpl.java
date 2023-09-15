package com.team5.secondhand.api.item.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.region.domain.Region;
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
    public Slice<Item> findAllByBasedRegion(Long categoryId, Long sellerId, List<Status> sales, Region region, Pageable pageable) {
        int pageSize = pageable.getPageSize();

        List<Item> fetch = jpaQueryFactory.selectFrom(item)
                .where(
                        eqRegion(region),
                        eqCategory(categoryId),
                        inSales(sales),
                        eqSeller(sellerId),
                        item.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageSize+1)
                .orderBy(item.id.desc())
                .fetch();
        return new SliceImpl<>(getContents(fetch, pageSize), pageable, hasNext(fetch, pageSize));
    }

    @Override
    public Slice<Item> findAllByIdAndRegion(Long last, Long categoryId, Long sellerId, List<Status> sales, Region region, Pageable pageable) {
        int pageSize = pageable.getPageSize();

        List<Item> fetch = jpaQueryFactory.selectFrom(item)
                .where(
                        eqLast(last),
                        eqRegion(region),
                        eqCategory(categoryId),
                        inSales(sales),
                        eqSeller(sellerId),
                        item.isDeleted.eq(false)
                )
                .limit(pageSize+1)
                .orderBy(item.id.desc())
                .fetch();
        return new SliceImpl<>(getContents(fetch, pageSize), pageable, hasNext(fetch, pageSize));
    }

    private boolean hasNext(List<Item> fetch, int pageSize) {
        return fetch.size() > pageSize;
    }

    private List<Item> getContents(List<Item> fetch, int pageSize) {
        return fetch.subList(0, Math.min(fetch.size(), pageSize));
    }

    private BooleanExpression eqLast(Long last) {
        if (last == null) {
            return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다
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

    private BooleanExpression eqRegion(Region region) {
        if (region == null) {
            return null;
        }
        return item.region.eq(region);
    }
}
