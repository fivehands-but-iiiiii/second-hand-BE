package com.team5.secondhand.chat.chatroom.handler;

import com.team5.secondhand.chat.exception.ErrorType;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        ErrorType errorType = ErrorType.of(ex.getMessage());
        if (errorType == ErrorType.DEFAULT) {
            return super.handleClientMessageProcessingError(clientMessage, ex);
        }

        return errorMessage(errorType.getErrorMessage());
    }

    private Message<byte[]> errorMessage(String errorMessage) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
