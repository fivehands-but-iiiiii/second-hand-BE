package com.team5.secondhand.chat.chatroom.service;

import com.team5.secondhand.application.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.application.chatroom.dto.response.ChatLog;
import com.team5.secondhand.application.chatroom.dto.response.ChatroomSummary;
import com.team5.secondhand.application.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetaCache;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetaRepository;
import com.team5.secondhand.chat.chatroom.repository.entity.ChatroomMetaInfoEntity;
import com.team5.secondhand.chat.bubble.event.ChatBubbleArrivedEvent;
import com.team5.secondhand.chat.bubble.event.ChatNotificationEvent;
import com.team5.secondhand.chat.chatroom.event.ChatroomCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomCacheService {
    private final ChatroomMetaCache chatroomMetaCache;
    private final ChatroomMetaRepository chatroomMetaRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void enterToChatRoom(String roomId, Long memberId) {
        // 1. 캐시에서 채팅방을 찾는다.
        Chatroom chatroom = chatroomMetaCache.findByChatroomId(roomId).orElseThrow();
        // 2.
        if (chatroom==null) {
            ChatroomMetaInfoEntity chatroomEntity = chatroomMetaRepository.findByChatroomId(roomId).orElseThrow();
            chatroom = chatroomEntity.toDomain();
        }
        // 3.
        chatroom.enter(memberId);
        chatroomMetaCache.saveChatroom(roomId, chatroom);
        // 시간 지나면 한꺼번에 처리하도록 함
        chatroomMetaRepository.save(ChatroomMetaInfoEntity.fromDomain(chatroom));
    }

    // TODO
    public void exitToChatRoom(String roomId, Long memberId) {
        ChatroomMetaInfoEntity chatroom = chatroomMetaRepository.findByChatroomId(roomId).orElseThrow();
        chatroom.toDomain().exit(memberId);
    }

    public ChatroomMetaInfoEntity getChatroom(String chatroomId) {
        return chatroomMetaRepository.findByChatroomId(chatroomId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public ChatLog getMessageInfo(String roomId, Long memberId) {
        ChatroomMetaInfoEntity chatroom = chatroomMetaRepository.findByChatroomId(roomId).orElseGet(() -> ChatroomMetaInfoEntity.create(roomId, memberId));
        return ChatLog.of(chatroom.toDomain(), memberId);
    }

    @Transactional(readOnly = true)
    public List<ChatroomSummary> addLastMessage(List<ChatroomSummary> chatroomSummaries, Long memberId) {

        return chatroomSummaries.stream()
                .map(s -> s.addChatLogs(getMessageInfo(s.getChatroomId(), memberId)))
                .collect(Collectors.toList());
    }

    @Async
    @EventListener
    public void chatroomCreatedEventHandler(ChatroomCreatedEvent event) {
        ChatroomInfo info = event.getInfo();
        Chatroom chatroom = Chatroom.init(info);
        chatroomMetaRepository.save(ChatroomMetaInfoEntity.fromDomain(chatroom));
    }

    @Async
    @EventListener
    public void chatBubbleArrivedEventHandler(ChatBubbleArrivedEvent event) throws NotChatroomMemberException {
        ChatBubble chatBubble = event.getChatBubble();
        Chatroom chatroom = getChatroom(chatBubble.getChatroomId()).toDomain();
        chatroom.updateLastMessage(chatBubble);
        ChatroomMetaInfoEntity saveChatroom = chatroomMetaRepository.save(ChatroomMetaInfoEntity.fromDomain(chatroom));
        eventPublisher.publishEvent(ChatNotificationEvent.of(saveChatroom.toDomain(), chatBubble));
    }
}
