package com.team5.secondhand.chat.notification.repository;

import com.team5.secondhand.chat.notification.domain.SseKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InmemoryNotificationRepository implements NotificationRepository {
    private final Map<SseKey, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(SseKey id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void deleteAllStartByWithId(long id) {
        emitters.forEach((key, value) -> {
            if (key.startsWith(id)) {
                emitters.remove(key);
            }
        });
    }

    @Override
    public Map<SseKey, SseEmitter> findAllStartById(long id) {
        return emitters.entrySet().stream()
                .filter(e -> e.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Optional<SseEmitter> findStartById(long id) {
        return emitters.entrySet().stream()
                .filter(e -> e.getKey().startsWith(id))
                .map(Map.Entry::getValue)
                .findAny();
    }
}
