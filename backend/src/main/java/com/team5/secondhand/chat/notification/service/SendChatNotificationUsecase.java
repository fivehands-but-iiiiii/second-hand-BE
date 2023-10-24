package com.team5.secondhand.chat.notification.service;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.notification.dto.ChatNotification;

public interface SendChatNotificationUsecase {
    void sendChatNotificationToMember(Long id, Chatroom chatroom, ChatNotification chatNotification);
}
