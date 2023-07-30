package com.team5.secondhand.chat.noti.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Event {
    CHAT_NOTIFICATION("chatNotification");

    private final String camelExpression;

    @JsonValue
    public String getEvent() {
        return this.camelExpression;
    }
}
