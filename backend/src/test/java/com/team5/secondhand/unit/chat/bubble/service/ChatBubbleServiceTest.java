package com.team5.secondhand.unit.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.service.ChatBubbleService;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.bubble.event.ChatBubbleArrivedEvent;
import com.team5.secondhand.global.properties.ChatConstProperties;
import com.team5.secondhand.unit.AbstractApplicationTest;
import com.team5.secondhand.unit.ApplicationTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ApplicationTest
@DisplayName("SERVICE - ChatBubbleService 테스트")
class ChatBubbleServiceTest extends AbstractApplicationTest {

    String key = "testroom-1";
    ChatBubble chatBubble;

    @InjectMocks
    ChatBubbleService chatBubbleService;
    @Mock
    protected ChatConstProperties properties; // 전체 설정


    @BeforeEach
    void clean() {
        MockitoAnnotations.openMocks(this);
        chatBubble = fixtureMonkey().giveMeBuilder(ChatBubble.class).sample();
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        when(properties.getBucket()).thenReturn("chatroom");
        when(properties.getPageSize()).thenReturn(25);
        when(chatroomMetainfoCache.findByChatroomId(any(String.class)))
                .thenReturn(Optional.of(fixtureMonkey().giveMeOne(Chatroom.class)));
        when(chatBubbleCache.save(any(String.class), eq(chatBubble)))
                .thenReturn(chatBubble);
        when(chatBubbleCache.findAllByRoomId(any(String.class)))
                .thenReturn(fixtureMonkey().giveMe(ChatBubble.class, 25));
        when(chatBubbleCache.findAllByRoomId(any(String.class), any(Pageable.class)))
                .thenReturn(new SliceImpl<>(fixtureMonkey().giveMe(ChatBubble.class, 25), PageRequest.of(0, 25), true));
    }

    @Test
    @DisplayName("채팅 메시지를 저장할 수 있다.")
    void saveChatBubble() throws Exception {
        ChatBubble chatBubble1 = chatBubbleService.saveChatBubble(chatBubble);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(chatBubble1.getChatroomId()).isEqualTo(chatBubble.getChatroomId());
        });
    }

    @Test
    @DisplayName("채팅 메시지를 페이지를 입력하여 조회할 수 있다.")
    void getChatBubbles() throws Exception {
        //given
        fixtureMonkey().giveMeBuilder(ChatBubble.class)
                .set("chatroomId", "testroom-1")
                .sampleList(100)
                .forEach(chatBubbleService::saveChatBubble);

        //when
        Slice<ChatBubble> chatBubbles = chatBubbleService.getChatBubbles(1, key);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(chatBubbles.getContent().size()).isEqualTo(25);
        });
    }

    @Test
    @DisplayName("채팅 메시지가 도착하면 캐시에 저장하고 이벤트를 발행한다.")
    void handleMessage() {
        chatBubbleService.handleMessage(chatBubble);

        verify(chatroomMetainfoCache, times(1)).findByChatroomId(any(String.class));
        verify(publisher, times(1)).publishEvent(any(ChatBubbleArrivedEvent.class));
        verify(chatBubbleCache, times(1)).save(any(String.class), any(ChatBubble.class));
        verify(redisChatPublisher, times(1)).publish(any(ChatBubble.class));
    }

//    @Test
//    @DisplayName("채팅 메시지를 캐시에서 Repository로 옮긴다.")
//    void moveDataCacheToRepository() {
//        chatBubbleService.moveDataCacheToRepository();
//
//        verify(chatBubbleCache, times(1)).findAllByRoomId(any(String.class));
//        verify(chatBubbleRepository, times(1)).saveAll(any(List.class));
//        verify(chatBubbleCache, times(1)).clear(any(String.class));
//    }

}
