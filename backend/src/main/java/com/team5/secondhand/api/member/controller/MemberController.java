package com.team5.secondhand.api.member.controller;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Oauth;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberLogin;
import com.team5.secondhand.api.member.dto.request.MemberRegion;
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
import com.team5.secondhand.global.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import static com.team5.secondhand.api.member.exception.MemberExceptionHandler.JOIN_SESSION_KEY;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final OAuthService oAuthService;
    private final MemberService memberService;
    private final ProfileUploadUsecase profileUpload;
    private final GetValidRegionsUsecase validRegions;
    private final JwtService jwtService;

    @Operation(
            summary = "회원가입",
            tags = "Members",
            description = "사용자는 회원가입을 할 수 있다."
    )
    @PostMapping("/join")
    public GenericResponse<Long> join(@RequestBody MemberJoin request, HttpSession session) throws MemberException, NotValidRegionException, NoMainRegionException {
        UserProfile tempMember = (UserProfile) session.getAttribute(JOIN_SESSION_KEY);
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
    public GenericResponse<MemberDetails> login(MemberLogin request, HttpServletResponse response) throws MemberException, EmptyBasedRegionException, IOException {
        MemberDetails member = memberService.login(request);
        jwtService.setTokenHeader(member, response);

        return GenericResponse.send("Member login Successfully", member);
    }

    @GetMapping("/git/login")
    public GenericResponse<MemberDetails> getGithubUser(String code, HttpServletResponse response) throws MemberException, EmptyBasedRegionException, IOException {
        UserProfile profile = oAuthService.getGithubUser(code);
        MemberDetails member = memberService.loginByOAuth(profile);
        jwtService.setTokenHeader(member, response);

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
            description = "사용자는 내 동네 설정을 할 수 있다."
    )
    @PostMapping("/members/region")
    public GenericResponse<Boolean> setMemberRegion(@RequestBody MemberRegion memberRegion) throws NotValidRegionException, NoMainRegionException {
        //region id 적합 확인
        Map<Region, Boolean> basedRegions = BasedRegion.mapping(validRegions.getRegions(memberRegion.getRegionsId()), memberRegion.getRegions());

        memberService.updateRegions(memberRegion.getId(), basedRegions);

        //TODO response
        return GenericResponse.send("Member update profile image Successfully", null);
    }

    @Operation(
            summary = "사용자 지역 스위치",
            tags = "Members",
            description = "사용자는 내 동네 전환을 할 수 있다."
    )
    @PatchMapping("/members/region")
    public GenericResponse<Boolean> switchMemberRegion(@RequestBody MemberRegion memberRegion) {
        //TODO: 지역 검증은 추후에 validator 로 검증
        memberService.switchRegions(memberRegion.getId(), memberRegion);

        //TODO response
        return GenericResponse.send("Member update profile image Successfully", null);
    }

}
