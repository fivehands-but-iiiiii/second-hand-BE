package com.team5.secondhand.global.aws.controller;

import com.team5.secondhand.global.aws.dto.response.ProfileImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import com.team5.secondhand.global.aws.service.ImageHostService;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageHostController {

    private final ImageHostService imageHostService;

    @Operation(
            summary = "프로필 사진 업로드",
            tags = "Image",
            description = "사용자는 자신의 프로필 사진을 업로드할 수 있다."
    )
    @PostMapping(value = "/profile/image", consumes = {"multipart/form-data"})
    public GenericResponse<ProfileImageInfo> setMemberProfile (@RequestPart MultipartFile profile) throws ImageHostException {
        ProfileImageInfo profileImageInfo = imageHostService.uploadMemberProfileImage(profile);
        return GenericResponse.send("Member upload profile image Successfully", profileImageInfo);
    }

}
