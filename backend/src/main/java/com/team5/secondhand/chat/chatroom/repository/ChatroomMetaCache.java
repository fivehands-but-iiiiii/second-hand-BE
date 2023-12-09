package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.global.properties.ChatMetaInfoProperties;
import com.team5.secondhand.global.util.RedisOperationsHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatroomMetaCache {
    private final RedisOperationsHelper operationsHelper;
    private final ChatMetaInfoProperties chatMetaInfoProperties;

    public Chatroom saveChatroom(String chatroomId, Chatroom chatroom) {
        operationsHelper.put(generateKey(chatroomId), chatroom, null);
        return chatroom;
    }

    public Optional<Chatroom> findByChatroomId(String chatroomId) {
        Optional<Chatroom> chatroom = operationsHelper.get(generateKey(chatroomId), Chatroom.class);
        return chatroom;
    }

    public List<Chatroom> findAll() {
        return operationsHelper.findAll(generateKey("*"), Chatroom.class);
    }

    public void clear() {
        operationsHelper.delete(generateKey("*"));
    }

    private String generateKey(String chatroomId) {
        return String.format("%s:%s", chatMetaInfoProperties.getMetaInfoKey(), chatroomId);
    }
}
