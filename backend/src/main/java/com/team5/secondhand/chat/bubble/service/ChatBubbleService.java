package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.global.event.chatbubble.ChatBubbleArrivedEvent;
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
public class ChatBubbleService {
    @Value("${const.chat.bucket}")
    private String chatBucketPrefix;
    @Value("${const.chat.page-size}")
    private int chatLoadSize;

    private final RedisTemplate<String, ChatBubble> redisChatBubbleTemplate;

    //TODO Repository 로직 Repository 로 분리하기
    @Transactional(readOnly = true)
    public Slice<ChatBubble> getChatBubbles(int page, String roomId) {
        ListOperations<String, ChatBubble> listOperations = redisChatBubbleTemplate.opsForList();

        String key = generateChatLogKey(roomId);
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
        String key = generateChatLogKey(chatBubble.getRoomId());
        redisChatBubbleTemplate.opsForList().rightPush(key, chatBubble);
    }

    @Async
    @EventListener
    @Transactional
    public void getChatBubble(ChatBubbleArrivedEvent chatBubbleArrivedEvent) {
        ChatBubble chatBubble = chatBubbleArrivedEvent.getChatBubble();
        saveChatBubble(chatBubble);
    }

    private String generateChatLogKey (String roomId) {
        return String.format("%s%s:logs", chatBucketPrefix, roomId);
    }
}
