package com.team5.secondhand.api.member.controller;

import com.team5.secondhand.api.member.dto.request.BasedRegionSummary;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberProfileImageUpdate;
import com.team5.secondhand.api.member.dto.request.MemberRegionUpdate;
import com.team5.secondhand.api.member.service.MemberService;
import com.team5.secondhand.api.model.Region;
import com.team5.secondhand.api.region.exception.NotValidRegionException;
import com.team5.secondhand.api.region.service.RegionService;
import com.team5.secondhand.global.aws.dto.response.ProfileImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostingException;
import com.team5.secondhand.global.aws.service.ImageHostService;
import com.team5.secondhand.global.aws.service.ProfileUploadUsecase;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RegionService regionService;
    private final ProfileUploadUsecase profileUploadUsecase;

    @Operation(
        summary = "회원가입",
        tags = "Members",
        description = "사용자는 회원가입을 할 수 있다."
    )
    @PostMapping("/join")
    public GenericResponse<Long> join (@RequestBody MemberJoin request) throws NotValidRegionException {
        List<Long> ids = request.getRegions().stream()
                .map(BasedRegionSummary::getId)
                .collect(Collectors.toList());
        Map<Long, Region> regions = regionService.getRegions(ids);
        Map<Region, Boolean> basedRegions = new HashMap<>();

        for (BasedRegionSummary regionSummary : request.getRegions()) {
            Region region = regions.get(regionSummary.getId());
            basedRegions.put(region, regionSummary.getIsRepresent());
        }

        Long joinedId = memberService.join(request, basedRegions);

        return GenericResponse.send("Member joined Successfully", joinedId);
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
