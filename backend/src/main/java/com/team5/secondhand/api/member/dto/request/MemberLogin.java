package com.team5.secondhand.api.member.dto.request;

import com.team5.secondhand.api.member.domain.Oauth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class MemberLogin {
    @NotNull
    @Size(min = 6, max = 16)
    private final String memberId;
    private final Oauth oauth;
}
