package com.team5.secondhand.api.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.domain.Oauth;
import com.team5.secondhand.api.member.dto.request.BasedRegionSummary;
import com.team5.secondhand.api.region.exception.EmptyBasedRegionException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@ToString
@RequiredArgsConstructor
public class MemberDetails {

    private final Long id;
    private final String memberId;
    private final String profileImgUrl;
    private final Oauth oauth;
    private final List<BasedRegionSummary> regions;

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

    public boolean isEmpty() {
        return this.id == null;
    }
}
