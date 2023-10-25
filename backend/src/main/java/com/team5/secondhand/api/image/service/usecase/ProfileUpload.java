package com.team5.secondhand.api.image.service.usecase;

import com.team5.secondhand.api.image.exception.ImageHostException;
import com.team5.secondhand.api.image.dto.response.ProfileImageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileUpload {
    ProfileImageInfo uploadMemberProfileImage(MultipartFile request) throws ImageHostException;
}
