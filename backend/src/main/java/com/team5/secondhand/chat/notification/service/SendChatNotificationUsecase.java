package com.team5.secondhand.chat.notification.service;

import com.team5.secondhand.chat.notification.dto.ChatNotification;

public interface SendChatNotificationUsecase {
    void sendChatNotificationToMember(String id, ChatNotification chatNotification);
}
