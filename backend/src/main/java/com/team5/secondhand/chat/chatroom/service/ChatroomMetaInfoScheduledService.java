package com.team5.secondhand.chat.chatroom.service;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetainfoCache;
import com.team5.secondhand.chat.chatroom.repository.ChatroomMetainfoRepository;
import com.team5.secondhand.chat.chatroom.repository.entity.ChatroomMetaInfoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatroomMetaInfoScheduledService {

    private final ChatroomMetainfoCache cache;
    private final ChatroomMetainfoRepository repository;

    @Scheduled(cron = "0 * * * * *")
    public void moveDataCacheToRepository() {
        List<Chatroom> allChatBubble = cache.findAll();
        List<ChatroomMetaInfoEntity> collect = allChatBubble.stream().map(ChatroomMetaInfoEntity::fromDomain).collect(Collectors.toList());
        cache.clear();
        repository.saveAll(collect);
    }
}
