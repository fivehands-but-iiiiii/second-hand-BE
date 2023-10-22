package com.team5.secondhand.application.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.domain.Oauth;
import com.team5.secondhand.application.member.dto.request.BasedRegionSummary;
import com.team5.secondhand.application.region.exception.EmptyBasedRegionException;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetails implements Serializable {

    private Long id;
    private String memberId;
    private String profileImgUrl;
    private Oauth oauth;
    private List<BasedRegionSummary> regions;

    @Builder
    protected MemberDetails(Long id, String memberId, String profileImgUrl, Oauth oauth, List<BasedRegionSummary> regions) {
        this.id = id;
        this.memberId = memberId;
        this.profileImgUrl = profileImgUrl;
        this.oauth = oauth;
        this.regions = regions;
    }

    public static MemberDetails fromMember(Member member) throws EmptyBasedRegionException {
        List<BasedRegionSummary> regions = member.getRegions().stream()
                .map(BasedRegionSummary::of)
                .collect(Collectors.toList());

        return MemberDetails.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .profileImgUrl(member.getProfileImgUrl())
                .oauth(member.getOauth())
                .regions(regions)
                .build();
    }

    @JsonIgnore
    public static MemberDetails empty() {
        return MemberDetails.builder().build();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.id == null;
    }
}
