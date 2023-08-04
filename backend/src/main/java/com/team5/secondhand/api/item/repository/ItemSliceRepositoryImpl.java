package com.team5.secondhand.api.item.repository;

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
                        eqSeller(sellerId)
                )
                .offset(pageable.getOffset())
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
