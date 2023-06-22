package com.team5.secondhand.global.aws.controller;

import com.team5.secondhand.api.member.dto.request.MemberProfileImageUpdate;
import com.team5.secondhand.global.aws.dto.response.ImageInfo;
import com.team5.secondhand.global.aws.dto.response.ProfileImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import com.team5.secondhand.global.aws.service.ImageHostService;
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

    @Operation(
            summary = "아이템 사진 업로드",
            tags = "Test"
    )
    @PostMapping("/thumbnail/image")
    public GenericResponse<ImageInfo> setItemThumbnail (@RequestBody String url) throws ImageHostException {
        ImageInfo imageInfo = imageHostService.uploadItemThumbnailImage(url);
        return GenericResponse.send("Member upload profile image Successfully", imageInfo);
    }

}
