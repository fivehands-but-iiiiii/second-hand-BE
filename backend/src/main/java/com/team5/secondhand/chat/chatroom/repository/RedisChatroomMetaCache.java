package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisChatroomMetaCache implements ChatroomMetainfoCache {

    private final RedisUtil redisUtil;
    private final String MAIN_KEY = "meta:chatroom";

    @Override
    public Chatroom saveChatroom(String chatroomId, Chatroom chatroom) {
        redisUtil.put(generateKey(chatroomId), chatroom, null);
        return chatroom;
    }

    @Override
    public Optional<Chatroom> findByChatroomId(String chatroomId) {
        Optional<Chatroom> chatroom = Optional.ofNullable(redisUtil.get(generateKey(chatroomId), Chatroom.class));
        return chatroom;
    }

    private String generateKey(String chatroomId) {
        return String.format("%s:%s", MAIN_KEY, chatroomId);
    }
}
