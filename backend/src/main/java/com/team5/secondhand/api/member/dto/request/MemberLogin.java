package com.team5.secondhand.api.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberLogin {
    private String memberId;
}
