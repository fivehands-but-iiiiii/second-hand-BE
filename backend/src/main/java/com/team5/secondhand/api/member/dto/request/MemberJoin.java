package com.team5.secondhand.api.member.dto.request;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.model.Region;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MemberJoin {

    @NotNull
    @Size(min = 6, max = 16)
    private final String memberId;

    private final String profileImgUrl;

    @Size(min = 1, max = 2)
    private final List<Long> regions;

    public Member toMember() {
        return Member.builder()
                .memberId(memberId)
                .profileImageUrl(profileImgUrl)
                .build();
    }
}
