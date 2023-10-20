package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.global.properties.ConstProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatBubbleScheduledService {

    private final ChatBubbleCache bubbleCache;
    private final ChatBubbleRepository bubbleRepository;
    private final ConstProperties.Chat chatContext;

    protected ChatBubbleScheduledService(ChatBubbleCache bubbleCache, ChatBubbleRepository bubbleRepository, ConstProperties chatContext) {
        this.bubbleCache = bubbleCache;
        this.bubbleRepository = bubbleRepository;
        this.chatContext = chatContext.getChat();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void moveDataCacheToRepository() {
        List<ChatBubble> allChatBubble = bubbleCache.findAllByRoomId(chatContext.getBucket() + "*");
        bubbleCache.clear(chatContext.getBucket() + "*");
        bubbleRepository.saveAll(allChatBubble);
    }
    
}
