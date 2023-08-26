package com.team5.secondhand.global.aws.domain;

import lombok.Getter;

@Getter
public enum Directory {
    ITEM_THUMBNAIL_ORIGIN("items/thumbnails/origin/"),
    ITEM_THUMBNAIL("items/thumbnails/"),
    ITEM_DETAIL("items/"),
    MEMBER_PROFILE_ORIGIN("members/origin/"),
    MEMBER_PROFILE("members/");

    private final String prefix;

    Directory(String prefix) {
        this.prefix = prefix;
    }
}
