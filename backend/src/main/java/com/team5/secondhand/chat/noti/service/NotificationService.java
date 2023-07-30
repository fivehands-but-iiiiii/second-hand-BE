package com.team5.secondhand.chat.noti.service;

import com.team5.secondhand.chat.noti.domain.Event;
import com.team5.secondhand.chat.noti.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long id, String lastEventId, HttpServletResponse response) {
        String memberId = String.format("%d_%d", id, System.currentTimeMillis());

        SseEmitter emitter = notificationRepository.save(memberId, new SseEmitter(DEFAULT_TIMEOUT));
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Transfer-Encoding", "chunked"); //본문 크기를 미리 알 수 없음

        emitter.onCompletion(() -> {
            log.info("SSE onCompletion");
            notificationRepository.deleteAllStartByWithId(memberId);
        });
        emitter.onTimeout(() -> {
            log.info("SSE onTimeout");
            notificationRepository.deleteAllStartByWithId(memberId);
            emitter.complete();
        });
        emitter.onError(e -> {
            log.info("SSE error : {}", e.getMessage());
            notificationRepository.deleteAllStartByWithId(memberId);
        });

        sendToClient(emitter, memberId, String.format("connected successfully member key : %d", id));

        if (!lastEventId.isEmpty()) {
            Map<String, SseEmitter> events = notificationRepository.findAllStartById(memberId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(Event.CHAT_NOTIFICATION.getEvent())
                    .data(data.toString()));
        } catch (IOException e) {
            log.error("SSE 전송 오류", e);
            notificationRepository.deleteAllStartByWithId(id);
        }
    }
}
