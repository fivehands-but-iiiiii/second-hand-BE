package com.team5.secondhand.chat.service;

import com.team5.secondhand.chat.domain.ChatRoom;
import com.team5.secondhand.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRepository;

    public List<ChatRoom> findAll() {
        return chatRepository.findAll();
    }

    public ChatRoom findByRoomId(String roomId) {
        return chatRepository.findById(roomId).orElseThrow(() -> new NoSuchElementException("해당하는 채팅방이 없습니다."));
    }

    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRepository.save(chatRoom);
        return chatRoom;
    }
}
