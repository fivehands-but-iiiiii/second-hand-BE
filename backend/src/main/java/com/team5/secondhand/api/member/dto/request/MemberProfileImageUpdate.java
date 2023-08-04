package com.team5.secondhand.api.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class MemberProfileImageUpdate {
    private final MultipartFile profileImage;
}
