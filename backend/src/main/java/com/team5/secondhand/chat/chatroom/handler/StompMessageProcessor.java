package com.team5.secondhand.chat.chatroom.handler;

import com.team5.secondhand.chat.exception.ErrorType;
import com.team5.secondhand.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        String memberId = getMemberIdByToken(headerAccessor.getFirstNativeHeader("Authorization"));

        switch (headerAccessor.getCommand()) {
            case CONNECT:
            case SUBSCRIBE:
                enterToChatRoom(headerAccessor, memberId);
            case SEND:
            default:
                break;
        }
    }

    private String getMemberIdByToken(String authorization) {
        if (authorization == null) {
            throw new MessageDeliveryException(ErrorType.UNAUTHORIZED.getMessage());
        }

        return jwtService.getMemberId(authorization).orElseThrow(() -> new MessageDeliveryException(ErrorType.UNAUTHORIZED.getMessage()));
    }

    private void enterToChatRoom(StompHeaderAccessor headerAccessor, String memberId) {
        String roomId = extractRoomId(headerAccessor.getDestination());
        //todo: redis에 해당 채팅방에 멤버 상태 변경
        //todo: 읽은 메시지 0으로 초기화
    }

    private String extractRoomId(String destination) {
        if (destination == null) {
            throw new MessageDeliveryException(ErrorType.BAD_REQUEST.getMessage());
        }
        return destination.replace("/sub/", "");
    }
}
