package com.team5.secondhand.chat.bubble.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.global.properties.ChatCacheProperties;
import com.team5.secondhand.global.util.RedisListOperationsHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatBubbleCache {

    private final RedisListOperationsHelper operationsHelper;
    private final ChatCacheProperties chatBubbleProperties;

    private long getStartIndex(Pageable page) {
        return (-1L * page.getPageSize() * page.getPageNumber());
    }

    private Slice<ChatBubble> getSlice(List<ChatBubble> messages, Pageable pageable) {
        if (messages.isEmpty()) {
            throw new IndexOutOfBoundsException("빈 페이지 입니다.");
        }

        if (messages.size() > pageable.getPageSize()) {
            messages.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(
                messages.subList(0, Math.min(pageable.getPageSize(), messages.size())), pageable,
                pageable.getPageSize() < messages.size());
    }

    public Slice<ChatBubble> findAllByRoomId(String key, Pageable pageable) {

        long startIndex = getStartIndex(pageable);
        long endIndex = startIndex - pageable.getPageSize();

        List<ChatBubble> messages = operationsHelper.getList(generateChatLogKey(key), endIndex - 1, startIndex,
                ChatBubble.class);

        return getSlice(messages, pageable);
    }

    public List<ChatBubble> findAllByRoomId(String key) {
        long size = operationsHelper.size(generateChatLogKey(key));
        return operationsHelper.getList(generateChatLogKey(key), 0, size, ChatBubble.class);
    }

    public ChatBubble save(String key, ChatBubble chatBubble) {
        operationsHelper.add(generateChatLogKey(key), chatBubble);
        return chatBubble;
    }

    public int getLastPage(String key, int pageSize) {
        Long size = operationsHelper.size(generateChatLogKey(key));
        return (int) (size / pageSize);
    }

    public void clear(String key) {
        operationsHelper.delete(generateChatLogKey(key));
    }

    public List<ChatBubble> findAllBubbles() {
        return operationsHelper.popAll(chatBubbleProperties.getKey(), ChatBubble.class);
    }

    private String generateChatLogKey (String roomId) {
        return String.format("%s%s", chatBubbleProperties.getKey(), roomId);
    }
}
