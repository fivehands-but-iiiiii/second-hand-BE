package com.team5.secondhand.api.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class MemberLogin {
    @NotNull
    private final String memberId;
}
