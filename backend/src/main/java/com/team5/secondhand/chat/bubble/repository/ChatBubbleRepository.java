package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.entity.ChatBubbleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatBubbleRepository {

    private final SpringDataChatBubbleRepository repository;

    public Slice<ChatBubbleEntity> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() + 1, Sort.by("createdAt").ascending());
        Page<ChatBubbleEntity> chatBubbles = repository.findAll(pageable);
        return new SliceImpl(chatBubbles.getContent().subList(0, Math.min(pageable.getPageSize(), chatBubbles.getContent().size())), pageable, chatBubbles.getSize() > chatBubbles.getSize());
    }

    public void saveAll(List<ChatBubbleEntity> allChatBubble) {
        repository.saveAll(allChatBubble);
    }
}
