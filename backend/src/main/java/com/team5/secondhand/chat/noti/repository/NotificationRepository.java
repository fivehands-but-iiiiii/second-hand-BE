package com.team5.secondhand.chat.noti.repository;

import com.team5.secondhand.chat.noti.domain.SseId;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;

public interface NotificationRepository {
    SseEmitter save(SseId id, SseEmitter sseEmitter);

    void deleteAllStartByWithId(String id);

    Map<SseId, SseEmitter> findAllStartById(String id);

    Optional<SseEmitter> findStartById(String id);
}
