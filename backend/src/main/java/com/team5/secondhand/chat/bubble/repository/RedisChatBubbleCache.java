package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisChatBubbleCache implements ChatBubbleCache {

    private final RedisTemplate<String, ChatBubble> redisChatBubbleTemplate;

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
        ListOperations<String, ChatBubble> listOperations = redisChatBubbleTemplate.opsForList();

        long startIndex = getStartIndex(pageable);
        long endIndex = startIndex - pageable.getPageSize();

        List<ChatBubble> messages = listOperations.range(key, endIndex - 1, startIndex);
        return getSlice(messages, pageable);
    }

    @Override
    public List<ChatBubble> findAllByRoomId(String key) {
        long size = redisChatBubbleTemplate.opsForList().size(key);
        return redisChatBubbleTemplate.opsForList().leftPop(key, size);
    }

    @Override
    public ChatBubble save(String key, ChatBubble chatBubble) {
        redisChatBubbleTemplate.opsForList().rightPush(key, chatBubble);
        return chatBubble;
    }

    @Override
    public int getLastPage(String key, int pageSize) {
        Long size = redisChatBubbleTemplate.opsForList().size(key);
        return (int)(size/pageSize);
    }

    @Override
    public void clear(String key) {
        redisChatBubbleTemplate.opsForList().getOperations().delete(key);
    }
}
