package com.team5.secondhand.api.item.dto.response;

import com.team5.secondhand.api.member.domain.Member;
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
