package com.team5.secondhand.global.oauth.controller;

import com.team5.secondhand.global.dto.GenericResponse;
import com.team5.secondhand.global.oauth.dto.UserProfile;
import com.team5.secondhand.global.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OAuthController {
    private final OAuthService oAuthService;

    //oAuth 를 사용해서 회원가입
    @GetMapping("/git/join")
    public ResponseEntity<?> join(@RequestParam String code) {
        UserProfile user = oAuthService.join(code);

        return ResponseEntity.ok(GenericResponse.send("추가 회원가입을 완료하세요.", user));
    }
}
