package com.team5.secondhand.global.aws.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class ItemImageUpload {
    private final MultipartFile itemImages;
}
