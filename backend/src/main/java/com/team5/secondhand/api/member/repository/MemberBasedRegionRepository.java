package com.team5.secondhand.api.member.repository;

import com.team5.secondhand.api.member.domain.BasedRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBasedRegionRepository extends JpaRepository<BasedRegion, Long> {
}
