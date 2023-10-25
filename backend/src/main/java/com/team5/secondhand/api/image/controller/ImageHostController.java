package com.team5.secondhand.api.image.controller;

import com.team5.secondhand.api.image.exception.ImageHostException;
import com.team5.secondhand.application.member.dto.request.MemberProfileImageUpdate;
import com.team5.secondhand.api.image.dto.response.ProfileImageInfo;
import com.team5.secondhand.api.image.service.ImageHostService;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageHostController {

    private final ImageHostService imageHostService;

    @Operation(
            summary = "프로필 사진 업로드",
            tags = "Test"
    )
    @PostMapping(value = "/profile/image", consumes = {"multipart/form-data"})
    public GenericResponse<ProfileImageInfo> setMemberProfile (@ModelAttribute MemberProfileImageUpdate profile) throws ImageHostException {
        ProfileImageInfo profileImageInfo = imageHostService.uploadMemberProfileImage(profile.getProfileImage());
        return GenericResponse.send("Member upload profile image Successfully", profileImageInfo);
    }

}
