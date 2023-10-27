package com.team5.secondhand.global.auth;

import com.team5.secondhand.application.member.dto.response.MemberDetails;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Getter
@Component
@RequestScope
public class AuthenticationContext {
    private Optional<MemberDetails> loginMember;

    public void storeLoginMember(MemberDetails member) {
        this.loginMember = Optional.of(member);
    }
}
