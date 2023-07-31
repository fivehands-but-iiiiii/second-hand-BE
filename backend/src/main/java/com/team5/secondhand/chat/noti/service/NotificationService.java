package com.team5.secondhand.chat.noti.service;

import com.team5.secondhand.chat.noti.domain.Event;
import com.team5.secondhand.chat.noti.domain.SseId;
import com.team5.secondhand.chat.noti.dto.ChatNotification;
import com.team5.secondhand.chat.noti.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements SendChatNotificationUsecase {
    private final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String id, String lastEventId, HttpServletResponse response) {
        SseId sseId = SseId.of(id);

        SseEmitter emitter = notificationRepository.save(sseId, new SseEmitter(DEFAULT_TIMEOUT));
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Transfer-Encoding", "chunked"); //본문 크기를 미리 알 수 없음

        emitter.onCompletion(() -> {
            log.info("SSE onCompletion");
            notificationRepository.deleteAllStartByWithId(id);
        });
        emitter.onTimeout(() -> {
            log.info("SSE onTimeout");
            notificationRepository.deleteAllStartByWithId(id);
            emitter.complete();
        });
        emitter.onError(e -> {
            log.info("SSE error : {}", e.getMessage());
            notificationRepository.deleteAllStartByWithId(id);
        });

        sendToClient(emitter, id, String.format("connected successfully member key : %s", id));

        if (!lastEventId.isEmpty()) {
            Map<SseId, SseEmitter> events = notificationRepository.findAllStartById(id);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey().getMemberId()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey().getId(), entry.getValue()));
        }

        return emitter;
    }

    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(Event.CHAT_NOTIFICATION.getEvent())
                    .data(data));
        } catch (IOException e) {
            log.error("SSE 전송 오류", e);
            notificationRepository.deleteAllStartByWithId(id);
        }
    }

    @Override
    public void sendChatNotificationToMember(String id, ChatNotification chatNotification) {
        SseEmitter sseEmitter = notificationRepository.findStartById(id).orElseThrow(); //TODO 에러 작성해주기
        sendToClient(sseEmitter, id, chatNotification);
    }
}
