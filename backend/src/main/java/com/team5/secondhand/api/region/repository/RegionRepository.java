package com.team5.secondhand.api.region.repository;

import com.team5.secondhand.api.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsAllByIdIn(List<Long> id);

}
