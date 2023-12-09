package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.chat.bubble.repository.entity.BubbleEntity;
import com.team5.secondhand.chat.bubble.event.ChatBubbleArrivedEvent;
import com.team5.secondhand.global.properties.ChatCacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatBubbleService {
    private final ChatBubbleRepository bubbleRepository;
    private final ChatBubbleCache bubbleCache;
    private final ChatCacheProperties chatBubbleProperties;

    @Transactional(readOnly = true)
    public Slice<ChatBubble> getChatBubbles(int page, String roomId) {
        String key = generateChatLogKey(roomId);
        Pageable pageable = PageRequest.of(page, chatBubbleProperties.getPageSize(), Sort.by("createdAt").descending());
        try {
            return bubbleCache.findAllByRoomId(key, pageable);
        } catch (IndexOutOfBoundsException e) {
            Slice<BubbleEntity> list = bubbleRepository.findAllByChatroomId(roomId, pageable);
            return list.map(BubbleEntity::toDomain);
        }
    }

    @Transactional
    public ChatBubble saveChatBubble(ChatBubble chatBubble) {
        String key = generateChatLogKey(chatBubble.getChatroomId());
        return bubbleCache.save(key, chatBubble);
    }

    private String generateChatLogKey (String roomId) {
        return String.format("%s%s", chatBubbleProperties.getKey(), roomId);
    }
}
