package com.team5.secondhand.global.scheduled;

import static com.querydsl.core.group.GroupBy.groupBy;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.chat.bubble.repository.entity.BubbleEntity;
import com.team5.secondhand.global.properties.ChatCacheProperties;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBubbleScheduledWorker {

    private final ChatBubbleCache chatBubbleCache;
    private final ChatBubbleRepository chatBubbleRepository;

    @Scheduled(cron = "0 * * * * *") // 매 1분마다
    public void clearChatBubbleCache() throws JsonProcessingException {
        log.info("🧹START : {} Thread clear chat bubble cache", Thread.currentThread().getId());
        List<ChatBubble> all = chatBubbleCache.findAllBubbles();

        if (all.isEmpty()) {
            return;
        }

        List<BubbleEntity> entities = all.stream().map(BubbleEntity::from)
                .collect(toList());
        chatBubbleRepository.saveAll(entities);
        log.info("🧹 END : clear chat bubble cache = {}", entities.size());
    }
}
