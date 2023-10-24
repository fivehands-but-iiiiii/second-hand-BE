package com.team5.secondhand.chat.notification.service;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.notification.domain.SseEvent;
import com.team5.secondhand.chat.notification.domain.SseKey;
import com.team5.secondhand.chat.notification.dto.ChatNotification;
import com.team5.secondhand.chat.notification.repository.NotificationRepository;
import com.team5.secondhand.global.event.chatbubble.ChatNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements SendChatNotificationUsecase {

    private final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;
    private final NotificationRepository notificationRepository;

    @Transactional
    public SseEmitter subscribe(Long id, String lastEventId, HttpServletResponse response) {
        SseKey sseId = SseKey.of(id);

        SseEmitter emitter = notificationRepository.save(sseId, new SseEmitter(DEFAULT_TIMEOUT));
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Last-Event-ID", sseId.getKey());

        emitter.onCompletion(() -> {
            log.info("SSE onCompletion");
            notificationRepository.deleteAllStartByWithId(id+"_");
        });
        emitter.onTimeout(() -> {
            log.info("SSE onTimeout");
            notificationRepository.deleteAllStartByWithId(id+"_");
            emitter.complete();
        });
        emitter.onError(e -> {
            log.info("SSE error : {}", e.getMessage());
            notificationRepository.deleteAllStartByWithId(id+"_");
        });

        sendToClient(emitter, id, String.format("connected successfully member key : %s", id));

        if (!lastEventId.isEmpty()) {
            Map<SseKey, SseEmitter> events = notificationRepository.findAllStartById(id+"_");
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey().getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey().getMemberId(), entry.getValue())); //클라이언트가 연결을 끊기 전까지 받지 못한 새로운 이벤트를 보내준다.
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, Long id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(id))
                    .name(SseEvent.CHAT_NOTIFICATION.getEvent())
                    .data(data));
        } catch (IOException e) {
            log.info("상대방이 접속중이 아닙니다.");
        }
    }

    @Override
    public void sendChatNotificationToMember(Long id, Chatroom chatroom, ChatNotification chatNotification) {
        try {
            SseEmitter sseEmitter = notificationRepository.findStartById(id+"_").get(); //TODO 에러 작성해주기
            if (!chatroom.hasPaticipant(id)) {
                sendToClient(sseEmitter, id, chatNotification);
            }
        } catch (NoSuchElementException e) {
            log.info("상대방이 접속중이 아닙니다.");
        }

    }

    //TODO Transaction 관련된 문제가 나지는 않을까?
    @Async
    @TransactionalEventListener
    public void getChatBubble (ChatNotificationEvent event) {
        Long receiverId = event.getChatBubble().getReceiver();
        //TODO 유효성 검증이 필요
        //TODO 현재 채팅방에 존재하는 멤버(1인 이상)에게 알람을 보내야 한다.
        //TODO 현재 채팅방을 구독중(websocket 통신중인) 멤버에게는 보내지 않아야 한다.
        sendChatNotificationToMember(receiverId, event.getChatroom(), ChatNotification.of(event.getChatBubble(), event.getChatroom()));
    }

}
