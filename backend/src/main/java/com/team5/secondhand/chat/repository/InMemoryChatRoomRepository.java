package com.team5.secondhand.chat.repository;

import com.team5.secondhand.chat.domain.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryChatRoomRepository implements ChatRoomRepository {

    private final ConcurrentMap<String, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();

    @Override
    public List<ChatRoom> findAll() {
        return new ArrayList<>(chatRoomMap.values());
    }

    @Override
    public Optional<ChatRoom> findById(String roomId) {
        return Optional.ofNullable(chatRoomMap.get(roomId));
    }

    @Override
    public boolean save(ChatRoom chatRoom) {
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return true;
    }
}
