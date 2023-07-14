package com.team5.secondhand.api.chatroom.service;

import com.team5.secondhand.api.chatroom.domian.ChatRoom;
import com.team5.secondhand.api.chatroom.exception.ExistChatRoomException;
import com.team5.secondhand.api.chatroom.repository.ChatRoomRepository;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    public String create(Item item, Member buyer) throws ExistChatRoomException {
        if (chatRoomRepository.findByItemAndBuyer(item, buyer).isPresent()) {
            throw new ExistChatRoomException("이미 존재하는 채팅방입니다.");
        }

        ChatRoom chatRoom = ChatRoom.create(item, buyer);

        return chatRoomRepository.save(chatRoom).getChatroomId();
    }
}
