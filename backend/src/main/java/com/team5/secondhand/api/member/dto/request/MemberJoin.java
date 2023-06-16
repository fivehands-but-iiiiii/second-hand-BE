package com.team5.secondhand.api.member.dto.request;

import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.domain.Oauth;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MemberJoin {

    @NotNull
    @Size(min = 6, max = 16)
    private final String memberId;
    @Size(max = 300)
    private final String profileImgUrl;
    @Size(min = 1, max = 2)
    private final List<BasedRegionSummary> regions;
    @NotNull
    private final Oauth oauth;

    public Member toMember() {
        return Member.builder()
                .memberId(memberId)
                .profileImgUrl(profileImgUrl)
                .oauth(oauth)
                .build();
    }
}
