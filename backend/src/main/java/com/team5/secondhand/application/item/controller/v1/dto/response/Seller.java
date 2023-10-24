package com.team5.secondhand.application.item.controller.v1.dto.response;

import com.team5.secondhand.application.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Seller {
    private final Long id;
    private final String memberId;

    public static Seller of(Member seller) {
        return Seller.builder()
                .id(seller.getId())
                .memberId(seller.getMemberId())
                .build();
    }
}
