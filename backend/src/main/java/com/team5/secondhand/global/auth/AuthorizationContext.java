package com.team5.secondhand.global.auth;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Slf4j
@Getter
@Component
@RequestScope
public class AuthorizationContext {

    private Optional<MemberDetails> loginMember;

    public void setLoginMember(MemberDetails member) {
        log.debug("👀 새로운 컨텍스트인가요? = {}", this);
        log.debug("👀 로그인한 회원은 누구인가요? = {}" , member.getMemberId());
        this.loginMember = Optional.ofNullable(member);
    }

}
