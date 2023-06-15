package com.team5.secondhand.global.aws.service;

import com.team5.secondhand.global.aws.dto.request.ProfileImageUpload;
import com.team5.secondhand.global.aws.dto.response.ProfileImageInfo;
import com.team5.secondhand.global.aws.exception.ImageHostingException;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileUploadUsecase {
    ProfileImageInfo uploadMemberProfileImage(MultipartFile request) throws ImageHostingException;
}
