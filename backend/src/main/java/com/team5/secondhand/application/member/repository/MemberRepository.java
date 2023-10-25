package com.team5.secondhand.application.member.repository;

import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.domain.Oauth;
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

    Optional<Member> findByMemberIdAndOauth(String memberId, Oauth oauth);

    boolean existsByMemberIdAndOauth(String memberId, Oauth oauth);

    @Modifying
    @Query("update BasedRegion br set br.represented = :status where br.member.id = :id and br.region.id = :regionId")
    int switchBasedRegion(@Param("status") boolean status,
                          @Param("id") Long id,
                          @Param("regionId") Long regionId);
}
