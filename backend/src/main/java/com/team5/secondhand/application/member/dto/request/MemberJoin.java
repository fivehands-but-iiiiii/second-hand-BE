package com.team5.secondhand.application.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.application.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MemberJoin {

    @NotNull
    @Size(min = 6, max = 16)
    private final String memberId;
    @Size(max = 300)
    private final String profileImgUrl;
    @Size(min = 1, max = 2)
    private final List<BasedRegionSummary> regions;

    @JsonIgnore
    public List<Long> getRegionsId() {
        return regions.stream().map(BasedRegionSummary::getId).collect(Collectors.toList());
    }

    public Member toMember() {
        return Member.builder()
                .memberId(memberId)
                .profileImgUrl(profileImgUrl)
                .regions(new ArrayList<>())
                .build();
    }
}
