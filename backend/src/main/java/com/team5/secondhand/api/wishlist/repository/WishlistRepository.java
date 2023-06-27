package com.team5.secondhand.api.wishlist.repository;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.wishlist.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByMemberAndItem(Member member, Item item);

    boolean existsByMemberAndItem(Member member, Item item);

    @Query("SELECT DISTINCT w.item.category from Wishlist w where w.member.id = :memberId")
    List<Long> findByDistinctCategoryByMember(@Param("memberId") Long memberId);
}
