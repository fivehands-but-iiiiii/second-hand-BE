//package com.team5.secondhand.global.event.chatroom;
//
//import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ChatroomCreatedEventhandler {
//
//    private final ApplicationEventPublisher eventPublisher;
//
//    @Async
//    @EventListener
//    public void createChatroomInfo (ChatroomCreatedEvent event) {
//        ChatroomInfo info = event.getInfo();
//        eventPublisher.publishEvent(new ChatroomMetaInfoCreateEvent(info));
//    }
//}
