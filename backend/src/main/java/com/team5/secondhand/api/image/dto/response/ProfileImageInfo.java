package com.team5.secondhand.api.image.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class ProfileImageInfo {
    private Long memberId;
    private final String originKey;
    private final String uploadUrl;

    @Builder
    private ProfileImageInfo(Long memberId, String originKey, String url) {
        this.memberId = memberId;
        this.originKey = originKey;
        this.uploadUrl = url;
    }

    public static ProfileImageInfo createNewImageInfo(String originKey, String imageUrl) {
        return ProfileImageInfo.builder()
                .originKey(originKey)
                .url(imageUrl)
                .build();
    }

    public void owned(Long id) {
        this.memberId = id;
    }
}
