package com.team5.secondhand.application.wishlist.service;

import com.team5.secondhand.application.member.dto.response.MemberDetails;

import java.util.List;

public interface CheckMemberLikedUsecase {
    Boolean isMemberLiked(Long itemId, MemberDetails member);
    List<Boolean> isMemberLiked(List<Long> itemId, Long memberId);
}
