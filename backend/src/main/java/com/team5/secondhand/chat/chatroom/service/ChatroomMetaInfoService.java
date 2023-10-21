package com.team5.secondhand.chat.chatroom.service;

import com.team5.secondhand.api.chatroom.dto.response.ChatLog;
import com.team5.secondhand.api.chatroom.dto.response.ChatroomSummary;
import com.team5.secondhand.api.chatroom.repository.ChatroomRepository;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetainfoCache;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetainfoRepository;
import com.team5.secondhand.chat.chatroom.repository.entity.ChatroomMetaInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomMetaInfoService {

    private final ChatroomMetainfoCache metainfoCache;
    private final ChatroomMetainfoRepository repository;

    public void enterToChatRoom(String roomId, Long memberId) {
        Chatroom chatroom = metainfoCache.findByChatroomId(roomId).orElseThrow();
        chatroom.enter(memberId);
        metainfoCache.saveChatroom(roomId, chatroom);
    }

    public void exitToChatRoom(String roomId, Long memberId) {
        Chatroom chatroom = metainfoCache.findByChatroomId(roomId).orElseThrow();
        chatroom.exit(memberId);
        metainfoCache.saveChatroom(roomId, chatroom);
    }

    @Transactional(readOnly = true)
    public ChatLog getMessageInfo(String roomId, Long memberId) {
        Chatroom chatroom = metainfoCache.findByChatroomId(roomId).orElseGet(() -> Chatroom.create(roomId, memberId));
        return ChatLog.of(chatroom, memberId);
    }

    @Transactional(readOnly = true)
    public List<ChatroomSummary> addLastMessage(List<ChatroomSummary> chatroomSummaries, Long memberId) {

        return chatroomSummaries.stream()
                .map(s -> s.addChatLogs(getMessageInfo(s.getChatroomId(), memberId)))
                .collect(Collectors.toList());
    }

    public Chatroom saveChatroom(Chatroom chatroom) {
        metainfoCache.saveChatroom(chatroom.getChatroomId(), chatroom);
        ChatroomMetaInfoEntity save = repository.save(ChatroomMetaInfoEntity.fromDomain(chatroom));
        return save.toDomain();
    }

    public Chatroom saveBubbleToChatroom(ChatBubble chatBubble) {
        Chatroom chatroom = metainfoCache.findByChatroomId(chatBubble.getChatroomId()).orElseThrow(NoSuchElementException::new);
        chatroom.updateLastMessage(chatBubble);
        return saveChatroom(chatroom);
    }
}
