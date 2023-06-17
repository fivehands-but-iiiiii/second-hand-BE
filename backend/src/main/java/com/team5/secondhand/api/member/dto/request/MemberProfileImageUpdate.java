package com.team5.secondhand.api.member.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MemberProfileImageUpdate {
    private Long id;
    private MultipartFile profileImage;
}
