package com.team5.secondhand.api.member.domain;

import com.team5.secondhand.global.exception.EmptyBasedRegionException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
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

    public void updateBasedRegions(List<BasedRegion> basedRegions) {
        this.regions = basedRegions;
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
}
