package com.team5.secondhand.chat.noti.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface NotificationRepository {
    SseEmitter save(String id, SseEmitter sseEmitter);

    void deleteAllStartByWithId(String id);

    Map<String, SseEmitter> findAllStartById(String memberId);
}
