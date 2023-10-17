package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisChatroomMetaCache implements ChatroomMetainfoCache {
    private final RedisTemplate<String, Chatroom> redisChatroomTemplate;
    private final String MAIN_KEY = "chatroom";

    @Override
    public Chatroom saveChatroom(String chatroomId, Chatroom chatroom) {
        redisChatroomTemplate.opsForValue().set(generateKey(chatroomId), chatroom);
        return redisChatroomTemplate.opsForValue().get(generateKey((chatroomId)));
    }

    @Override
    public Optional<Chatroom> findByChatroomId(String chatroomId) {
       Chatroom chatroom = redisChatroomTemplate.opsForValue().get(generateKey((chatroomId)));
        return Optional.ofNullable(chatroom);
    }

    private String generateKey(String chatroomId) {
        return String.format("%s:%s", MAIN_KEY, chatroomId);
    }
}
