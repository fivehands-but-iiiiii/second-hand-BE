package com.team5.secondhand.api.chatroom.dto.response;

import com.team5.secondhand.api.chatroom.domian.ChatRoom;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class ChatroomDetails {
    private final String opponentId;
    private final ChatItemDetails item;
    private final String chatroomId;

    @Builder
    private ChatroomDetails(String opponentId, ChatItemDetails item, String chatroomId) {
        this.opponentId = opponentId;
        this.item = item;
        this.chatroomId = chatroomId;
    }

    public static ChatroomDetails of(ChatRoom chatRoom, Member member) {
        List<String> chatroomMembers = chatRoom.getChatroomMemberIds();
        chatroomMembers.remove(member.getMemberId());

        return ChatroomDetails.builder().opponentId(chatroomMembers.get(0))
                .item(ChatItemDetails.from(chatRoom.getItem()))
                .chatroomId(chatRoom.getChatroomId())
                .build();
    }

    public static ChatroomDetails from(Item item) {
        return ChatroomDetails.builder().opponentId(item.getSeller().getMemberId())
                .item(ChatItemDetails.from(item))
                .chatroomId(null)
                .build();
    }
}
