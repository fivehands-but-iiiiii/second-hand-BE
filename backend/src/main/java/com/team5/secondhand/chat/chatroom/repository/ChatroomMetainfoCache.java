package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;

import java.util.List;
import java.util.Optional;

public interface ChatroomMetainfoCache {

    Chatroom saveChatroom(String chatroomId, Chatroom chatroom);

    Optional<Chatroom> findByChatroomId(String chatroomId);

    List<Chatroom> findAll();

    void clear();
}
