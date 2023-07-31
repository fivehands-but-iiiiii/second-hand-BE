package com.team5.secondhand.chat.noti.service;

import com.team5.secondhand.chat.noti.dto.ChatNotification;

public interface SendChatNotificationUsecase {
    void sendChatNotificationToMember(String id, ChatNotification chatNotification);
}
