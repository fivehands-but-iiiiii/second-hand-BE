package com.team5.secondhand.api.member.controller;

import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberProfileImageUpdate;
import com.team5.secondhand.api.member.dto.request.MemberRegionUpdate;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    @Operation(
        summary = "회원가입",
        tags = "Members",
        description = "사용자는 회원가입을 할 수 있다."
    )
    @PostMapping("/join")
    public GenericResponse<Long> join (MemberJoin request) {
        //TODO service 호출

        //TODO response
        return GenericResponse.send("Member joined Successfully", null);
    }

    @Operation(
        summary = "로그인",
        tags = "Members",
        description = "사용자는 로그인을 할 수 있다."
    )
    @PostMapping("/login")
    public GenericResponse<Member> login (MemberJoin request) {
        //TODO service 호출

        //TODO response
        return GenericResponse.send("Member joined Successfully", null);
    }

    @Operation(
            summary = "프로필 사진 설정",
            tags = "Members",
            description = "사용자는 자신의 프로필 사진을 설정할 수 있다."
    )
    @PatchMapping("/members/image")
    public GenericResponse<Boolean> setMemberProfile (@RequestBody Long id,
                                                      @RequestBody MemberProfileImageUpdate request) {
        //TODO 임시로 파라미터로 member index 받기

        //TODO service 호출

        //TODO response
        return GenericResponse.send("Member update profile image Successfully", null);
    }

    @Operation(
            summary = "프로필 사진 설정",
            tags = "Members",
            description = "사용자는 자신의 프로필 사진을 설정할 수 있다."
    )
    @PatchMapping("/members/region")
    public GenericResponse<Boolean> setMemberRegion (@RequestBody Long id,
                                                     @RequestBody MemberRegionUpdate request) {
        //TODO 임시로 파라미터로 member index 받기

        //TODO service 호출

        //TODO response
        return GenericResponse.send("Member update profile image Successfully", null);
    }

}
