package com.team5.secondhand.chat.notification.service;

import com.team5.secondhand.chat.bubble.event.ChatNotificationEvent;
import com.team5.secondhand.chat.notification.dto.ChatNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationEventListener {

    private final SendChatNotificationUsecase sendChatNotificationUsecase;

    @Async
    @EventListener
    public void getChatBubble(ChatNotificationEvent event) {
        Long receiverId = event.getChatBubble().getReceiver();
        sendChatNotificationUsecase.sendChatNotificationToMember(receiverId, event.getChatroom(),
                ChatNotification.of(event.getChatBubble(), event.getChatroom()));
    }

}
