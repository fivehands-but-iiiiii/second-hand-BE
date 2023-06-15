package com.team5.secondhand.api.member.repository;

import com.team5.secondhand.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying
    @Query("UPDATE Member m SET m.profileImgUrl = :url WHERE m.id = :id")
    int updateMemberProfileImage(@Param("id") Long id,
                                 @Param("url") String url);

    boolean existsByMemberId(String memberId);

    Optional<Member> findByMemberId(String memberId);
}
