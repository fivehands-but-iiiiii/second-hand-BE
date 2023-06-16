package com.team5.secondhand.api.member.domain;

import com.team5.secondhand.api.member.dto.request.BasedRegionSummary;
import com.team5.secondhand.api.model.Region;
import com.team5.secondhand.api.region.exception.NoMainRegionException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasedRegion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private boolean represented;

    @Builder
    private BasedRegion(Long id, Member member, Region region, boolean represented) {
        this.id = id;
        this.member = member;
        this.region = region;
        this.represented = represented;
    }

    public static Map<Region, Boolean> mapping(Map<Long, Region> regions, List<BasedRegionSummary> regionSummaries) throws NoMainRegionException {
        
        if (regionSummaries.isEmpty()) {
            throw new NoMainRegionException("지역을 1개 이상 설정해야 합니다.");
        }

        Map<Region, Boolean> basedRegions = new HashMap<>();

        for (BasedRegionSummary regionSummary : regionSummaries) {
            Region region = regions.get(regionSummary.getId());
            basedRegions.put(region, regionSummary.getOnFocus());
        }

        if (!basedRegions.containsValue(true)) {
            throw new NoMainRegionException("대표 지역이 지정되어 있지 않습니다.");
        }

        return basedRegions;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addBasedRegion(Region region) {
        this.region = region;
    }

    public void isRepresent() {
        this.represented = true;
    }

    public void isNotRepresent() {
        this.represented = false;
    }

    public static BasedRegion create(Member member, Region region) {
        BasedRegion membersRegion = BasedRegion.builder()
                .represented(true)
                .build();
        membersRegion.addMember(member);
        membersRegion.addBasedRegion(region);
        return membersRegion;
    }

    public static BasedRegion addSubRegion(Member member, Region region) {
        BasedRegion membersRegion = BasedRegion.builder()
                .represented(false)
                .build();
        membersRegion.addMember(member);
        membersRegion.addBasedRegion(region);
        return membersRegion;
    }
}
