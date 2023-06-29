package com.team5.secondhand.api.wishlist.service;

import java.util.List;

public interface CheckMemberLikedUsecase {
    Boolean isMemberLiked(Long itemId, Long memberId);
    List<Boolean> isMemberLiked(List<Long> itemId, Long memberId);
}
