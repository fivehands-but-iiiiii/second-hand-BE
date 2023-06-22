package com.team5.secondhand.global.aws.service.usecase;

import com.team5.secondhand.global.aws.dto.response.ProfileImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostException;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileUpload {
    ProfileImageInfo uploadMemberProfileImage(MultipartFile request) throws ImageHostException;
}
