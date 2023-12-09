package com.team5.secondhand.integration.chat.bubble.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team5.secondhand.FixtureFactory;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.integration.IntegrationTest;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ChatBubbleCacheTest extends FixtureFactory {

    String roomId = "1234";
    @Autowired
    ChatBubbleCache chatBubbleCache;

    @AfterEach
    void tearDown() {
        chatBubbleCache.findAllBubbles();
    }

    @Nested
    @DisplayName("findAllBubbles 를 시행할 때, ")
    class findAllBubbles {

        @Test
        @DisplayName("캐시에 저장된 모든 ChatBubble을 반환한다.")
        void findAllBubbles() throws JsonProcessingException {
            List<ChatBubble> chatBubbleCaches = fixtureMonkey().giveMe(ChatBubble.class,
                    100);
            chatBubbleCaches.forEach(e -> chatBubbleCache.save(roomId, e));

            List<ChatBubble> allBubbles = chatBubbleCache.findAllBubbles();
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(allBubbles.size()).isEqualTo(100);
            });
        }

        @Test
        @DisplayName("캐시에 저장된 모든 ChatBubble을 반환 후 캐시는 비워진다.")
        void findAllBubblesAndClear() throws JsonProcessingException {
            List<ChatBubble> chatBubbleCaches = fixtureMonkey().giveMe(ChatBubble.class,
                    100);
            chatBubbleCaches.forEach(e -> chatBubbleCache.save(roomId, e));

            List<ChatBubble> allBubbles = chatBubbleCache.findAllBubbles();
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(allBubbles.size()).isEqualTo(100);
            });
            List<ChatBubble> allBubbles2 = chatBubbleCache.findAllBubbles();
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(allBubbles2.size()).isEqualTo(0);
            });
        }
    }

    @Nested
    @DisplayName("getLastPage 를 시행할 때, ")
    class getLastPage {

        @Test
        @DisplayName("채팅 메시지가 저장된 마지막 페이지 숫자를 반환한다.")
        void getLastPage() {
            List<ChatBubble> chatBubbleCaches = fixtureMonkey().giveMe(ChatBubble.class,
                    100);
            chatBubbleCaches.forEach(e -> chatBubbleCache.save(roomId, e));

            int lastPage = chatBubbleCache.getLastPage(roomId, 25);
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(lastPage).isEqualTo(4);
            });
        }

        @Test
        @DisplayName("채팅 메시지가 저장된 마지막 페이지 숫자를 반환한다.")
        void getLastPage2() {
            List<ChatBubble> chatBubbleCaches = fixtureMonkey().giveMe(ChatBubble.class,
                    91);
            chatBubbleCaches.forEach(e -> chatBubbleCache.save(roomId, e));

            int lastPage = chatBubbleCache.getLastPage(roomId, 10);
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(lastPage).isEqualTo(9);
            });
        }
    }
}
