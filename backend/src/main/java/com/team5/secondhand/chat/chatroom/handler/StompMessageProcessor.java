package com.team5.secondhand.chat.chatroom.handler;

import com.team5.secondhand.chat.chatroom.event.EnterChatRoomEvent;
import com.team5.secondhand.chat.chatroom.event.ExitChatRoomEvent;
import com.team5.secondhand.chat.exception.ErrorType;
import com.team5.secondhand.chat.notification.service.SessionService;
import com.team5.secondhand.global.util.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class StompMessageProcessor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final ApplicationEventPublisher publisher;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.debug("stomp command : {} , destination :  {} , sessionId : {}", headerAccessor.getCommand(), headerAccessor.getDestination(), headerAccessor.getSessionId());
        handleMessage(headerAccessor);
        return message;
    }

    private void handleMessage(StompHeaderAccessor headerAccessor) {
        if (headerAccessor == null || headerAccessor.getCommand() == null) {
            throw new MessageDeliveryException(ErrorType.BAD_REQUEST.getMessage());
        }

        switch (headerAccessor.getCommand()) {
            case CONNECT:
                Long memberId = getMemberIdByToken(headerAccessor.getFirstNativeHeader("Authorization"));
                sessionService.saveSession(headerAccessor.getSessionId(), memberId);
                break;
            case SUBSCRIBE:
                enterToChatRoom(headerAccessor);
                break;
            case UNSUBSCRIBE:
                exitToChatRoom(headerAccessor);
                break;
            case DISCONNECT:
                sessionService.deleteSession(headerAccessor.getSessionId());
                break;
        }
    }

    private Long getMemberIdByToken(String authorization) {
        if (authorization == null) {
            throw new MessageDeliveryException(ErrorType.UNAUTHORIZED.getMessage());
        }

        return jwtService.getMemberId(authorization).orElseThrow(() -> new MessageDeliveryException(ErrorType.UNAUTHORIZED.getMessage()));
    }

    private void enterToChatRoom(StompHeaderAccessor headerAccessor) {
        String memberId = sessionService.getMemberIdBySessionId(headerAccessor.getSessionId());
        String roomId = extractRoomId(headerAccessor.getDestination());
        publisher.publishEvent(new EnterChatRoomEvent(roomId, Long.valueOf(memberId)));
    }

    private void exitToChatRoom(StompHeaderAccessor headerAccessor) {
        String memberId = sessionService.getMemberIdBySessionId(headerAccessor.getSessionId());
        String roomId = extractRoomId(headerAccessor.getDestination());
        publisher.publishEvent(new ExitChatRoomEvent(roomId, Long.valueOf(memberId)));
    }

    private String extractRoomId(String destination) {
        if (destination == null) {
            throw new MessageDeliveryException(ErrorType.BAD_REQUEST.getMessage());
        }
        return destination.replace("/sub/", "");
    }
}
