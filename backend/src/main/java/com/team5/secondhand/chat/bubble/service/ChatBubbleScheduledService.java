package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.chat.bubble.repository.entity.ChatBubbleEntity;
import com.team5.secondhand.global.properties.ChatConstProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBubbleScheduledService {

    private final ChatBubbleCache bubbleCache;
    private final ChatBubbleRepository bubbleRepository;
    private final ChatConstProperties chatContext;

    @Scheduled(cron = "0 0 * * * *")
    public void moveDataCacheToRepository() {
        List<ChatBubble> allChatBubble = bubbleCache.findAllByRoomId(chatContext.getBucket() + "*");
        bubbleCache.clear(chatContext.getBucket() + "*");

        List<ChatBubbleEntity> collect = allChatBubble.stream()
                .map(ChatBubbleEntity::fromDomain)
                .collect(Collectors.toList());

        bubbleRepository.saveAll(collect);
    }
    
}
