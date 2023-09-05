package com.team5.secondhand.global.event.chatbubble;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.global.model.BaseEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ChatBubbleArrivedEvent extends BaseEvent {
    private final ChatBubble chatBubble;

    public ChatBubbleArrivedEvent(ChatBubble chatBubble) {
        super();
        this.chatBubble = chatBubble;
        log.info("ChatBubble Arrived Event Occur = {}", chatBubble.toString());
    }

    public String getChatReceiverId() {
        return chatBubble.getReceiver();
    }
}
