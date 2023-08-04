package com.team5.secondhand.api.chatroom.dto.response;

import com.team5.secondhand.api.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ChatMember {
    private final String memberId;
    private final String profileImgUrl;

    public static ChatMember of(Member member) {
        return ChatMember.builder()
                .memberId(member.getMemberId())
                .profileImgUrl(member.getProfileImgUrl())
                .build();
    }
}
