package com.team5.secondhand.api.wishlist.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WishListItem {
    private Long itemId;
}
