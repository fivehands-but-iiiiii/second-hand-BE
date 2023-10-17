package com.team5.secondhand.chat.chatroom.service;

import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetainfoCache;
import com.team5.secondhand.global.event.chatbubble.ChatBubbleArrivedEvent;
import com.team5.secondhand.global.event.chatbubble.ChatNotificationEvent;
import com.team5.secondhand.global.event.chatroom.ChatroomCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomMetaEventListener {

    private final ApplicationEventPublisher eventPublisher;
    private final ChatroomMetainfoCache metaInfoRepository;

    @Async
    @EventListener
    public void chatroomCreatedEventHandler(ChatroomCreatedEvent event) {
        Chatroom chatroom = Chatroom.init(event.getInfo());
        metaInfoRepository.saveChatroom(chatroom.getChatroomId(), chatroom);
    }

    @Async
    @EventListener
    public void chatBubbleArrivedEventHandler(ChatBubbleArrivedEvent event) throws NotChatroomMemberException {
        ChatBubble chatBubble = event.getChatBubble();
        Chatroom chatroom = getChatroom(chatBubble.getChatroomId());
        chatroom.updateLastMessage(chatBubble);
        Chatroom saveChatroom = metaInfoRepository.saveChatroom(chatroom.getChatroomId(), chatroom);
        eventPublisher.publishEvent(ChatNotificationEvent.of(saveChatroom, chatBubble));
    }

    public Chatroom getChatroom(String chatroomId) {
        return metaInfoRepository.findByChatroomId(chatroomId).orElseThrow();
    }

}
