package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatBubbleRepository {

    private final SpringDataChatBubbleRepository repository;

    public Slice<ChatBubble> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() + 1, Sort.by("createdAt").ascending());
        Page<ChatBubble> chatBubbles = repository.findAll(pageable);
        return new SliceImpl(chatBubbles.getContent().subList(0, Math.max(pageable.getPageSize(), chatBubbles.getSize())), pageable, chatBubbles.getSize() > chatBubbles.getSize());
    }
}
