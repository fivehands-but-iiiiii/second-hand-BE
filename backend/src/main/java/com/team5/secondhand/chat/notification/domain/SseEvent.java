package com.team5.secondhand.chat.notification.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SseEvent {
    CHAT_NOTIFICATION("chatNotification");

    private final String camelExpression;

    @JsonValue
    public String getEvent() {
        return this.camelExpression;
    }
}
