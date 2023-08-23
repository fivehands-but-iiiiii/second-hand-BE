package com.team5.secondhand.chat.chatroom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.repository.ChatroomCacheRepository;
import com.team5.secondhand.global.event.chatbubble.ChatBubbleArrivedEvent;
import com.team5.secondhand.global.event.chatroom.ChatroomCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomCacheService {
    private final HashOperations<String, String, Chatroom> chatroomHashOperations;
    private final ObjectMapper objectMapper;
    private final String MAIN_KEY = "chatroom";
    private final ChatroomCacheRepository chatroomCacheRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void enterToChatRoom(String roomId, String memberId) {
        Chatroom chatroom = chatroomCacheRepository.findByChatroomId(roomId).orElseThrow();
        chatroom.enter(memberId);
        chatroomCacheRepository.saveChatroom(roomId, chatroom);
    }

    public void exitToChatRoom(String roomId, String memberId) {
        Chatroom chatroom = chatroomCacheRepository.findByChatroomId(roomId).orElseThrow();
        chatroom.exit(memberId);
    }

    public Chatroom getChatroom(String chatroomId) {
        return chatroomCacheRepository.findByChatroomId(chatroomId).orElseThrow();
    }

    @EventListener
    private void chatroomCreatedEventHandler(ChatroomCreatedEvent event) {
        ChatroomInfo info = event.getInfo();
        Chatroom chatroom = Chatroom.init(info);
        chatroomCacheRepository.saveChatroom(chatroom.getChatroomId(), chatroom);
    }

    @EventListener
    private void chatBubbleArrivedEventHandler(ChatBubbleArrivedEvent event) throws NotChatroomMemberException {
        ChatBubble chatBubble = event.getChatBubble();
        Chatroom chatroom = getChatroom(chatBubble.getRoomId());
        chatroom.updateLastMessage(chatBubble);
        Chatroom saveChatroom = chatroomCacheRepository.saveChatroom(chatBubble.getRoomId(), chatroom);
        eventPublisher.publishEvent(ChatNotificationEvent.of(chatBubble, chatroom));
    }
}
