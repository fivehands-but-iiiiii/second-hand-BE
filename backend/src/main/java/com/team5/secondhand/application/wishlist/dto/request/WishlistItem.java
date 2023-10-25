package com.team5.secondhand.application.wishlist.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WishlistItem {
    private Long itemId;
}
