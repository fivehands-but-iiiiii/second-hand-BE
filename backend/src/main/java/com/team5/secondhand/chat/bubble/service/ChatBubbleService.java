package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.chat.topic.service.RedisChatPublisher;
import com.team5.secondhand.global.event.chatbubble.ChatBubbleArrivedEvent;
import com.team5.secondhand.global.properties.ConstProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatBubbleService {

    private final ConstProperties.Chat chatContext;

    private final RedisChatPublisher redisChatPublisher;
    private final ApplicationEventPublisher publisher;

    private final ChatBubbleCache bubbleCache;
    private final ChatBubbleRepository bubbleRepository;

    protected ChatBubbleService(ConstProperties properties, ChatBubbleCache bubbleCache, RedisChatPublisher redisChatPublisher, ApplicationEventPublisher publisher, ChatBubbleRepository bubbleRepository) {
        this.chatContext = properties.getChat();
        this.bubbleCache = bubbleCache;
        this.redisChatPublisher = redisChatPublisher;
        this.publisher = publisher;
        this.bubbleRepository = bubbleRepository;
    }

    @Transactional(readOnly = true)
    public Slice<ChatBubble> getChatBubbles(int page, String roomId) {
        String key = generateChatLogKey(roomId);
        Pageable pageable = PageRequest.of(page, chatContext.getPageSize(), Sort.by("createdAt").ascending());
        Slice<ChatBubble> list = bubbleCache.findAllByRoomId(key, pageable);

        if (!list.hasContent()) {
            int lastCachePage = bubbleCache.getLastPage(key, chatContext.getPageSize());
            pageable = PageRequest.of(page - lastCachePage, chatContext.getPageSize(), Sort.by("createdAt").ascending());
            return bubbleRepository.findAll(pageable);
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

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void moveDataCacheToRepository() {
        List<ChatBubble> allChatBubble = bubbleCache.findAllByRoomId(chatContext.getBucket() + "*");
        bubbleCache.clear(chatContext.getBucket() + "*");
        bubbleRepository.saveAll(allChatBubble);
    }

}
