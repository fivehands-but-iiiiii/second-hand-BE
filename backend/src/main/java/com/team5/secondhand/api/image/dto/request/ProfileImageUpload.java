package com.team5.secondhand.api.image.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileImageUpload {

    private final MultipartFile profile;

}
