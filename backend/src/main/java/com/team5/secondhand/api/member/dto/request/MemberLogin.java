package com.team5.secondhand.api.member.dto.request;

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
}
