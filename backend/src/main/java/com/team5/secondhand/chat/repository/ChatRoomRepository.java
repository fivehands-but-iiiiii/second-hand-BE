package com.team5.secondhand.chat.repository;

import com.team5.secondhand.chat.domain.ChatRoom;

import java.util.*;

public interface ChatRoomRepository {
    List<ChatRoom> findAll();
    Optional<ChatRoom> findById(String roomId);

    boolean save(ChatRoom chatRoom);
}
