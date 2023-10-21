package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.chat.bubble.repository.entity.ChatBubbleEntity;
import com.team5.secondhand.global.event.chatbubble.ChatBubbleArrivedEvent;
import com.team5.secondhand.global.properties.ChatConstProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBubbleService {

    private final ChatConstProperties chatContext;

    private final RedisChatPublisher redisChatPublisher;
    private final ApplicationEventPublisher publisher;

    private final ChatBubbleCache bubbleCache;
    private final ChatBubbleRepository bubbleRepository;

    @Transactional(readOnly = true)
    public Slice<ChatBubble> getChatBubbles(int page, String roomId) {
        String key = generateChatLogKey(roomId);
        Pageable pageable = PageRequest.of(page, chatContext.getPageSize(), Sort.by("createdAt").ascending());
        Slice<ChatBubble> list = bubbleCache.findAllByRoomId(key, pageable);

        if (!list.hasContent()) {
            int lastCachePage = bubbleCache.getLastPage(key, chatContext.getPageSize());
            pageable = PageRequest.of(page - lastCachePage, chatContext.getPageSize(), Sort.by("createdAt").ascending());

            Slice<ChatBubbleEntity> all = bubbleRepository.findAll(pageable);
            List<ChatBubble> collect = all.getContent().stream()
                    .map(ChatBubbleEntity::toDomain)
                    .collect(Collectors.toList());

            return new SliceImpl<>(collect.subList(0, Math.min(pageable.getPageSize(), all.getContent().size())), pageable, all.getSize() > pageable.getPageSize());
        }

        return list;
    }

    @Transactional
    public ChatBubble saveChatBubble(ChatBubble chatBubble) {
        String key = generateChatLogKey(chatBubble.getChatroomId());
        return bubbleCache.save(key, chatBubble);
    }

    private String generateChatLogKey(String roomId) {
        return String.format("%s%s:logs", chatContext.getBucket(), roomId);
    }

    @Transactional
    public void handleMessage(ChatBubble message) {
        saveChatBubble(message);
        redisChatPublisher.publish(message);
        publisher.publishEvent(new ChatBubbleArrivedEvent(message));
    }

}
