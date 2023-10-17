package com.team5.secondhand.unit.api.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.unit.api.AbstractApplicationTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DisplayName("REPOSITORY - ChatBubbleRequestCache 테스트")
class ChatBubbleRequestCacheTest extends AbstractApplicationTest {

    ChatBubble chatBubble;
    String key;

    @BeforeEach
    void init() {
        chatBubble = fixtureMonkey().giveMeBuilder(ChatBubble.class).sample();
        key = "test:chatBubble1";
        chatBubbleCache.clear(key);
    }


    @Test
    @DisplayName("캐시에 저장할 수 있다.")
    void save() {
        Slice<ChatBubble> all = chatBubbleCache.findAllByRoomId(key, PageRequest.of(0, 200));

        ChatBubble saved = chatBubbleCache.save(key, chatBubble);

        Slice<ChatBubble> newAll = chatBubbleCache.findAllByRoomId(key, PageRequest.of(0, 200));
        Assertions.assertThat(newAll.getContent()).hasSize(all.getContent().size() + 1);
    }

    @Test
    @DisplayName("캐시에 저장된 데이터를 조회할 수 있다.")
    void findAllByRoomId() {

        for (int i = 0; i < 30; i++) {
            chatBubbleCache.save(key, fixtureMonkey().giveMeOne(ChatBubble.class));
        }

        Slice<ChatBubble> allByRoomId = chatBubbleCache.findAllByRoomId(key, PageRequest.of(0, 10));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(allByRoomId.getSize()).isEqualTo(10);
            softAssertions.assertThat(allByRoomId.getContent().get(0).getId()).isNotNull();
            softAssertions.assertThat(allByRoomId.hasNext()).isTrue();
        });
    }

    @Test
    @DisplayName("캐시에 다음 페이지 여부를 조회할 수 있다.")
    void findAllByRoomId_lastpage() {

        for (int i = 0; i < 25; i++) {
            chatBubbleCache.save(key, fixtureMonkey().giveMeOne(ChatBubble.class));
        }

        Slice<ChatBubble> allByRoomId = chatBubbleCache.findAllByRoomId(key, PageRequest.of(2, 10));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(allByRoomId.getContent()).hasSize(5);
            softAssertions.assertThat(allByRoomId.hasNext()).isFalse();
        });
    }
}
