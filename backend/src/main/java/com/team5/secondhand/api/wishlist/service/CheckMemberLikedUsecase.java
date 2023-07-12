package com.team5.secondhand.api.wishlist.service;

import com.team5.secondhand.api.member.dto.response.MemberDetails;

import java.util.List;

public interface CheckMemberLikedUsecase {
    Boolean isMemberLiked(Long itemId, MemberDetails member);
    List<Boolean> isMemberLiked(List<Long> itemId, Long memberId);
}
