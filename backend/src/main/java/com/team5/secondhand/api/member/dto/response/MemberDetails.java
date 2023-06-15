package com.team5.secondhand.api.member.dto.response;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.global.exception.EmptyBasedRegionException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class MemberDetails {

    private final long id;
    private final String memberId;
    private final String profileImgUrl;
    private final Long mainRegion;
    private final Long subRegion;

    public static MemberDetails fromMember(Member member) throws EmptyBasedRegionException {

        return MemberDetails.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .profileImgUrl(member.getProfileImgUrl())
                .mainRegion(member.getRepresentedRegionId())
                .subRegion(member.getSubRegionId())
                .build();
    }
}
