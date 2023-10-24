package com.team5.secondhand.application.region.repository;

import com.team5.secondhand.application.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsAllByIdIn(List<Long> id);

    @Query(value = "SELECT * FROM region r WHERE MATCH(r.city, r.county, r.district) AGAINST(:address IN NATURAL LANGUAGE MODE) limit 100", nativeQuery = true)
    List<Region> findAllByAddress(@Param("address") String address);
}
