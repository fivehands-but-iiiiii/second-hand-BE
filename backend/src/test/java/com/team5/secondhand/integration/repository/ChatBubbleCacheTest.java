package com.team5.secondhand.integration.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.team5.secondhand.TestContainer;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.integration.IntegrationTest;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@IntegrationTest
class ChatBubbleCacheTest extends TestContainer {

    @Autowired
    ChatBubbleCache chatBubbleCache;

    ChatBubble chatBubble;
    String chatroomId = "test-room-01";

    @BeforeEach
    void setUp() {
        chatBubble = fixtureMonkey().giveMeBuilder(ChatBubble.class)
                .set("id", 1L)
                .set("chatroomId", chatroomId)
                .sample();
    }

    @AfterEach
    void tearDown() {
        chatBubbleCache.clear(chatroomId);
    }

    @Nested
    @DisplayName("findAllByRoomId 테스트 - ")
    class findAllByRoomId {

        @Test
        @DisplayName("성공")
        void findAllByRoomId_success() {
            // given
            chatBubbleCache.save(chatroomId, chatBubble);

            // when
            var result = chatBubbleCache.findAllByRoomId(chatroomId);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.size()).isEqualTo(1);
                softAssertions.assertThat(result.get(0).getId()).isEqualTo(chatBubble.getId());
            });
        }

        @Test
        @DisplayName("데이터 없을 시 빈 리스트 반환")
        void findAllByRoomId_success2() {
            // given

            // when
            var result = chatBubbleCache.findAllByRoomId(chatroomId);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.size()).isEqualTo(0);
            });
        }

        @Test
        @DisplayName("데이터 전체를 모두 블러올 수 있다.")
        void findAllByRoomId_success3() {
            // given
            List<ChatBubble> chatBubbles = fixtureMonkey().giveMe(ChatBubble.class, 100);
            chatBubbles.forEach(chatBubble -> chatBubbleCache.save(chatroomId, chatBubble));

            // when
            var result = chatBubbleCache.findAllByRoomId(chatroomId);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.size()).isEqualTo(100);
            });
        }
    }

    @Nested
    @DisplayName("findAllByRoomId paging 테스트 - ")
    class testFindAllByRoomId {

        @Test
        @DisplayName("페이징 수보다 적은 데이터 저장시 성공")
        void findAllByRoomId_success() {
            // given
            chatBubbleCache.save(chatroomId, chatBubble);
            List<ChatBubble> chatBubbles = fixtureMonkey().giveMe(ChatBubble.class, 10);
            chatBubbles.forEach(chatBubble -> chatBubbleCache.save(chatroomId, chatBubble));
            Pageable pageRequest = PageRequest.of(0, 25);

            // when
            var result = chatBubbleCache.findAllByRoomId(chatroomId, pageRequest);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getContent().size()).isEqualTo(11);
                softAssertions.assertThat(result.getContent().get(0).getId())
                        .isEqualTo(chatBubble.getId());
            });
        }

        @Test
        @DisplayName("페이징 수보다 많은 데이터 저장시 첫번째 페이지 조회 성공")
        void findAllByRoomId_success2() {
            // given
            chatBubbleCache.save(chatroomId, chatBubble);
            List<ChatBubble> chatBubbles = fixtureMonkey().giveMe(ChatBubble.class, 30);
            chatBubbles.forEach(chatBubble -> chatBubbleCache.save(chatroomId, chatBubble));
            Pageable pageRequest = PageRequest.of(0, 25);

            // when
            var result = chatBubbleCache.findAllByRoomId(chatroomId, pageRequest);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getContent().size()).isEqualTo(25);
                softAssertions.assertThat(result.getContent().stream()
                        .anyMatch(e -> e.getMessage().equals(chatBubble.getMessage()))).isFalse();
            });
        }

        @Test
        @DisplayName("페이징 수보다 많은 데이터 저장시 두번째 페이지 조회 성공")
        void findAllByRoomId_success3() {
            // given
            chatBubbleCache.save(chatroomId, chatBubble);
            List<ChatBubble> chatBubbles = fixtureMonkey().giveMe(ChatBubble.class, 30);
            chatBubbles.forEach(chatBubble -> chatBubbleCache.save(chatroomId, chatBubble));
            Pageable pageRequest = PageRequest.of(1, 25);

            // when
            var result = chatBubbleCache.findAllByRoomId(chatroomId, pageRequest);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getContent().size()).isEqualTo(6);
                softAssertions.assertThat(result.getContent().stream()
                        .anyMatch(e -> e.getMessage().equals(chatBubble.getMessage()))).isTrue();
            });
        }

        @Test
        @DisplayName("데이터가 없는 페이지를 조회했을 때 예외가 발생해야 한다.")
        void findAllByRoomId_fail() {
            // given
            chatBubbleCache.save(chatroomId, chatBubble);
            Pageable pageRequest = PageRequest.of(2, 25);

            // then
            assertThatThrownBy(() -> chatBubbleCache.findAllByRoomId(chatroomId, pageRequest))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

    }

    @Nested
    class getLastPage {

        @Test
        @DisplayName("성공")
        void getLastPage_success() {
            // given
            List<ChatBubble> chatBubbles = fixtureMonkey().giveMe(ChatBubble.class, 30);
            chatBubbles.forEach(chatBubble -> chatBubbleCache.save(chatroomId, chatBubble));

            // when
            var result = chatBubbleCache.getLastPage(chatroomId, 25);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isEqualTo(1);
            });
        }

    }

    @Nested
    class clear {

        @Test
        @DisplayName("성공")
        void clear_success() {
            // given
            chatBubbleCache.save(chatroomId, chatBubble);

            // when
            chatBubbleCache.clear(chatroomId);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(chatBubbleCache.findAllByRoomId(chatroomId).size())
                        .isEqualTo(0);
            });
        }
    }
}
