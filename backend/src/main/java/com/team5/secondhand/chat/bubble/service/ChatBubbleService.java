package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.chat.topic.service.RedisChatPublisher;
import com.team5.secondhand.global.properties.ConstProperties;
import com.team5.secondhand.global.event.chatbubble.ChatBubbleArrivedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatBubbleService {

    private final ConstProperties.Chat chatContext;
    private final RedisChatPublisher redisChatPublisher;
    private final ApplicationEventPublisher publisher;
    private final ChatBubbleRepository repository;

    public ChatBubbleService(ConstProperties properties, ChatBubbleRepository repository, RedisChatPublisher redisChatPublisher, ApplicationEventPublisher publisher) {
        this.chatContext = properties.getChat();
        this.repository = repository;
        this.redisChatPublisher = redisChatPublisher;
        this.publisher = publisher;
    }

    @Transactional(readOnly = true)
    public Slice<ChatBubble> getChatBubbles(int page, String roomId) {
        String key = generateChatLogKey(roomId);
        Pageable pageable = PageRequest.of(page, chatContext.getPageSize(), Sort.by("createdAt").ascending());
        Slice<ChatBubble> list = repository.findAllByRoomId(key, pageable);
        return list;
    }

    public ChatBubble saveChatBubble(ChatBubble chatBubble) {
        String key = generateChatLogKey(chatBubble.getRoomId());
        return repository.save(key, chatBubble);
    }

    private String generateChatLogKey (String roomId) {
        return String.format("%s%s:logs", chatContext.getBucket(), roomId);
    }

    @Transactional
    public void handleMessage(ChatBubble message) {
        saveChatBubble(message);

        redisChatPublisher.publish(message);
        publisher.publishEvent(new ChatBubbleArrivedEvent(message));
    }
}
