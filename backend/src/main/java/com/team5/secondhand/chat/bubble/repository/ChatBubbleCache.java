package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatBubbleCache {
    Slice<ChatBubble> findAllByRoomId(String roomId, Pageable pageable);

    ChatBubble save(String key, ChatBubble chatBubble);

    int getLastPage(String key, int pageSize);
}
