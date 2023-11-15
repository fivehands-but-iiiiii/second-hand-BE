package com.team5.secondhand.chat.notification.service;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.notification.domain.SseEvent;
import com.team5.secondhand.chat.notification.domain.SseKey;
import com.team5.secondhand.chat.notification.dto.ChatNotification;
import com.team5.secondhand.chat.notification.repository.NotificationRepository;
import com.team5.secondhand.chat.bubble.event.ChatNotificationEvent;
import com.team5.secondhand.global.properties.ChatNotificationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements SendChatNotificationUsecase {

    private final ChatNotificationProperties notificationProperties;
    private final NotificationRepository notificationRepository;

    @Transactional
    public SseEmitter subscribe(Long id, String lastEventId, HttpServletResponse response) {
        SseKey sseId = SseKey.of(id);

        SseEmitter emitter = notificationRepository.save(sseId, new SseEmitter(notificationProperties.getTimeOut()));
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Last-Event-ID", sseId.getKey());

        emitter.onCompletion(() -> onStatusCallback("SSE onCompletion", id));
        emitter.onTimeout(() -> {
            onStatusCallback("SSE onTimeout", id);
            emitter.complete();
        });
        emitter.onError(e -> onStatusCallback("SSE onError = " + e.getMessage(), id));

        sendToClient(emitter, id, String.format("connected successfully member key : %s", id));

        if (!lastEventId.isEmpty()) {
            Map<SseKey, SseEmitter> events = notificationRepository.findAllStartById(id+"_");
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey().getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey().getMemberId(), entry.getValue())); //클라이언트가 연결을 끊기 전까지 받지 못한 새로운 이벤트를 보내준다.
        }

        return emitter;
    }

    public void onStatusCallback(String message, Long id) {
        log.info(message);
        notificationRepository.deleteAllStartByWithId(id +"_");
    }

    private void sendToClient(SseEmitter emitter, Long id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(id))
                    .name(SseEvent.CHAT_NOTIFICATION.getEvent())
                    .data(data));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public void sendChatNotificationToMember(Long id, Chatroom chatroom, ChatNotification chatNotification) {
        try {
            SseEmitter sseEmitter = notificationRepository.findStartById(id+"_").orElseThrow(() -> new NoSuchElementException("상대방이 접속중이 아닙니다."));
            if (chatroom.hasPaticipant(id)) {
                sendToClient(sseEmitter, id, chatNotification);
            }
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
        }

    }

    //TODO Transaction 관련된 문제가 나지는 않을까?
    @Async
    @EventListener
    public void getChatBubble (ChatNotificationEvent event) {
        Long receiverId = event.getChatBubble().getReceiver();
        sendChatNotificationToMember(receiverId, event.getChatroom(), ChatNotification.of(event.getChatBubble(), event.getChatroom()));
    }

}
