package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.*;

@Service
@RequiredArgsConstructor
public class ChatLogService {
    private final int LOAD_SIZE = 25;

    private final ChatBubbleRepository chatBubbleRepository;

    public Slice<ChatBubble> getChatBubbles(int page, String roomId) {
        Pageable pageable = PageRequest.of(page, LOAD_SIZE, by("id").descending());
        return chatBubbleRepository.findAllByRoomId(roomId, pageable);
    }

    public ChatBubble getChatBubble(String id) {
        return chatBubbleRepository.findById(id).orElseThrow();
    }
}
