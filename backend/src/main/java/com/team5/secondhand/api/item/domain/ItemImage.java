package com.team5.secondhand.api.item.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage {
    @Size(min = 10, max = 5000)
    private String url;

    @Builder
    protected ItemImage(String url) {
        this.url = url;
    }

    public static ItemImage from(String url) {
        return new ItemImage(url);
    }
}
