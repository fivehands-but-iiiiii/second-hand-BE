package com.team5.secondhand.api.s3.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileImageUpload {

    private final MultipartFile profile;

}
