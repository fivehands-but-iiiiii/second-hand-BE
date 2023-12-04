package com.team5.secondhand.chat.notification.repository;

import com.team5.secondhand.chat.notification.domain.SseKey;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;

public interface NotificationRepository {
    SseEmitter save(SseKey id, SseEmitter sseEmitter);
    void deleteAllStartByWithId(long id);
    Map<SseKey, SseEmitter> findAllStartById(long id);
    Optional<SseEmitter> findStartById(long id);
    void deleteById(SseKey sseId);
}
