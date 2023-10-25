package com.team5.secondhand.api.image.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Type {
    JPG(".jpg"), GIF(".gif"), JPEG(".jpeg"), PNG(".png");

    private final String suffix;

    public static boolean isValidType(String name) {
        return Arrays.stream(values()).anyMatch(t -> t.suffix.endsWith(name.toLowerCase()));
    }
}
