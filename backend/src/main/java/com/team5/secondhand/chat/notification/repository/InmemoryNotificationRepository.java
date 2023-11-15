package com.team5.secondhand.chat.notification.repository;

import com.team5.secondhand.chat.notification.domain.SseKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//TODO Redis로 변경
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
    public void deleteAllStartByWithId(String id) {
        int regIdx = id.indexOf("_");
        String prefix = id.substring(0, regIdx+1);
        emitters.forEach((key, value) -> {
            if (key.startsWith(prefix)) {
                emitters.remove(key);
            }
        });
    }

    @Override
    public Map<SseKey, SseEmitter> findAllStartById(String id) {
        int regIdx = id.indexOf("_");
        String prefix = id.substring(0, regIdx+1);
        return emitters.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Optional<SseEmitter> findStartById(String id) {
        int regIdx = id.indexOf("_");
        String prefix = id.substring(0, regIdx+1);
        return emitters.entrySet().stream()
                .filter(e -> e.getKey().startsWith(id))
                .map(Map.Entry::getValue)
                .findAny();
    }
}
