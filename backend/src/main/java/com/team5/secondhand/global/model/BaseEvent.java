package com.team5.secondhand.global.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BaseEvent {
    private Instant createdAt;

    protected BaseEvent() {
        this.createdAt = Instant.now();
    }
}
