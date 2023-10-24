package com.team5.secondhand.api.s3.service.usecase;

import com.team5.secondhand.api.s3.exception.ImageHostException;
import com.team5.secondhand.api.s3.dto.response.ProfileImageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileUpload {
    ProfileImageInfo uploadMemberProfileImage(MultipartFile request) throws ImageHostException;
}
