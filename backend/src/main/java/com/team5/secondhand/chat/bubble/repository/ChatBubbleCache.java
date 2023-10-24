package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatBubbleCache {
    Slice<ChatBubble> findAllByRoomId(String key, Pageable pageable);

    List<ChatBubble> findAllByRoomId(String key);

    ChatBubble save(String key, ChatBubble chatBubble);

    int getLastPage(String key, int pageSize);

    void clear(String key);
}
