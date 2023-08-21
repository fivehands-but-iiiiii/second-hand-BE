package com.team5.secondhand.chat.chatroom.service;

import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetaInfoRepository;
import com.team5.secondhand.global.event.chatroom.ChatroomMetaInfoCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomMetaInfoService {
    private final ChatroomMetaInfoRepository metaInfoRepository;

    @Async
    @EventListener
    public void metaInfoCreate(ChatroomMetaInfoCreateEvent event) {
        ChatroomInfo info = event.getInfo();
        Chatroom init = Chatroom.init(info);
        metaInfoRepository.save(init);
    }
}
