package com.team5.secondhand.api.member.domain;

import com.team5.secondhand.api.model.BaseTimeEntity;
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
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    private String profileImageUrl;

    @Size(min = 1, max = 2)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<BasedRegion> regions = new ArrayList<>();

    @Builder
    public Member(Long id, String memberId, String profileImageUrl, List<BasedRegion> regions) {
        this.id = id;
        this.memberId = memberId;
        this.profileImageUrl = profileImageUrl;
        this.regions = regions;
    }

    public Member newMember() {
        return new Member();
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

}
