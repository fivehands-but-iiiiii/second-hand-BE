package com.team5.secondhand.api.chatroom.service;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import com.team5.secondhand.api.chatroom.dto.response.ChatroomDetails;
import com.team5.secondhand.api.chatroom.exception.ExistChatRoomException;
import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.service.ItemService;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatRoomFacade {
    private final ChatroomService chatRoomService;
    private final ItemService itemService;
    private final MemberService memberService;

    public ChatroomDetails findChatroomInfo(long itemId, long memberId) throws ExistMemberIdException, ExistItemException {
        Item item = itemService.findById(itemId);
        Member member = memberService.findById(memberId);
        Optional<Chatroom> chatroom = chatRoomService.findChatroom(itemId, memberId);

        if (chatroom.isEmpty()) {
            return ChatroomDetails.from(item);
        }

        return ChatroomDetails.of(chatroom.get(), member);
    }

    public ChatroomDetails findChatroomInfo(String chatroomId, long memberId) throws ExistChatRoomException, NotChatroomMemberException, ExistMemberIdException {
        Chatroom chatroom = chatRoomService.findByChatroomId(chatroomId).orElseThrow(() -> new ExistChatRoomException("해당하는 채팅방이 없습니다."));

        if (!chatroom.isMemberIn(memberId)) {
            throw new NotChatroomMemberException("채팅방 멤버가 아닙니다.");
        }

        Member member = memberService.findById(memberId);

        return ChatroomDetails.of(chatroom, member);
    }
}
