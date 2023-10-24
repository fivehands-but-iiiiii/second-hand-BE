package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.repository.entity.BubbleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatBubbleRepository {
    Slice<ChatBubble> findAllByRoomId(String roomId, Pageable pageable);

    ChatBubble save(String key, ChatBubble chatBubble);
}
