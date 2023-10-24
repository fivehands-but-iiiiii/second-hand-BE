package com.team5.secondhand.application.member.domain;

import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.application.region.exception.EmptyBasedRegionException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberId;
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Oauth oauth;

    @Size(min = 1, max = 2)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasedRegion> regions = new ArrayList<>();

    @Builder
    private Member(Long id, String memberId, String profileImgUrl, Oauth oauth, List<BasedRegion> regions) {
        this.id = id;
        this.memberId = memberId;
        this.profileImgUrl = profileImgUrl;
        this.oauth = oauth;
        this.regions = regions;
    }

    public static Member newBasicMember(String memberId, String profileImgUrl, List<BasedRegion> regions) {
        return Member.builder()
                .memberId(memberId)
                .profileImgUrl(profileImgUrl)
                .regions(regions)
                .oauth(Oauth.NONE)
                .build();
    }

    public Long getRepresentedRegionId() throws EmptyBasedRegionException {
        return regions.stream()
                .filter(BasedRegion::isRepresented).findAny()
                .orElseThrow(() -> new EmptyBasedRegionException("메인 지역이 설정되어있지 않습니다."))
                .getId();
    }

    public Long getSubRegionId() {
        try {
            return regions.stream()
                    .filter(BasedRegion::isRepresented).findAny()
                    .orElseThrow(() -> new EmptyBasedRegionException("지역이 설정되어있지 않습니다."))
                    .getId();
        } catch (EmptyBasedRegionException e) {
            return null;
        }
    }

    public void updateBasedRegions(Map<Region, Boolean> regionMap) {
        regions.clear();

        regionMap.keySet().forEach(region -> {
            if (regionMap.get(region)) {
                regions.add(BasedRegion.create(this, region));
            }
            if (!regionMap.get(region)) {
                regions.add(BasedRegion.addSubRegion(this, region));
            }
        });
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Member other = (Member) obj;
        return this.memberId.equals(other.memberId) && this.profileImgUrl.equals(other.profileImgUrl);
    }

    public void updatePlatform(Oauth joinPlatform) {
        this.oauth = joinPlatform;
    }

    public boolean equals(long memberId) {
        return this.id == memberId;
    }

    public void setDefaultProfileImg() {
        this.profileImgUrl = "https://secondhand-team5-bucket.s3.ap-northeast-2.amazonaws.com/members/3689aa38-fb45-4908-94ef-d32b1dfff318-%E1%84%85%E1%85%AE%E1%84%91%E1%85%B53.jpeg";
    }
}
