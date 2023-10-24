package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisChatBubbleCache implements ChatBubbleCache {

    private final RedisUtil redisUtil;
    private long getStartIndex(Pageable page) {
        return (-1L * page.getPageSize() * page.getPageNumber()) - 1;
    }

    private Slice<ChatBubble> getSlice(List<ChatBubble> messages, Pageable pageable) {

        if (messages.size() > pageable.getPageSize()) {
            messages.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(messages.subList(0, Math.min(pageable.getPageSize(), messages.size())), pageable, pageable.getPageSize() < messages.size());
    }

    @Override
    public Slice<ChatBubble> findAllByRoomId(String key, Pageable pageable) {

        long startIndex = getStartIndex(pageable);
        long endIndex = startIndex - pageable.getPageSize();

        List<ChatBubble> messages = redisUtil.getList(key, endIndex - 1, startIndex, ChatBubble.class);
        return getSlice(messages, pageable);
    }

    @Override
    public List<ChatBubble> findAllByRoomId(String key) {
        long size = redisUtil.size(key);
        return redisUtil.getList(key, 0, size, ChatBubble.class);
    }

    @Override
    public ChatBubble save(String key, ChatBubble chatBubble) {
        redisUtil.putToList(key, chatBubble);
        return chatBubble;
    }

    @Override
    public int getLastPage(String key, int pageSize) {
        Long size = redisUtil.size(key);
        return (int) (size / pageSize);
    }

    @Override
    public void clear(String key) {
        redisUtil.delete(key);
    }
}
