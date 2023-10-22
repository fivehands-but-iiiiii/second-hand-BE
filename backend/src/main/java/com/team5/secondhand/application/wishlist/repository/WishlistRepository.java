package com.team5.secondhand.application.wishlist.repository;

import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.wishlist.domain.Wishlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Query("SELECT w FROM Wishlist w WHERE (:itemCategory IS NULL OR w.item.category = :itemCategory) AND (w.member.id = :memberId) AND (w.item.isDeleted = false) ORDER BY w.id DESC")
    Slice<Wishlist> findAllByItemCategoryAndMemberIdOrderById(Pageable page, @Param("itemCategory") Long itemCategory, @Param("memberId") Long memberId);
    Optional<Wishlist> findByMemberAndItem(Member member, Item item);

    @Query("SELECT DISTINCT w.item.category from Wishlist w where w.member.id = :memberId")
    List<Long> findByDistinctCategoryByMember(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndItemId(Long memberId, Long itemId);

    @Query("select w.item.id from Wishlist w where w.member.id = :memberId")
    List<Long> findAllItemIdByMemberId (@Param("memberId") Long memberId);
}
