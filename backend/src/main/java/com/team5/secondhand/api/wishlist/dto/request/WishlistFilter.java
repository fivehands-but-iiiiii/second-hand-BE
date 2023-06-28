package com.team5.secondhand.api.wishlist.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class WishlistFilter {
    @Size(min = 0)
    private final int page;
    private final Long category;
}
