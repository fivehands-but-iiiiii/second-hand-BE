package com.team5.secondhand.global.auth;

import com.team5.secondhand.application.member.dto.response.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthenticationContext context;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Optional<MemberDetails> loginMember = context.getLoginMember();
        Login parameterAnnotation = parameter.getParameterAnnotation(Login.class);

        if (parameterAnnotation.required() && loginMember.isEmpty()) {
            throw new AuthenticationException("로그인이 필요한 기능입니다.");
        }

        return loginMember.orElse(MemberDetails.empty());
    }
}
