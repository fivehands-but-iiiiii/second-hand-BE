package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatBubbleRepository {

    private final SpringDataChatBubbleRepository repository;

    public Slice<ChatBubble> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() + 1, Sort.by("createdAt").ascending());
        Page<ChatBubble> chatBubbles = repository.findAll(pageable);
        return new SliceImpl(chatBubbles.getContent().subList(0, Math.min(pageable.getPageSize(), chatBubbles.getContent().size())), pageable, chatBubbles.getSize() > chatBubbles.getSize());
    }

    public void saveAll(List<ChatBubble> allChatBubble) {
        repository.saveAll(allChatBubble);
    }
}
