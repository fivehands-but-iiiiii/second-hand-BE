package com.team5.secondhand.api.member.dto.response;

import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.domain.Oauth;
import com.team5.secondhand.api.member.dto.request.BasedRegionSummary;
import com.team5.secondhand.global.exception.EmptyBasedRegionException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@RequiredArgsConstructor
public class MemberDetails {

    private final long id;
    private final String memberId;
    private final String profileImgUrl;
    private final Oauth oauth;
    private final List<BasedRegionSummary> regions;

    public static MemberDetails fromMember(Member member) throws EmptyBasedRegionException {
        List<BasedRegionSummary> regions = member.getRegions().stream()
                .map(region -> BasedRegionSummary.builder().id(region.getId()).onFocus(region.isRepresented()).build())
                .collect(Collectors.toList());

        return MemberDetails.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .profileImgUrl(member.getProfileImgUrl())
                .oauth(member.getOauth())
                .regions(regions)
                .build();
    }
}
