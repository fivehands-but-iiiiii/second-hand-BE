package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class ChatroomCacheRepository implements ChatroomMetainfoCustomRepository {
    private final RedisTemplate<String, Chatroom> redisChatroomTemplate;
    private final String MAIN_KEY = "chatroom";

    public Chatroom saveChatroom(String chatroomId, Chatroom chatroom) {
        redisChatroomTemplate.opsForValue().set(generateKey(chatroomId), chatroom);
        return redisChatroomTemplate.opsForValue().get(generateKey((chatroomId)));
    }

    public Optional<Chatroom> findByChatroomId(String chatroomId) {
       Chatroom chatroom = redisChatroomTemplate.opsForValue().get(generateKey((chatroomId)));
        return Optional.ofNullable(chatroom);
    }

    private String generateKey(String chatroomId) {
        return String.format("%s:%s", MAIN_KEY, chatroomId);
    }
}
