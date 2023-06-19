package com.team5.secondhand.api.member.controller;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Oauth;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberLogin;
import com.team5.secondhand.api.member.dto.request.MemberRegionUpdate;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.MemberException;
import com.team5.secondhand.api.member.service.MemberService;
import com.team5.secondhand.api.oauth.dto.UserProfile;
import com.team5.secondhand.api.oauth.service.OAuthService;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.NoMainRegionException;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.GetValidRegionsUsecase;
import com.team5.secondhand.global.aws.dto.response.ProfileImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostingException;
import com.team5.secondhand.global.aws.service.ProfileUploadUsecase;
import com.team5.secondhand.global.dto.GenericResponse;
import com.team5.secondhand.global.exception.EmptyBasedRegionException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final OAuthService oAuthService;
    private final MemberService memberService;
    private final ProfileUploadUsecase profileUpload;
    private final GetValidRegionsUsecase validRegions;

    @Operation(
            summary = "회원가입",
            tags = "Members",
            description = "사용자는 회원가입을 할 수 있다."
    )
    @PostMapping("/join")
    public GenericResponse<Long> join(@RequestBody MemberJoin request, HttpSession session) throws MemberException, NotValidRegionException, NoMainRegionException {
        UserProfile tempMember = (UserProfile) session.getAttribute("tempMember");
        Oauth joinPlatform = Oauth.NONE;

        if (tempMember == null) {
            memberService.isValidMemberId(request.getMemberId());
        } else {
            joinPlatform = Oauth.GITHUB;
            memberService.checkDataCorruption(request, tempMember);
            memberService.isExistMemberId(request.getMemberId(), joinPlatform);
        }

        Map<Region, Boolean> basedRegions = BasedRegion.mapping(validRegions.getRegions(request.getRegionsId()), request.getRegions());
        Long joinedId = memberService.join(request, basedRegions, joinPlatform);

        session.invalidate();
        return GenericResponse.send("Member joined Successfully", joinedId);
    }

    @Operation(
            summary = "아이디 중복검사",
            tags = "Members",
            description = "사용자는 회원가입 하기 위해서 아이디 중복검사를 해야한다."
    )
    @GetMapping("/join/availability")
    public GenericResponse<Boolean> checkDuplicateId(String memberId) {
        Boolean isDuplicate = memberService.isExistMemberId(memberId, Oauth.NONE);
        return GenericResponse.send("아이디 중복검사", isDuplicate);
    }


    @Operation(
            summary = "로그인",
            tags = "Members",
            description = "사용자는 로그인을 할 수 있다."
    )
    @PostMapping("/login")
    public GenericResponse<MemberDetails> login(MemberLogin request) throws MemberException, EmptyBasedRegionException {
        MemberDetails member = memberService.login(request);

        return GenericResponse.send("Member login Successfully", member);
    }

    @Operation(

            summary = "로그아웃",
            tags = "Members",
            description = "사용자는 로그아웃을 할 수 있다."
    )
    @PostMapping("/logout")
    public GenericResponse<Long> logout(MemberJoin request) {
        //TODO Auth 헤더 만료
        //TODO response
        return GenericResponse.send("Member joined Successfully", null);
    }

    @GetMapping("/git/login")
    public GenericResponse<MemberDetails> getGithubUser(String code) throws MemberException, EmptyBasedRegionException {
        UserProfile user = oAuthService.getGithubUser(code);
        MemberDetails member = memberService.loginByOAuth(user);

        //todo : jwt 생성
        return GenericResponse.send("Member login Successfully", member);
    }

    @Operation(
            summary = "프로필 사진 변경",
            tags = "Members",
            description = "사용자는 자신의 프로필 사진을 변경할 수 있다."
    )
    @PatchMapping(value = "/members/image", consumes = {"multipart/form-data"})
    public GenericResponse<ProfileImageInfo> setMemberProfile(@RequestParam Long id,
                                                              @RequestPart MultipartFile profile) throws ImageHostingException {
        ProfileImageInfo profileImageInfo = profileUpload.uploadMemberProfileImage(profile);
        profileImageInfo.owned(id);

        memberService.updateProfileImage(id, profileImageInfo.getUploadUrl());
        return GenericResponse.send("Member update profile image Successfully", profileImageInfo);
    }

    @Operation(
            summary = "사용자 지역 변경",
            tags = "Members",
            description = "사용자는 자신의 프로필 사진을 설정할 수 있다."
    )
    @PatchMapping("/members/region")
    public GenericResponse<Boolean> setMemberRegion(@RequestBody Long id,
                                                    @RequestBody MemberRegionUpdate request) {
        //TODO 임시로 파라미터로 member index 받기


        //TODO response
        return GenericResponse.send("Member update profile image Successfully", null);
    }

}
