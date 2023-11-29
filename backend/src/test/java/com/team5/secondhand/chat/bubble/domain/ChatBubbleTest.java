package com.team5.secondhand.chat.bubble.domain;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Slf4j
class ChatBubbleTest {

    @Nested
    @DisplayName("ChatBubble 객체 생성")
    class ready {

            @Test
            @DisplayName("ChatBubble 객체 생성")
            void createChatBubble() {
                // given
                ChatBubble chatBubble = ChatBubble.builder()
                        .chatroomId("1")
                        .sender(1L)
                        .receiver(2L)
                        .message("hello")
                        .build();

                // when
                chatBubble.ready();

                // then
                assertNotNull(chatBubble.getId());
                assertNotNull(chatBubble.getCreatedAt());
            }

            @Test
            @DisplayName("ChatBubble 객체 생성시 연속된 id 생성")
            void createChatBubbleWithContinuousId() {
                // given
                ChatBubble chatBubble1 = ChatBubble.builder()
                        .chatroomId("1")
                        .sender(1L)
                        .receiver(2L)
                        .message("hello")
                        .build();
                ChatBubble chatBubble2 = ChatBubble.builder()
                        .chatroomId("1")
                        .sender(1L)
                        .receiver(2L)
                        .message("hello")
                        .build();

                // when
                chatBubble1.ready();
                chatBubble2.ready();

                log.info("chatBubble1.getId(): {}", chatBubble1.getId());
                log.info("chatBubble2.getId(): {}", chatBubble2.getId());

                // then
                assertEquals(chatBubble1.getId() + 1, chatBubble2.getId());
            }
    }
}
