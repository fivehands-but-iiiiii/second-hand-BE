package com.team5.secondhand.chat.chatroom.service;

import com.team5.secondhand.api.chatroom.dto.response.ChatLog;
import com.team5.secondhand.api.chatroom.dto.response.ChatroomSummary;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetainfoCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomMetaInfoService {

    private final ChatroomMetainfoCache metainfoCache;

    public void enterToChatRoom(String roomId, String memberId) {
        Chatroom chatroom = metainfoCache.findByChatroomId(roomId).orElseThrow();
        chatroom.enter(memberId);
    }

    public void exitToChatRoom(String roomId, String memberId) {
        Chatroom chatroom = metainfoCache.findByChatroomId(roomId).orElseThrow();
        chatroom.exit(memberId);
    }

    @Transactional(readOnly = true)
    public ChatLog getMessageInfo(String roomId, String memberId) {
        Chatroom chatroom = metainfoCache.findByChatroomId(roomId).orElseGet(() -> Chatroom.create(roomId, memberId));
        return ChatLog.of(chatroom, memberId);
    }

    @Transactional(readOnly = true)
    public List<ChatroomSummary> addLastMessage(List<ChatroomSummary> chatroomSummaries, String memberId) {

        return chatroomSummaries.stream()
                .map(s -> s.addChatLogs(getMessageInfo(s.getChatroomId(), memberId)))
                .collect(Collectors.toList());
    }
}
