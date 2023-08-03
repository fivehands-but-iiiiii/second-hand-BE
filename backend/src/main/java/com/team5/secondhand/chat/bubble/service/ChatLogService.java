package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.global.event.ChatBubbleArrivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatLogService {
    @Value("${const.chat.bucket}")
    private String chatBucketPrefix;
    @Value("${const.chat.page-size}")
    private int chatLoadSize;

    private final RedisTemplate<String, ChatBubble> redisChatBubbleTemplate;
    
    @Transactional(readOnly = true)
    public Slice<ChatBubble> getChatBubbles(int page, String roomId) {
        ListOperations<String, ChatBubble> listOperations = redisChatBubbleTemplate.opsForList();

        String key = chatBucketPrefix + roomId;
        long startIndex = getStartIndex(page);
        long endIndex = startIndex - chatLoadSize;

        List<ChatBubble> messages = listOperations.range(key, endIndex, startIndex);
        Pageable pageable = PageRequest.ofSize(chatLoadSize);
        return getSlice(messages, pageable);
    }

    private long getStartIndex(int page) {
        return (-1L * chatLoadSize * page) - 1;
    }

    private Slice<ChatBubble> getSlice(List<ChatBubble> messages, Pageable pageable) {
        boolean hasNext = false;

        if (messages.size() > pageable.getPageSize()) {
            hasNext = true;
            messages.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(messages, pageable, hasNext);
    }

    @Transactional(readOnly = true)
    public void saveChatBubble(ChatBubble chatBubble) {
        String key = chatBucketPrefix + chatBubble.getRoomId();
        redisChatBubbleTemplate.opsForList().rightPush(key, chatBubble);
    }

    @Async
    @EventListener
    public void getChatBubble(ChatBubbleArrivedEvent chatBubbleArrivedEvent) {
        ChatBubble chatBubble = chatBubbleArrivedEvent.getChatBubble();
        saveChatBubble(chatBubble);
    }
}
