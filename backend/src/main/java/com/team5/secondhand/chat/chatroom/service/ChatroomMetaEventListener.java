package com.team5.secondhand.chat.chatroom.service;

import com.team5.secondhand.application.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.event.EnterChatRoomEvent;
import com.team5.secondhand.chat.chatroom.event.ExitChatRoomEvent;
import com.team5.secondhand.chat.bubble.event.ChatBubbleArrivedEvent;
import com.team5.secondhand.chat.notification.event.ChatNotificationEvent;
import com.team5.secondhand.chat.chatroom.event.ChatroomCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Service
@RequiredArgsConstructor
public class ChatroomMetaEventListener {

    private final ApplicationEventPublisher eventPublisher;
    private final ChatroomMetaInfoService chatroomMetaInfoService;

    @Async
    @EventListener
    public void chatroomCreatedEventHandler(ChatroomCreatedEvent event) {
        Chatroom chatroom = Chatroom.init(event.getInfo());
        chatroomMetaInfoService.saveChatroom(chatroom);
    }

    @Async
    @EventListener
    public void chatBubbleArrivedEventHandler(ChatBubbleArrivedEvent event) throws NotChatroomMemberException {
        ChatBubble chatBubble = event.getChatBubble();
        Chatroom saveChatroom = chatroomMetaInfoService.saveBubbleToChatroom(chatBubble);
        eventPublisher.publishEvent(ChatNotificationEvent.of(saveChatroom, chatBubble));
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT, fallbackExecution = true)
    public void enterToChatroom(EnterChatRoomEvent event) {
        chatroomMetaInfoService.enterToChatRoom(event.getChatroomId(), event.getMemberId());
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT, fallbackExecution = true)
    public void exitToChatroom(ExitChatRoomEvent event) {
        chatroomMetaInfoService.exitToChatRoom(event.getChatroomId(), event.getMemberId());
    }

}
