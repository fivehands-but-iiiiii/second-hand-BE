package com.team5.secondhand.integration.chat.bubble.service;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.team5.secondhand.FixtureFactory;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.global.config.ScheduledConfig;
import com.team5.secondhand.global.scheduled.ChatBubbleScheduledWorker;
import com.team5.secondhand.integration.IntegrationTest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.SoftAssertions;
import org.joda.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@IntegrationTest
class ChatBubbleScheduledWorkerTest extends FixtureFactory {

    @SpyBean
    ChatBubbleScheduledWorker chatBubbleScheduledWorker;
    @Autowired
    ChatBubbleRepository chatBubbleRepository;
    @Autowired
    ChatBubbleCache chatBubbleCache;

    String roomId = "1";

    @Nested
    @DisplayName("clearChatBubbleCache 를 시행할 때, ")
    class ClearChatBubbleCache {

        @Test
        @DisplayName("캐시를 비울 수 있다.")
        void clearChatBubbleCache() {
            // given
            List<ChatBubble> chatBubbles = fixtureMonkey().giveMeBuilder(ChatBubble.class)
                    .set("createdAt", Instant.now().toString())
                    .sampleList(10);
            chatBubbles.forEach(chatBubble -> chatBubbleCache.save(roomId, chatBubble));
            // when
            await().untilAsserted(() -> {
                chatBubbleScheduledWorker.clearChatBubbleCache();
            });
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(chatBubbleCache.findAllBubbles().size()).isEqualTo(0);
                softAssertions.assertThat(chatBubbleRepository.findAll().size())
                        .isEqualTo(chatBubbles.size());
            });
        }

        @Test
        @DisplayName("정해진 시간마다 캐시를 비울 수 있다.")
        void whenWait24Hours_thenExcute23Times() {
            await()
                .atMost(3, TimeUnit.MINUTES)
                .untilAsserted(() -> verify(chatBubbleScheduledWorker, atLeast(2)).clearChatBubbleCache());
        }
    }
}
