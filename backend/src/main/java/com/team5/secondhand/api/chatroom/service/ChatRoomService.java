package com.team5.secondhand.api.chatroom.service;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import com.team5.secondhand.api.chatroom.exception.ExistChatRoomException;
import com.team5.secondhand.api.chatroom.repository.ChatRoomRepository;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomService {
    private final ChatRoomRepository chatRoomRepository;
    public String create(Item item, Member buyer) throws ExistChatRoomException {
        if (chatRoomRepository.findByItemAndBuyer(item, buyer).isPresent()) {
            throw new ExistChatRoomException("이미 존재하는 채팅방입니다.");
        }

        Chatroom chatRoom = Chatroom.create(item, buyer);

        return chatRoomRepository.save(chatRoom).getChatroomId();
    }

    public Optional<Chatroom> findChatroom(long memberId, long itemId) {
        return chatRoomRepository.findByBuyer_IdAndItem_Id(memberId, itemId);
    }

    public Optional<Chatroom> findByChatroomId(String chatroomId) {
        return chatRoomRepository.findByChatroomId(chatroomId);
    }
}
